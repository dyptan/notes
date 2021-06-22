Test MaprDB secondary index performance

`java -jar dataGen-BETA-jar-with-dependencies.jar JsonBuilder 100000`


```
  hadoop fs -mkdir /mapr/cyber.mapr.cluster/user/mapr/datevg
  maprcli table create -path /mapr/cyber.mapr.cluster/user/mapr/datevg/payrolls-dn-scala-test -tabletype json
  mapr importJSON -src file:///tmp/dummy_data.json -dst /mapr/cyber.mapr.cluster/user/mapr/datevg/payrolls-dn-scala-test
  sudo yum install mapr-gateway
  mapr dbshell
  maprcli cluster gateway set -dstcluster cyber.mapr.cluster -gateways localhost
  maprcli table index add -path /mapr/cyber.mapr.cluster//user/mapr/datevg/payrolls-dn-scala-test -index natural_key_index -indexedfields _natural_key,_metadata.space_id,_gdpr_access_controls[].scope -json
  maprcli table index add -path /mapr/cyber.mapr.cluster//user/mapr/datevg/payrolls-dn-scala-test -index space_id_index -indexedfields _metadata.space_id,_gdpr_access_controls[].scope -json
  maprcli table index list -path /mapr/cyber.mapr.cluster//user/mapr/datevg/payrolls-dn-scala-test -json
  maprcli table cf list -path /mapr/cyber.mapr.cluster//user/mapr/datevg/payrolls-dn-scala-test -json
```
