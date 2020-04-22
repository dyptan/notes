import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka09._

val kafkaParams = Map[String, Object]("bootstrap.servers"-> "localhost:9092",
"auto.offset.reset"-> "latest",
"enable.auto.commit"-> "false",
"group.id"->"1",
"key.deserializer"-> "org.apache.kafka.common.serialization.StringDeserializer",
"value.deserializer"-> "org.apache.kafka.common.serialization.StringDeserializer")

val ssc = new StreamingContext(sc, Seconds(1))

val consumerStrategy = ConsumerStrategies.SubscribePattern[String, Array[Byte]](
      java.util.regex.Pattern.compile("topic1"), kafkaParams)

val messagesDStream =  KafkaUtils.createDirectStream[String, Array[Byte]](
      ssc, LocationStrategies.PreferConsistent, consumerStrategy)      

// messagesDStream.foreachRDD(rdd =>
//       rdd.foreachPartition(partition =>
//        partition.foreach(consumerRecord => println(s"""Printing message : $consumerRecord"""))
//     )
// )

messagesDStream.foreachRDD(rdd =>
    rdd.foreachPartition(partition =>
    for (record <- partition) println( record.value() ) )
)


ssc.start()
