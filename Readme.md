## MAPR

#### Repositories

```
https://package.mapr.com/releases/v6.1.0/ubuntu/dists/binary/Release.gpg
deb https://package.mapr.com/releases/MEP/MEP-6.0/ubuntu binary trusty
deb https://package.mapr.com/releases/v6.0.0/ubuntu binary trusty
```

#### Service management

```
maprcli node list -columns service
maprcli node services -cluster `hostname -f` restart
maprcli node services -name resourcemanager -action restart -nodes `hostname -f`
maprcli node services -name hs2 -action stop -nodes `hostname -f`
maprcli node services -name hivemeta -action restart -nodes `hostname -f`
```

#### managing ElasticStreams

```
maprcli stream create -path /user/mapr/pump -produceperm u:mapr -consumeperm u:mapr -topicperm u:mapr
maprcli stream topic create -path /user/mapr/pump -topic input -partitions 3
maprcli stream topic create -path /user/mapr/pump -topic alert -partitions 3
```
#### debugging ES
```
maprcli stream topic info -path /user/mapr/pump -topic alert -json
maprcli stream info -path /user/mapr/pump
maprcli stream topic list -path /user/mapr/pump -json
maprcli stream cursor list -path /user/mapr/pump -topic alert -json
maprcli stream assign list -path /user/mapr/pump -topic alert -json

mapr streamanalyzer -path /user/mapr/pump -printMessages true
```

## Linux 

#### System resources limits check

```
ulimit -Ha (shows all global limits for current user)
ps -U mapr -L | wc -l (will give you the number of running threads)
ls /proc/<RM PID>/fd | wc -l (the number of opened files and connections for RM)
lsof -umapr | wc -l (the count of utilized FDs out of available pool)
```

#### user managenent

```
groupadd -g 5000 mapr
useradd -g 5000 -u 5000 -m mapr
usermod -a -G sudo mapr
sudo usermod -g 1001 -u 1001 ivan
```

#### Yum

```
sudo yum clean all
sudo yum list --showduplicates mapr-tez
```

#### Search class

```
for i in *.jar; do jar -tvf "$i" | grep -Hsi ClassName && echo "$i"; done
grep -R org.apache.tez.runtime.api.Event ./
```

#### Replace Hadoop lib jars used by Tez with corresponding jars in Hadoop lib directories.

```
ls tez_lib_bkp/ | sed 's/2.7.0-mapr-1710.jar/*/g' | while read file; do find /opt/mapr/hadoop -name $file | grep -v "test\|sources" |head -1 ; done | xargs cp -t /opt/mapr/tez/tez-0.8/lib/
```


## Kubernetes

#### Ping nodes
```
 kubectl run -i --tty --rm debug --image=busybox --restart=Never -- sh
```

## HADOOP

#### Yarn

```
yarn daemonlog -setlevel maprdemo:8088 org.apache.hadoop DEBUG
yarn logs -applicationId application_1528173243110_0007
yarn application -appStates FINISHED -list
```

#### MapReduce

```
hadoop fs -mkdir -p /user/mapr/mapreduce
hadoop fs -put /opt/mapr/hadoop/hadoop-2.7.0/NOTICE.txt /user/mapr/mapreduce/
hadoop jar /opt/mapr/hadoop/hadoop-2.7.0/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.0-mapr-1803.jar wordcount  /user/mapr/mapreduce/NOTICE.txt /user/mapr/mapreduce/out
hadoop jar /opt/mapr/hadoop/hadoop-2.7.0/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.0-mapr-1808.jar sleep -m 1 -mt 60000 -r 1 -rt 60000
```

## SPARK

#### enable FS debug and OOM dump
```
/opt/mapr/spark/spark-2.3.2/bin/run-example --master yarn --deploy-mode client SparkPi 10
 --conf spark.hadoop.fs.mapr.trace=debug
 --conf spark.executor.extraJavaOptions="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp"
```
#### If there is no external metastore:
`./bin/spark-shell --conf spark.sql.catalogImplementation=in-memory`
#### run HS with spar-submit
`\bin\spark-submit  --class org.apache.spark.deploy.history.HistoryServer spark-internal`

#### load data
```
val fifaDF = spark.read.option("header", true).csv("/tmp/data.csv")
import org.apache.spark.sql
fifaDF.withColumn("age", $"age".cast(sql.types.IntegerType)).select("name", "club", "age").write.parquet("/tmp/fifa/par")
val fifaFromParDF = spark.read.parquet("/tmp/fifa/par")
val fifaAvgAge = fifaFromParDF.groupBy().avg("age")
fifaAvgAge.toJSON.rdd.saveAsTextFile("/tmp/fifa/out_json")
```

#### Run multiple executors at once

```
for (( c=1; c<=5; c++ ))
do
  ./bin/spark-sql --master yarn -f ~/1097.sql --executor-memory 600Mb --executor-cores 1 &
done

while true; do ps -ef  | grep 24106; sleep 1; done
```

## KAFKA

```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
bin/kafka-topics.sh --list --zookeeper localhost:2181
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test
```

## HIVE

```
CREATE TABLE students (name VARCHAR(64), age INT, gpa DECIMAL(3, 2));
INSERT INTO TABLE students VALUES ('fred flintstone', 35, 1.28), ('barney rubble', 32, 2.32);
create view myview as select * from students where age > 33;

CREATE OR REPLACE VIEW myview as select * from students where age < 33;
```
## OOZIE

```
oozie admin -oozie http://localhost:11000/oozie -shareliblist hive*
hadoop fs -mkdir -p /user/mapr/examples/apps/spark/lib/
hadoop fs -put abr_curat.py /user/mapr/examples/apps/spark/lib/
hadoop fs -put job.properties /user/mapr/examples/apps/spark/
hadoop fs -put workflow.xml /user/mapr/examples/apps/spark/

export SPARK_HOME=/opt/mapr/spark/spark-2.3.1/
export OOZIE_HOME=/opt/mapr/oozie/oozie-4.3.0/

sudo cp $SPARK_HOME/conf/spark-defaults.conf $OOZIE_HOME/share/lib/spark/
sudo cp $SPARK_HOME/conf/hive-site.xml $OOZIE_HOME/share/lib/spark/
sudo cp $SPARK_HOME/python/lib/py4j*src.zip $OOZIE_HOME/share/lib/spark/
sudo cp $SPARK_HOME/python/lib/pyspark*.zip $OOZIE_HOME/share/lib/spark/

maprcli node services -name oozie -action restart -nodes node6

$OOZIE_HOME/bin/oozie job -oozie="http://localhost:11000/oozie" -config ~/job.properties -run
$OOZIE_HOME/bin/oozie job -info <id>
$OOZIE_HOME/bin/oozie job -log <id>
```

#### CSVs

curl -O https://raw.githubusercontent.com/amanthedorkknight/fifa18-all-player-statistics/master/2019/data.csv
