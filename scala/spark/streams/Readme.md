You need to create Streams with specific name

    maprcli stream create -path /user/mapr/pump -produceperm u:mapr -consumeperm u:mapr -topicperm u:mapr
    maprcli stream topic create -path /user/mapr/pump -topic topic0 -partitions 4

 

Upload the source file I have attached to your Spark node

 

Compile the source against your Spark version as following

    scalac -classpath $(echo *.jar /opt/mapr/spark/spark-2.3.3/jars/*.jar | tr ' ' ':') Main.scala -d Main.jar

 

submit the jar to your cluster

    /opt/mapr/spark/spark-2.3.3/bin/spark-submit --class Main Main.jar

 

populate the stream with some data

    mapr perfproducer -ntopics 1 -path /user/mapr/pump -nmsgs 50 -npart 4 -rr
    
```
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

object Main {

  val spark = org.apache.spark.sql.SparkSession.builder().appName("test").master("local[1]").getOrCreate()


  def readFromES() = {
    val kafkaDF: DataFrame = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers","localhost:9092")
      .option("subscribe", "/user/mapr/pump:topic0")
      .load()
    kafkaDF
      .select(col("topic"), expr("cast(value as string) as actualValue"))
      .writeStream
      .format("console")
      .outputMode("append")
      .start()
      .awaitTermination()
  }

  def main(args: Array[String]) = {


    readFromES()
  }

}

```
