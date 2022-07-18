package de.rewe.rsm.bigdata.elastic.indexer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rewe.rsm.bigdata.spark.conf.AppConf;

public class IndexerAppConf extends AppConf {

	private static final Logger LOG = LoggerFactory.getLogger(IndexerAppConf.class);

	private static final long serialVersionUID = 1L;

	private static final String CONFIG_FILE_PROPERTY = "rsm.bigdata.elastic.indexer.config";

	private static class InstanceHolder {
		private static final IndexerAppConf INSTANCE = new IndexerAppConf();
	}

	private IndexerAppConf() {
		super(extractConfigFile(), Configs.class);
	}

	public static IndexerAppConf getInstance() {
		return InstanceHolder.INSTANCE;
	}

	private static String extractConfigFile() {
		String configFile = System.getProperty(CONFIG_FILE_PROPERTY);
		if (configFile == null) {
			String defaultConfigFile = "file://elastic-indexer-configuration.properties";
			LOG.info("No configuration set. Using default definition: " + defaultConfigFile);
			return defaultConfigFile;
		}
		LOG.info("Configuration file: " + configFile);
		return configFile;
	}

	public String getProperty(Configs key) {
		return this.getProperty(key.getName());
	}

	public int getIntProperty(Configs key) {
		return this.getIntProperty(key.getName());
	}

}