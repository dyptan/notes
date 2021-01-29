package org.apache.spark.streaming.kafka10

import java.{util => ju}

import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.TopicPartition

object App {
  def main(args: Array[String]): Unit = {

    val kafkaParams = new ju.HashMap[String, Object]()
    kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")
    kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaParams.put("enable.auto.commit", "false")
    kafkaParams.put("group.id", "subsgroup")
    kafkaParams.put("max.partition.fetch.bytes", "5242880")
    kafkaParams.put("auto.offset.reset", "earliest")

    val topicPartition1 = new TopicPartition("/user/mapr/pump:topic0", 0)
    val topicPartition2 = new TopicPartition("/user/mapr/pump:topic0", 1)

    val consumer = new KafkaConsumer[Any, Any](kafkaParams)

    val topics = new ju.ArrayList[String];
    topics.add("/user/mapr/pump:topic0")
    consumer.subscribe(topics)

    val currentOffsets = Map(topicPartition1 -> 0, topicPartition2 -> 0)
    val toSeek = currentOffsets

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
    toSeek.foreach { case (topicPartition, offset) =>
      consumer.seek(topicPartition, offset)
    }
  }
}
