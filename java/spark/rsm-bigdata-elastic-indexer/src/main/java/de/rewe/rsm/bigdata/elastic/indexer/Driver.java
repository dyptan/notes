package de.rewe.rsm.bigdata.elastic.indexer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.TaskContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.CanCommitOffsets;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.ConsumerStrategy;
import org.apache.spark.streaming.kafka010.HasOffsetRanges;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.kafka010.OffsetRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rewe.rsm.bigdata.elastic.indexer.config.Configs;
import de.rewe.rsm.bigdata.elastic.indexer.config.IndexerAppConf;
import de.rewe.rsm.bigdata.spark.streaming.shutdown.GracefulShutdownChecker;

public class Driver {

	private static final String APP_CONFIG_PREFIX = "rsm.bigdata.elastic.indexer.";
	private static final String KAFKA_GROUP_ID = "rsm.bigdata.elastic.indexer";

	private static final Logger LOG = LoggerFactory.getLogger(Driver.class);

	public static void main(String[] args) {
		LOG.info("Starting driver...");

		final IndexerAppConf appConf = IndexerAppConf.getInstance();
		final SparkConf sparkConf = appConf.getSparkConfig(APP_CONFIG_PREFIX);
		final String checkpointDirectory = appConf.getProperty(Configs.STREAMING_CHECKPOINT_DIRECTORY);

		final SparkSession sparkSession = SparkSession.builder().config(sparkConf).getOrCreate();
		try (final JavaStreamingContext context = JavaStreamingContext.getOrCreate(checkpointDirectory, () -> initStream(sparkSession, appConf))) {
			final String shutdownTriggerDirectory = appConf.getProperty(Configs.STREAMING_SHUTDOWN_TRIGGER_DIRECTORY);
			final String shutdownAppId = appConf.getProperty(Configs.STREAMING_SHUTDOWN_TRIGGER_FILE_PREFIX);
			final long shutdownCheckTimeout = Long.parseLong(appConf.getProperty(Configs.STREAMING_SHUTDOWN_CHECK_TIME));
			final GracefulShutdownChecker shutdownChecker = new GracefulShutdownChecker(context, shutdownTriggerDirectory, shutdownAppId);

			context.start();

			shutdownChecker.awaitTermination(shutdownCheckTimeout);
		}
	}

	private static JavaStreamingContext initStream(final SparkSession sparkSession, final IndexerAppConf appConfig) throws Exception {
		final Long duration = Long.valueOf(appConfig.getProperty(Configs.SPARK_BATCH_DURATION));
		final JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());
		final JavaStreamingContext context = new JavaStreamingContext(sparkContext, Durations.seconds(duration));

		// create Kafka consumer configuration
		final Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
		kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, KAFKA_GROUP_ID);
		kafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		kafkaParams.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		kafkaParams.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, appConfig.getIntProperty(Configs.STREAMING_MAX_RECORD_SIZE));

		// create Kafka consumer strategy
		List<String> topics = Arrays.asList("mytopic", "test");
		/*List<String> topics = Arrays.asList("/app/lois/dev3/private/mapres/sparkbug/stream1:t", "/app/lois/dev3/private/mapres/sparkbug/stream2:t",
				"/app/lois/dev3/private/mapres/sparkbug/stream3:t", "/app/lois/dev3/private/mapres/sparkbug/stream4:t",
				"/app/lois/dev3/private/mapres/sparkbug/stream5:t", "/app/lois/dev3/private/mapres/sparkbug/stream6:t");*/
		Map<TopicPartition, Long> offsets = new HashMap<>();
		for (int i = 0; i < topics.size(); i++) {
			offsets.put(new TopicPartition(topics.get(i), 0), 0L);
			offsets.put(new TopicPartition(topics.get(i), 1), 0L);
		}
		final ConsumerStrategy<Byte[], String> consumerStrategy = ConsumerStrategies.Subscribe(topics, kafkaParams, offsets);
		LOG.info("Successfully created ConsumerStrategy: {}", consumerStrategy);

		// consume events
		final JavaInputDStream<ConsumerRecord<Byte[], String>> inputStream = KafkaUtils.createDirectStream(context, LocationStrategies.PreferConsistent(),
				consumerStrategy);
		LOG.debug("Initialized input Stream");
		CanCommitOffsets offsetsCommitter = (CanCommitOffsets) inputStream.inputDStream();
		inputStream.foreachRDD(rdd -> {
			final OffsetRange[] offsetRanges = ((HasOffsetRanges) rdd.rdd()).offsetRanges();

			LOG.debug("Starting RDD with following OffsetRanges: {}", (Object[]) offsetRanges);

			rdd.map(r -> new RecordData(r.topic(), r.partition(), r.offset(), r.value())).foreachPartition(partition -> {
				if (partition.hasNext()) {
					LOG.debug("Initialization for partition");
					final OffsetRange o = offsetRanges[TaskContext.get().partitionId()];

					while (partition.hasNext()) {
						final RecordData recordData = partition.next();
						final String topic = recordData.getTopic();
						final String record = recordData.getData();
						LOG.info("Processing record from topic '{}', partition {} and offset {}. Payload: {}", topic, recordData.getPartition(),
								recordData.getOffset(), record);
					}
				}
			});

			// offsetsCommitter.commitAsync(offsetRanges, (map, e) -> {
			// if (e != null) {
			// LOG.warn(e.getMessage());
			// }
			// if (map != null) {
			// map.forEach((key, value) -> LOG.info("Offset commited: {}, {} -> {}", key.topic(), key.partition(), value.offset()));
			// }
			// });
		});

		return context;
	}

}
