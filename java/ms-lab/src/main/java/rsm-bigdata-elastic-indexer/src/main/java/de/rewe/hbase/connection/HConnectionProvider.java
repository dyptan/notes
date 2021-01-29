package de.rewe.hbase.connection;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple
 *
 * @author REWE Systems GmbH, 2015
 */
public class HConnectionProvider {
	static final Logger LOG = LoggerFactory.getLogger(HConnectionProvider.class);

	public static final String userGID = "rsm.mapr.user.gid";
	public static final String userUID = "rsm.mapr.user.uid";
	public static final String userName = "rsm.mapr.user.name";
	/**
	 * singleton
	 */
	private static final HConnectionProvider INSTANCE = new HConnectionProvider();

	private transient volatile Connection connection;
	private transient volatile Configuration config;

	/**
	 * @return the singleton instance
	 */
	public static HConnectionProvider getInstance() {
		return INSTANCE;
	}

	/**
	 * shutdown hook
	 */
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				LOG.info("Running HBase Shutdown Hook.");
				getInstance().closeConnection();
			}
		});
	}

	/**
	 * Reinitialize the HConnectionProvider with an instance of HBaseTestingUtility to work with a minicluster.
	 *
	 * @param tables
	 */
	public static synchronized void reinit(final Connection connection) {
		if (INSTANCE.connection != connection) {
			LOG.info("\n\t----> HConnectionProvider reinit");
			INSTANCE.closeConnection();
			INSTANCE.connection = connection;
			// in this case we set also a new configuration object
			INSTANCE.config = connection.getConfiguration();
		} else {
			LOG.debug("HConnectionProvider has already been reinitialized with this connection");
		}
	}

	/**
	 * Closes the connection.
	 */
	public void closeConnection() {
		if (connection == null || connection.isClosed()) {
			LOG.info("HBase connection has already been closed");
		} else {
			LOG.info("Closing HBase connection to {}", connection.getConfiguration().get(HConstants.ZOOKEEPER_QUORUM));
			try {
				connection.close();
			} catch (IOException e) {
				throw new RuntimeException("close connection error", e);
			}
		}
	}

	/**
	 * Returns the Table interface.
	 *
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	public Table getTable(final TableName tableName) throws IOException {
		return getConnection().getTable(tableName);
	}

	/**
	 * Configuration.
	 *
	 * @return
	 */
	public Configuration getConfiguration() {
		if (config == null) {
			synchronized (HConnectionProvider.class) {
				if (config == null) {
					config = createConfig();
				}
			}
		}
		return config;
	}

	public Configuration getConfigurationCloned() {
		Configuration config = getConfiguration();
		Configuration clonedConfig = new Configuration(config);
		return clonedConfig;
	}

	public Connection getConnection() {
		if (connection == null) {
			synchronized (HConnectionProvider.class) {
				if (connection == null) {
					connection = createConnection();
				}
			}
		}
		if (connection.isAborted() || connection.isClosed()) {
			synchronized (HConnectionProvider.class) {
				if (connection.isAborted() || connection.isClosed()) {
					connection = createConnection();
				}
			}
		}
		return connection;
	}

	/**
	 * Initializes a connection according to the config.
	 *
	 * @return
	 */
	private static Connection createConnection() {
		LOG.info("\n\t----> HConnectionProvider init");
		Configuration config = getInstance().getConfiguration();
		try {
			return ConnectionFactory.createConnection(config);
		} catch (IOException e) {
			throw new RuntimeException("Fehler bei Erzeugung einer Verbindung mit HBase: " + config.get(HConstants.ZOOKEEPER_QUORUM) + ":"
					+ config.get(HConstants.ZOOKEEPER_CLIENT_PORT), e);
		}

	}

	private static Configuration createConfig() {
		Configuration config = HBaseConfiguration.create();
		String gid = System.getProperty(userGID);
		String uid = System.getProperty(userUID);
		String username = System.getProperty(userName);
		/**
		 * Default in unsecured ist root zum test zwecken mit root/mapr TODO testen
		 */
		config.set("hadoop.spoofed.user.uid", (uid == null) ? "0" : uid);
		config.set("hadoop.spoofed.user.gid", (gid == null) ? "0" : gid);
		config.set("hadoop.spoofed.user.username", (username == null) ? "root" : gid);
		// config.set("hadoop.spoofed.user.uid", "168834666");
		return config;
	}

	private HConnectionProvider() {
	}

}