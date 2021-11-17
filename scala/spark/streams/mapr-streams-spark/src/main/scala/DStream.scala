import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka09._
import org.apache.spark.streaming.kafka09.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka09.ConsumerStrategies.Subscribe
import org.apache.spark._
import org.apache.spark.streaming._

object DStream {

  val conf = new SparkConf()
  .setMaster("local[2]")
  .setAppName("NetworkWordCount")
  .set("spark.streaming.backpressure.enabled", "true")
  .set("spark.streaming.kafka.maxRatePerPartition", "10")
  .set("spark.streaming.kafka.consumer.poll.ms", "1000")
  .set("spark.streaming.kafka.allowNonConsecutiveOffsets", "true")

  val streamingContext = new StreamingContext(conf, Seconds(1))

  def init(args: Array[String]): Unit = {
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "DStreamExample",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
      // "max.poll.records" -> "100"
    )

    val topics = Array(args(0))
    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.foreachRDD{
      rdd => 
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        println(s"Ranges for batch: ${offsetRanges.mkString}")
        rdd.foreach(println)

        stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
    }
  }
  // map(record => (record.key, record.value)).print()

  def main (args: Array[String]) = {
    init(args)
    streamingContext.start()
    streamingContext.awaitTermination()
  }

}