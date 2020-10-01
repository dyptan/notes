import java.util

import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.spark.sql.SparkSession


object kafkaDirectStream {

  def main(args: Array[String]): Unit = {

  val topic1 = "/user/mapr/pump:topic0";


  val kafkaParams: Map[String, Object] = Map(
  "key.serializer" -> classOf[StringSerializer], // send data to kafka
  "value.serializer" -> classOf[StringSerializer],
  "key.deserializer" -> classOf[StringDeserializer], // receiving data from kafka
  "value.deserializer" -> classOf[StringDeserializer],
  "auto.offset.reset" -> "earliest",
  "enable.auto.commit" -> false.asInstanceOf[Object],
    "group.id" -> "group1",
    "spark.kafka.poll.time" -> "3000"
  )

  import org.apache.spark.streaming._

  val checkpointDirectory = "maprfs:///opt/checkpoint/Processor"


  val listTopics = Array(topic1)
//  listTopics.add(topic1)
//  listTopics.add(topic2);

   import org.apache.spark.streaming.kafka09.{ConsumerStrategies, KafkaUtils, LocationStrategies};




    val sc = SparkSession.builder().appName("streams").master("local[1]").getOrCreate().sparkContext
    val ssc = new StreamingContext(sc, Seconds(1))

//    val topicc = java.util.regex.Pattern.compile(topic1)
//    val consumerStrategy = ConsumerStrategies.SubscribePattern[String, String](
//      topicc, kafkaParams)

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](listTopics.toSet, kafkaParams)
    )


//    val processedStream = stream.map(record => (record.key(), record.value()))
//    processedStream.print()

    stream.foreachRDD(rdd =>
      rdd.foreachPartition(partition =>
        for (record <- partition) println( record.value() ) )
    )

    ssc.start()
    ssc.awaitTermination()

  }
}