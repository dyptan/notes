package de.rewe.rsm.bigdata.spark.conf;

/**
 * Interface for configuration enums, used by {@link AppConf}
 */
public interface ConfigProperty {

	/**
	 * @return The name of the config property
	 */
	String getName();

	/**
	 * @return The default value of the property, may be null
	 */
	String getDefaultValue();

}
