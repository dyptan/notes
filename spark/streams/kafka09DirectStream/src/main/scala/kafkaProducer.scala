/*
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{ConstantInputDStream, DStream}
import org.apache.spark.streaming.kafka.producer._

case class Item(id: Int, value: Int) {
  override def toString: String = s"""{"id":"$id","value":"$value"}"""
}

object kafkaProducer {
  val Array(kafkaBrokers, topics, numMessages) = Array("node8:9092", "mytopic", "10000")
  val batchTime = Seconds(1)
  val producerConf = new ProducerConf(
    bootstrapServers = kafkaBrokers.split(",").toList)

  val items = (0 until numMessages.toInt).map(i => Item(i, i).toString)

  val sc = SparkSession.builder().appName("streams").master("local[1]").getOrCreate().sparkContext

  val ssc = new StreamingContext(sc, batchTime)

  val defaultRDD: RDD[String] = ssc.sparkContext.parallelize(items)

  val dStream: DStream[String] = new ConstantInputDStream[String](ssc, defaultRDD)

  dStream.foreachRDD(_.sendToKafka(topics, producerConf))

  ssc.start()
  ssc.awaitTermination()

}*/
