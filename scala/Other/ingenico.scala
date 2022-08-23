import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import org.apache.kafka.common.utils.Bytes

import java.util.concurrent.ConcurrentHashMap
import java.util.{Collections, Properties, UUID}
import scala.collection.JavaConversions.iterableAsScalaIterable
import scala.collection.JavaConverters.{asScalaIteratorConverter, mapAsScalaMapConverter}

0 until 100 foreach { i =>

    val catalogStream = "/user/mapr/ingenico"
    val pollTimeout = 1000
    val topic = "topic0"
    val bootstrapServer = "local:1234"

    // create consumer configs
    val properties = new Properties
    val groupId = UUID.randomUUID.toString
    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[ByteArrayDeserializer].getName)
    properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100")

    val bytesStringKafkaConsumer = new KafkaConsumer[Bytes, String](properties)

    bytesStringKafkaConsumer.subscribe(Collections.singletonList(s"$catalogStream:$topic"))
    val newCatalog = new ConcurrentHashMap[Long, Bytes]

    val topicPartition = new TopicPartition(s"$catalogStream:$topic", 0)
    // println(s"iteration: $i, Position: ${bytesStringKafkaConsumer.position(topicPartition)}")

    val endOffsets = bytesStringKafkaConsumer.endOffsets(Collections.singletonList(topicPartition))
    val endOffset = endOffsets.asScala(topicPartition)

    while (newCatalog.size < endOffset) {

        val records: ConsumerRecords[Bytes, String] = bytesStringKafkaConsumer.poll(pollTimeout)
        val list = records.toList


        list.foreach(r => newCatalog.put(r.offset(), r.key()))
        println(s"iteration: $i, Records get, records: ${list.map(r => s"key: ${r.key()} offset: ${r.offset()}").mkString("\n")}")

        println(s"iteration: $i, end offset: $endOffsets")

    }

    if (newCatalog.size != endOffset) throw new RuntimeException("wrong size!!!!! AAAARGH")
    bytesStringKafkaConsumer.close()

}