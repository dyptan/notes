package de.rewe.hbase.util;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.ClientProtos;
import org.apache.hadoop.hbase.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utils class
 * 
 * @author a10366a
 * 
 */
public class RsmHBaseUtils {
	private static final Logger log = LoggerFactory.getLogger(RsmHBaseUtils.class);

	// search class path for this file
	public static final String DEFAULT_CONFIG_FILE_NAME = "rsm-hbase.properties";

	/**
	 * 
	 * @author a10366a
	 * 
	 */
	public static enum PropertyType {
		INTEGER,
		BOOLEAN,
		STRING
	}

	/**
	 * 
	 * @author a10366a
	 * 
	 */
	public static enum Configs {

		HBASE_ZOOKEEPER_HOST("HBASE_ZOOKEEPER_HOST", "", PropertyType.STRING),
		HBASE_ZOOKEEPER_PORT("HBASE_ZOOKEEPER_PORT", "2181", PropertyType.STRING);

		private String name;
		private String defaultValue;
		private PropertyType propertyType;

		/**
		 * 
		 * @param name
		 * @param defaultValue
		 * @param propertyType
		 */
		private Configs(final String name, final String defaultValue, final PropertyType propertyType) {
			this.name = name;
			this.defaultValue = defaultValue;
			this.propertyType = propertyType;
		}

		public String getName() {
			return name;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public PropertyType getPropertyType() {
			return propertyType;
		}

	}

	/**
	 * Data Types used for hbase
	 * 
	 * @author A10366A
	 * 
	 */
	public static enum HTypes {
		BYTE,
		SHORT,
		INT,
		LONG,
		STRING,
		SEP
	}

	/**
	 * 
	 * @param zookeeperHost
	 * @param zookeeperPort
	 * @param user
	 */
	public static void setConnectionProperties(final String zookeeperHost, final String zookeeperPort, final String user) {
		setUser(user);
		setConnectionProperties(zookeeperHost, zookeeperPort);
	}

	/**
	 * 
	 * @param zookeeperHost
	 * @param zookeeperPort
	 */
	public static void setConnectionProperties(final String zookeeperHost, final String zookeeperPort) {
		setSystemProperty(RsmHBaseUtils.Configs.HBASE_ZOOKEEPER_HOST.getName(), zookeeperHost);
		setSystemProperty(RsmHBaseUtils.Configs.HBASE_ZOOKEEPER_PORT.getName(), zookeeperPort);
	}

	/**
	 * 
	 * @param user
	 */
	public static void setUser(final String user) {
		System.setProperty("HADOOP_USER_NAME", user);
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	private static void setSystemProperty(final String name, final String value) {
		System.setProperty(name, value);
		log.info("Setting property {}={}", name, value);
	}

	/**
	 * convert a scan object to a string
	 *
	 * @param scan
	 *            Scan-Objekt
	 * @return String
	 */
	public static String convertScanToString(final Scan scan) {
		try {
			ClientProtos.Scan proto = ProtobufUtil.toScan(scan);
			return Base64.encodeBytes(proto.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
