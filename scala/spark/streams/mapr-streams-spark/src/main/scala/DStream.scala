import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka09._
import org.apache.spark.streaming.kafka09.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka09.ConsumerStrategies.Subscribe
import org.apache.spark._
import org.apache.spark.streaming._

object DStream {

  val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
  val streamingContext = new StreamingContext(conf, Seconds(1))

  def init() {
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("/user/mapr/pump:topic0")
    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(record => (record.key, record.value)).print()
  }

  def main (args: Array[String]) {
    init()
    streamingContext.start()
    streamingContext.awaitTermination()
  }

}