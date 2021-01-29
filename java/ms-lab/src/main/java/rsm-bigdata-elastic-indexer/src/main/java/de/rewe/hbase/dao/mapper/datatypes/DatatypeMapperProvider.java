package de.rewe.hbase.dao.mapper.datatypes;

import java.util.Map;

/**
 * Service interface to provide a serialization for a given plugin
 */
public interface DatatypeMapperProvider {

	/**
	 * Returns the map of datatype-mappers for given classes
	 * 
	 * @return
	 */
	Map<Class<?>, DatatypeMapper> getDatatypeMappers();

}
