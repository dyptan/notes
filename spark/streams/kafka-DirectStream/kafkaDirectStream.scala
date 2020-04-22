val groupId = "1";
val pollMs = "1000";
val topic1 = "topic1";
val topic2 = "topic2";
val kafkaServers = "localhost:9092";

val kafkaParams = new HashMap[String, Object];
kafkaParams.put("bootstrap.servers", kafkaServers);
kafkaParams.put("auto.offset.reset", "latest");
kafkaParams.put("enable.auto.commit", "false");
kafkaParams.put("group.id",groupId);
kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

import org.apache.spark._
import org.apache.spark.streaming._

val ssc = new StreamingContext(sc, Seconds(1))

val listTopics = new ArrayList[String]();
listTopics.add(topic1);
listTopics.add(topic2);

import org.apache.spark.streaming.kafka09._

// import org.apache.spark.streaming.kafka09.CanCommitOffsets;
// import org.apache.spark.streaming.kafka09.ConsumerStrategies;
// import org.apache.spark.streaming.kafka09.HasOffsetRanges;
// import org.apache.spark.streaming.kafka09.KafkaUtils;
// import org.apache.spark.streaming.kafka09.LocationStrategies;
// import org.apache.spark.streaming.kafka09.OffsetRange;

val stream = KafkaUtils.createDirectStream[String, String](sc, listTopics, kafkaParams);