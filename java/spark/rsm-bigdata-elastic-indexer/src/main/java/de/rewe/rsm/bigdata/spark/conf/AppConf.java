package de.rewe.rsm.bigdata.spark.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Application-wide configuration of properties.
 *
 * @author REWE Systems GmbH, 2015
 *
 */
public class AppConf extends Configured implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(AppConf.class);

	private final Properties props;

	/**
	 * Use a custom config file.
	 *
	 * @param path2Config
	 *            the resource name
	 */
	public AppConf(final String path2Config) {
		this(path2Config, null);
	}

	/**
	 * Create an AppConf object from properties. This constructor is mostly for testing purposes.
	 * 
	 * @param props
	 *            the properties to use
	 */
	public AppConf(final Properties props) {
		this.props = props;
	}

	public <T extends Enum<?> & ConfigProperty> AppConf(final String path2Config, final Class<T> configEnumClazz) {
		this.props = getProperties(path2Config, configEnumClazz);
	}

	/**
	 * Lazily initializes the default configuration {@code config.properties }
	 */
	public static AppConf getDefault() {
		return Holder.DEFAULT_APP_CONF;
	}

	private static class Holder {
		private static final AppConf DEFAULT_APP_CONF = new AppConf("/config.properties");
	}

	private static <T extends Enum<?> & ConfigProperty> Properties getProperties(final String path2Config, final Class<T> configEnumClazz) {
		Properties properties = new Properties();

		try (InputStream stream = getConfigInputStream(path2Config)) {
			properties.load(stream);
			log.info("App configuration {} loaded", path2Config);
		} catch (IOException ex) {
			throw new RuntimeException("Property file for application configuration can't be read from path = " + path2Config);
		}

		if (configEnumClazz != null) {
			for (ConfigProperty property : configEnumClazz.getEnumConstants()) {
				if (properties.getProperty(property.getName()) == null && property.getDefaultValue() != null) {
					properties.setProperty(property.getName(), property.getDefaultValue());
				}
			}
		}

		return properties;
	}

	private static InputStream getConfigInputStream(final String path2Config) throws IOException {
		boolean useHdfs = false;
		boolean useFile = false;
		try {
			String scheme = new URI(path2Config).getScheme();
			if ("hdfs".equalsIgnoreCase(scheme)) {
				useHdfs = true;
			} else if ("file".equalsIgnoreCase(scheme)) {
				useFile = true;
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException("Error while parsing configuration path '" + path2Config + "'", e);
		}

		if (useHdfs) {
			Configuration hadoopConfiguration = new Configuration();
			Path pathFS = new Path(path2Config);
			FileSystem fileSystem = pathFS.getFileSystem(hadoopConfiguration);
			return fileSystem.open(pathFS);
		} else if (useFile) {
			return new FileInputStream(new File(path2Config.substring("file://".length())));
		}

		return AppConf.class.getResourceAsStream(path2Config);
	}

	public String getProperty(final String key) {
		return Preconditions.checkNotNull(props.getProperty(key), "Missing property: " + key);
	}

	public Optional<String> getOptionalProperty(final String key) {
		String value = props.getProperty(key);
		return Optional.ofNullable(value);
	}

	public int getIntProperty(final String key) {
		String val = getProperty(key);
		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(String.format("Property %s is not parseable as int: %s", key, val), e);
		}
	}

	public List<String> getListProperty(final String key) {
		return Arrays.asList(getProperty(key).split(","));
	}

	public Set<Integer> getIntSetProperty(final String key) {
		String value = getProperty(key);
		String[] csv = value.split(",");
		Set<Integer> mbbsSet = Arrays.stream(csv).map(String::trim).map(Integer::parseInt).collect(Collectors.toSet());
		return mbbsSet;
	}

	public Optional<String> getOptionalProperty(final ConfigProperty key) {
		return this.getOptionalProperty(key.getName());
	}

	/**
	 * Parses all {@code spark.*} properties from the config file and sets them in the returned {@link SparkConf} object.
	 */
	public SparkConf getSparkConfig() {
		return this.getSparkConfig(null);
	}

	/**
	 * Parses all {@code spark.*} properties from the config file and sets them in the returned {@link SparkConf} object.
	 *
	 * @param appPrefix
	 *            App prefix to be cut off the property name
	 */
	public SparkConf getSparkConfig(final String appPrefix) {
		SparkConf sparkConf = new SparkConf();
		for (String key : props.stringPropertyNames()) {
			String sparkKey = key;
			if (appPrefix != null && sparkKey.startsWith(appPrefix)) {
				sparkKey = sparkKey.substring(appPrefix.length());
			}

			if (sparkKey.startsWith("spark.")) {
				String value = props.getProperty(key);
				sparkConf.set(sparkKey, value);
			}
		}
		return sparkConf;
	}

}