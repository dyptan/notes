# 1. Contents
 - [Hadoop](#hadoop) 
 - [Linux](#linux) 
 - [Spark](#spark)

## SPARK

```
export SPARK_PRINT_LAUNCH_COMMAND=1
/opt/mapr/spark/spark-2.3.2/bin/run-example --master yarn --deploy-mode client SparkPi 10
 --conf spark.executor.extraJavaOptions="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp"
 --conf spark.executor.extraJavaOptions="-XX:ErrorFile=targetDir/hs_err_pid_%p.log"
 --conf spark.driver.extraJavaOptions="-verbose"
 &> /tmp/file.log
```

If there is no external metastore:

`./bin/spark-shell --conf spark.sql.catalogImplementation=in-memory`

run HS with spark-submit

`\bin\spark-submit  --class org.apache.spark.deploy.history.HistoryServer spark-internal`

Remove class from JIT compilations

`--conf
spark.executor.extraJavaOptions="-XX:CompileCommand=exclude,org.apache.spark.util.SizeEstimator  -XX:CompileCommand=exclude,org.apache.spark.util.SizeEstimator.*"
`


## Linux 

#### Bash

```
for (( c=1; c<=5; c++ ))
do
  ./bin/spark-sql --master yarn -f ~/1097.sql --executor-memory 600Mb --executor-cores 1 &
done

while true; do ps -ef  | grep 24106; sleep 1; done
```

#### System resources

```
ulimit -Ha (shows all global limits for current user)
ps -U mapr -L | wc -l (will give you the number of running threads)
ls /proc/<PID>/fd | wc -l (the number of opened files and connections for PID)
sudo lsof -u mapr  | egrep -v "mem|DEL|cwd|rtd|txt" | wc -l #the count of utilized FDs out of available pool
```

#### SSH tricks

Forward local port 2222 to ssh port on remote node (useful for scp uploads)
```
ssh -g -L 2222:0.0.0.0:22 mapr@node14
```
Login to VM machine via hypervisor as "jump" host
```
ssh -J mapr@work mapr@node4
```

#### Add permanent routes in Centos7
```
echo "192.168.0.0/24 via 192.168.33.1" | sudo tee --append /etc/sysconfig/network-scripts/route-enp0s8
sudo systemctl restart network
```

#### Search class in multiple Jars

```
for i in *.jar; do jar -tvf "$i" | grep -Hsi ClassName && echo "$i"; done
or
find /opt/mapr/ -name "*.jar" -exec sh -c 'jar -tf {}|grep -H --label {} org.apache.log4j.rolling.RollingFileAppender' \;
```

## HADOOP

#### Yarn

```
yarn daemonlog -setlevel maprdemo:8088 org.apache.hadoop DEBUG
yarn logs -applicationId application_1528173243110_0007
yarn application -appStates FINISHED -list
```

### KAFKA

```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
bin/kafka-topics.sh --list --zookeeper localhost:2181
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test
```

### HIVE

```
CREATE TABLE students (name VARCHAR(64), age INT, gpa DECIMAL(3, 2));
INSERT INTO TABLE students VALUES ('fred flintstone', 35, 1.28), ('barney rubble', 32, 2.32);
create view myview as select * from students where age > 33;

CREATE OR REPLACE VIEW myview as select * from students where age < 33;
```
