package de.rewe.rsm.bigdata.elastic.indexer.config;

import de.rewe.rsm.bigdata.spark.conf.ConfigProperty;

/**
 * Enum of configuration, includes name, default value and type.
 */
public enum Configs implements ConfigProperty {

	// Spark job properties
	SPARK_DEFAULT_FILESYSTEM("spark.default.filesystem", "nameservice1:8020"),
	SPARK_BATCH_DURATION("batch.interval", "1"),

	STREAMING_CHECKPOINT_DIRECTORY("rsm.bigdata.elastic.indexer.checkpoint.directory", null),
	STREAMING_OFFSET_TABLE("rsm.bigdata.elastic.indexer.offset.table", null),
	STREAMING_MAX_RECORD_SIZE("rsm.bigdata.elastic.indexer.max.record.size", "5242880"),

	STREAMING_SHUTDOWN_TRIGGER_DIRECTORY("rsm.bigdata.elastic.indexer.shutdown.trigger.directory", null),
	STREAMING_SHUTDOWN_TRIGGER_FILE_PREFIX("rsm.bigdata.elastic.indexer.shutdown.trigger.file.prefix", "init"),
	STREAMING_SHUTDOWN_CHECK_TIME("rsm.bigdata.elastic.indexer.shutdown.check.time", "60000"),

	INDEXER_TABLE_PATH("rsm.bigdata.elastic.indexer.table.path", null),
	INDEXER_ERROR_RECORDS_TABLE("rsm.bigdata.elastic.indexer.errorrecords.table", null),
	INDEXER_BULK_REQUEST_SIZE("rsm.bigdata.elastic.indexer.bulk.request.size", "10"),

	ELASTICSEARCH_URL("rsm.bigdata.elastic.indexer.elasticsearch.url", null),
	ELASTICSEARCH_PREFIX("rsm.bigdata.elastic.indexer.elasticsearch.index.prefix", null),
	ELASTICSEARCH_USER("rsm.bigdata.elastic.indexer.elasticsearch.user", null),
	ELASTICSEARCH_PASSWORD("rsm.bigdata.elastic.indexer.elasticsearch.password", null),
	ELASTICSEARCH_ENABLE_CONNECTION_POOLING("rsm.bigdata.elastic.indexer.elasticsearch.enableConnectionPooling", "false"),
	ELASTICSEARCH_MAX_CONNECTIONS_PER_HOST("rsm.bigdata.elastic.indexer.elasticsearch.maxConnectionsPerHost", "5"),
	ELASTICSEARCH_MAX_TOTAL_CONNECTIONS("rsm.bigdata.elastic.indexer.elasticsearch.maxTotalConnections", "10"),

	TOPIC_INDEX_MAPPER_CLASS("rsm.bigdata.topic.index.mapper.class", null),
	TOPIC_INDEX_MAPPER_IGNORE_RECORDS_WITHOUT_MAPPING("rsm.bigdata.topic.index.mapper.ignore.records.without.mapping", "false"),
	INDEX_ATTRIBUTE_HOLDER_CLASS("rsm.bigdata.index.attribute.holder.class", "de.rewe.rsm.bigdata.elastic.indexer.elastic.SimpleIndexAttributeHolder");

	private String name;

	private String defaultValue;

	/**
	 *
	 * @param name
	 * @param defaultValue
	 */
	private Configs(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @return the defaultValue
	 */
	@Override
	public String getDefaultValue() {
		return this.defaultValue;
	}

}