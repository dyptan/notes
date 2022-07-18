package de.rewe.hbase.dao.mapper.datatypes;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import de.rewe.hbase.dao.mapper.BusinesObjectMappingException;

/**
 * Repository holding all strategies to transform an object into a byte[] and vice versa.
 */
public class DatatypeMapperRepository {

	private enum Holder {
		INSTANCE;

		private final DatatypeMapperRepository factory = new DatatypeMapperRepository();
	}

	private Map<Class<?>, DatatypeMapper> datatypeMappers = new HashMap<>();

	private DatatypeMapperRepository() {
		ServiceLoader<DatatypeMapperProvider> services = ServiceLoader.load(DatatypeMapperProvider.class, this.getClass().getClassLoader());
		for (DatatypeMapperProvider datatypeMapperProvider : services) {
			this.datatypeMappers.putAll(datatypeMapperProvider.getDatatypeMappers());
		}
	}

	/**
	 * Returns the {@link DatatypeMapper} for the given class.
	 * 
	 * @param clazz
	 *            The class to be translated
	 * @return the {@link DatatypeMapper} to translate a obect of the given class into byte arrays or vice versa
	 */
	public <T> DatatypeMapper getDatatypeMapper(Class<T> clazz) {
		DatatypeMapper datatypeMapper = this.datatypeMappers.get(clazz);
		if (datatypeMapper == null) {
			throw new BusinesObjectMappingException("No datatype mapper defined for " + clazz.getName());
		}
		return datatypeMapper;
	}

	/**
	 * Returns the singleton instance of this repository
	 */
	public static DatatypeMapperRepository getInstance() {
		return Holder.INSTANCE.factory;
	}

}
