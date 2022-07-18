package com.example

import java.{util => ju}

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition

import scala.collection.JavaConverters._
import java.nio.file.Paths

object Kafka10Consumer {

  val propertiesFile = new java.io.FileInputStream("conf/consumer.properties")
  val properties = new java.util.Properties()
  properties.load(propertiesFile)


  def main(args: Array[String]): Unit = {

    val kafkaParams = new ju.HashMap[String, Object]()
    kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")
    kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaParams.put("enable.auto.commit", "false")
    kafkaParams.put("group.id", "Kafka10Consumer")
    kafkaParams.put("max.poll.records", "10")
    kafkaParams.put("auto.offset.reset", args.lift(1).getOrElse("earliest"))

    // val topicPartition1 = new TopicPartition("/user/mapr/pump:topic0", 0)
    // val topicPartition2 = new TopicPartition("/user/mapr/pump:topic0", 1)

    val consumer = new KafkaConsumer[Any, Any](properties)

    val topics = new ju.ArrayList[String];
    topics.add(args.lift(0).getOrElse("/user/mapr/pump:topic0"))

    consumer.subscribe(topics)

    println(s"subscribed to topics: $topics")
    try {
    
      while (true) {
        Thread.sleep(1000)
        val records = consumer.poll(10)
        println(s"records pulled: ${records.count()}")
        records.iterator.asScala
        .foreach(record => println(s"Partition: ${record.partition}, Value: ${record.value}, Offset: ${record.offset}"))
        
        consumer.commitAsync()
      }
    }
    finally {
      consumer.close()
    }

  
    // // This is to test manual offset rewind (seek):
    // val currentOffsets = Map(topicPartition1 -> 0, topicPartition2 -> 0)
    // val toSeek = currentOffsets

    //    val aor = kafkaParams.get(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG)
    //    val shouldSuppress =
    //      aor != null && aor.asInstanceOf[String].toUpperCase(Locale.ROOT) == "NONE"
    //    try {
    //      if (KafkaUtils.isStreams(currentOffsets.map(a => (a._1, a._2.toLong)))) {
    //        KafkaUtils.waitForConsumerAssignment(consumer, toSeek.keys)
    //      } else {
    //        consumer.poll(0)
    //      }
    //    } catch {
    //      case x: NoOffsetForPartitionException if shouldSuppress =>
    //        print("Catching NoOffsetForPartitionException since " +
    //          ConsumerConfig.AUTO_OFFSET_RESET_CONFIG + " is none.  See KAFKA-3370")
    //    }
    // toSeek.foreach { case (topicPartition, offset) =>
    //   consumer.seek(topicPartition, offset)
    // }


    }
}
