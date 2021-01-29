You need to create Streams with specific name

    maprcli stream create -path /user/mapr/pump -produceperm u:mapr -consumeperm u:mapr -topicperm u:mapr
    maprcli stream topic create -path /user/mapr/pump -topic topic0 -partitions 4



Compile the source against your Spark version as following

    scalac -classpath $(echo *.jar /opt/mapr/spark/spark-2.2.1/jars/*.jar | tr ' ' ':') Stream.scala -d Stream.jar


submit the jar to your cluster

    /opt/mapr/spark/spark-2.2.1/bin/spark-submit --class Scala Scala.jar

populate the stream with some data

    mapr perfproducer -ntopics 1 -path /user/mapr/pump -nmsgs 50 -npart 4 -rr
    
