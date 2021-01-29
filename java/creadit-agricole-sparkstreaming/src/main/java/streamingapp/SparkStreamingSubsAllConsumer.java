package streamingapp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.tools.VerifiableConsumer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka09.CanCommitOffsets;
import org.apache.spark.streaming.kafka09.ConsumerStrategies;
import org.apache.spark.streaming.kafka09.HasOffsetRanges;
import org.apache.spark.streaming.kafka09.KafkaUtils;
import org.apache.spark.streaming.kafka09.LocationStrategies;
import org.apache.spark.streaming.kafka09.OffsetRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparkStreamingSubsAllConsumer {
	private static final Logger LOG = LoggerFactory.getLogger(SparkStreamingSubsAllConsumer.class);



	public static void main(String[] args) throws InterruptedException {

		String groupId = "1";
		String pollMs = "100";
		String topic1 = "mytopic";
		String topic2 = "test";
		
		String kafkaServers = "node7:9092";
		if (args.length >= 5) {
			kafkaServers = args[4];
		} else {
			System.out.println("No kafka servers provided. Using mapr-es");
		}
		
		// Printing arguments for control
		System.out.println("Group ID : "+groupId);
		System.out.println("Poll MS : "+pollMs);
		System.out.println("Topic1 : "+topic1);
		System.out.println("Topic2 : "+topic2);
		System.out.println("Kafka Servers : "+kafkaServers);
		
		
		// Setting KAFKA PARAMETERS		
		Map<String, Object> kafkaParams = new HashMap<String, Object>();
		kafkaParams.put("bootstrap.servers", kafkaServers);
		kafkaParams.put("auto.offset.reset", "earliest");
		kafkaParams.put("enable.auto.commit", "false");
		kafkaParams.put("group.id",groupId);
		kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		
		// Setting SPARK STREAMING PARAMETERS		
		SparkConf config = new SparkConf().setAppName("credit-agricol-sparkstreaming").setMaster("local[*]")
				.set("spark.streaming.kafka.maxRatePerPartition", "100")
				.set("spark.streaming.kafka.consumer.driver.poll.ms", pollMs);
		
		JavaSparkContext sc = new JavaSparkContext(config);
		sc.setLogLevel("ERROR");
		JavaStreamingContext sparkContext = new JavaStreamingContext(sc, new Duration(10000));
		
		List<String> listTopics = new ArrayList<>();
		listTopics.add(topic1);
		if (topic2 != null && !"".equals(topic2)) {
			listTopics.add(topic2);
		}
		
		final JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(
			sparkContext,
			LocationStrategies.PreferConsistent(),
			ConsumerStrategies.<String, String>Subscribe(listTopics, kafkaParams)
		);


		stream.foreachRDD(rdd -> {
			
			// Getting offsetRange (for later commit)
			OffsetRange[] offsetRanges = ((HasOffsetRanges) rdd.rdd()).offsetRanges();
			
			rdd.foreachPartition(consumerRecords -> {
				// Printing each messages
				while (consumerRecords.hasNext()) {
					System.out.println("Printing message : "+consumerRecords.next().value());
//					ConsumerRecord consumerRecord = consumerRecords.next();
//					System.out.printf("Processing record from topic %s, partition %s and offset %s. Payload: %s \n", consumerRecord.topic(), consumerRecord.partition(),
//							consumerRecord.offset(), consumerRecord.value());
				}
			});

			// Committing offsetRange
//			OffsetCommitCallback cb = new OffsetCommitCallback() {
//				@Override
//				public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
//					System.out.printf("\nCallback meta: %s", map.values());
//				}
//			};
			((CanCommitOffsets) stream.inputDStream()).commitAsync(offsetRanges, null);

		});
			
		sparkContext.start();
		sparkContext.awaitTermination();
		return;
    }
}
