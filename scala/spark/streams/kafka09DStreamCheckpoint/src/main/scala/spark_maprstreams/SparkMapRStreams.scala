package maprstreams_vertica

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.common.serialization.Serializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{ Seconds, StreamingContext }
import org.apache.spark.streaming.kafka09.{ ConsumerStrategies, KafkaUtils, LocationStrategies }
import org.apache.spark.streaming.dstream.{ DStream, InputDStream }
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka.producer._
import java.util.Locale
import java.util.Random
import java.util.{ Map => JMap }
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import scala.collection.mutable.ArrayBuffer

import java.sql.{Array => _, _}
import java.util.Properties

import com.twitter.chill.{java => _ , _}

/*
* 
* Spark for Vertica/MapR-FS/MapR-Streams Loading
* 
* */
object SparkMapRStreams {

  final val checkpointDirectory = "maprfs:///opt/checkpoint/Processor"

  def main(args: Array[String]) = {
    if(args.length != 5){
      printf("parameter => <stream-path> <topic-regexp-name> <output stream-path:topic or verticahost:port> <stream-batch-interval-sec> <kafka-poll-time-msec>\n")
      sys.exit(-1)
    }

    val ssc = StreamingContext.getOrCreate(checkpointDirectory, () => functionToCreateContext(args))
    //--------------------------------
    // start..end
    //--------------------------------
    printf("start to read stream\n")
    ssc.start()
    printf("wait termination\n")
    ssc.awaitTermination()
    printf("stop read stream\n")
    //ssc.stop(stopSparkContext = true, stopGracefully = true)

  }

  /*
  * The process is same as Flink code.
  * Thus, output is also same.
  *
  * */
  def functionToCreateContext(args: Array[String]): StreamingContext = {
    val Array(streamPath,topicRegexpName,output,streamBatchIntervalSec_,kafkaPollTimeoutMsec) = args
    val streamBatchIntervalSec = streamBatchIntervalSec_.toInt
    val groupId = "spark-maprstreams-vertica-fixed-id"
    val offsetReset = "earliest"
    val brokers = "localhost:9999"

    printf("start Kafka configuration => groupId[%s] offsetReset[%s] kafkaPollTimeoutMsec[%s] brokers[%s]\n",
      groupId, offsetReset, kafkaPollTimeoutMsec, brokers)


    val kafkaParams = Map[String, String](
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG ->
        "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG ->
        "org.apache.kafka.common.serialization.ByteArrayDeserializer",
    //  ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> offsetReset,
    //  ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "true",
      "fetch.min.bytes" -> "20"
    )


    val producerConfMap = Map[String, String](
     ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> 
        "org.apache.kafka.common.serialization.StringDeserializer",
     ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG ->
        "maprstreams_vertica.PayloadSerializer"
     )
    val kafkaBrokers = "a,b,c"
    val producerConf = new ProducerConf(bootstrapServers = kafkaBrokers.split(",").toList)
    producerConf.withKeySerializer("org.apache.kafka.common.serialization.StringSerializer")
    producerConf.withValueSerializer("maprstreams_vertica.PayloadSerializer")

    //--------------------------------
    // Spark Conf
    //--------------------------------
    val sparkConf = new SparkConf()
      .setAppName(SparkMapRStreams.getClass.getName)
      .set("spark.streaming.kafka.consumer.poll.ms", kafkaPollTimeoutMsec)
    val ssc = new StreamingContext(sparkConf, Seconds(streamBatchIntervalSec))

    // BUG? it does not work.
      ssc.checkpoint(checkpointDirectory)


    val timeOffset = System.currentTimeMillis()
    val bcastTimeOffset = ssc.sparkContext.broadcast(timeOffset)

    //printf("timeOffset = %d\n",timeOffset)
    val topiccStr = streamPath + ":" + topicRegexpName
    val topicc = java.util.regex.Pattern.compile(topiccStr)
    printf("start Spark configuration => topic[%s] streamBatchIntervalSec[%d]\n",
      topiccStr, streamBatchIntervalSec)
    val consumerStrategy = ConsumerStrategies.SubscribePattern[String, Array[Byte]](
      topicc, kafkaParams)


    //Create Direct Streams
    val messagesDStream =  KafkaUtils.createDirectStream[String, Array[Byte]](
      ssc, LocationStrategies.PreferConsistent, consumerStrategy)

    val valuesDStream = messagesDStream.map{ msg =>
      ( msg.timestamp(),msg.value())
    }.repartition(100)

  } 

}
