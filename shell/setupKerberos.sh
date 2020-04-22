
# fill up the some vars
FQDN=`hostname -f`
HOSTNAME=`echo $FQDN | awk -F "." ' {print $1} '` 
DOMAIN=`echo $FQDN | awk -F "." '{for (i=2; i<NF; i++) printf $i "."; print $NF}' `
DOMAINH=`echo $DOMAIN | awk '{print toupper($0)}'`
CLUSTER_NAME=cyber.mapr.cluster

#init the KDC
kdb5_util create -s -r $DOMAINH -P 12345

echo "*/admin@$DOMAINH    *"  > /etc/krb5kdc/kadm5.acl

#create principals for admin user
echo "addprinc -pw 123456 root/admin@$DOMAINH" | kadmin.local

#Restart Kerberos server and KDC
sudo /etc/init.d/krb5-kdc restart
sudo /etc/init.d/krb5-admin-server restart

#create principal for mapr user
echo "addprinc -randkey mapr/$CLUSTER_NAME" | kadmin -p root/admin -w 123456
echo "addprinc -randkey mapr/$FQDN" | kadmin -p root/admin -w 123456

#create keytab for mapr user
echo "ktadd -k /opt/mapr/conf/mapr.keytab mapr/$CLUSTER_NAME@$DOMAINH" | kadmin -p root/admin -w 123456
echo "ktadd -k /opt/mapr/conf/mapr.keytab mapr/$FQDN@$DOMAINH" | kadmin -p root/admin -w 123456

# Restart KDC services again
sudo /etc/init.d/krb5-kdc restart
sudo /etc/init.d/krb5-admin-server restart

#configure sh
/opt/mapr/server/configure.sh -C localhost -Z localhost -N $CLUSTER_NAME -secure -genkeys -kerberosEnable

#It has resulted in the following configuration:
#cat /opt/mapr/conf/mapr-clusters.conf 
cyber.mapr.cluster secure=true kerberosEnable=true cldbPrincipal=mapr/cyber.mapr.cluster@NODE9 localhost:7222

### cat /etc/krb5.conf
[logging]
 default = FILE:/var/log/krb5libs.log
 kdc = FILE:/var/log/krb5kdc.log
 admin_server = FILE:/var/log/kadmind.log

[libdefaults]
 dns_lookup_realm = false
 ticket_lifetime = 24h
 renew_lifetime = 7d
 forwardable = true
 rdns = false
 default_realm = NODE9
 default_ccache_nae = KEYRING:persistent:%{uid}

[realms]
 NODE9 = {
  kdc = node9.cluster.com
  admin_server = node9.cluster.com
 }

[domain_realm]
 .cluster.com = NODE9
 cluster.com = NODE9
####


### My effective hive-site.xml is as simple as:
<configuration>
 <property>
    <name>hive.server2.authentication</name>
    <value>KERBEROS</value>
    <description>authentication type</description>
  </property>

  <property>
    <name>hive.server2.authentication.kerberos.principal</name>
    <value>mapr/node9.cluster.com@NODE9</value>
    <description>HiveServer2 principal. If _HOST is used as the FQDN portion, it will be replaced with the actual hostname of the running instance.</description>
  </property>

  <property>
    <name>hive.server2.authentication.kerberos.keytab</name>
    <value>/opt/mapr/conf/mapr.keytab</value>
    <description>Keytab file for HiveServer2 principal</description>
  </property>
</configuration>
###

## Auth in the cluster
kinit -kt /opt/mapr/conf/mapr.keytab mapr/$CLUSTER_NAME@$DOMAINH 

# check
klist
Ticket cache: FILE:/tmp/krb5cc_5000
Default principal: mapr/node9.cluster.com@NODE9

Valid starting     Expires            Service principal
26.09.19 10:38:23  27.09.19 10:38:23  krbtgt/NODE9@NODE9

# Get the mapr ticket
maprlogin kerberos

# Eventually I managed to run the STS 
./bin/spark-submit --class org.apache.spark.sql.hive.thriftserver.HiveThriftServer2

# and connect via beeline remotely:
./bin/beeline -u "jdbc:hive2://localhost:10000/;principal=mapr/node9.cluster.com@NODE9"

## For extended debug I used the opts:
./bin/spark-submit --driver-java-options "-Dsun.security.krb5.debug=true -Dsun.security.spnego.debug=true -Djavax.net.debug=all" --class org.apache.spark.sql.hive.thriftserver.HiveThriftServer2