package de.rewe.hbase.dao.mapper.datatypes.standard;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import de.rewe.hbase.dao.mapper.datatypes.DatatypeMapper;
import de.rewe.hbase.dao.mapper.datatypes.DatatypeMapperProvider;

/**
 * Implementation of default Datatype Mappers
 */
public class StandardDatatypeMapperProvider implements DatatypeMapperProvider {

	@Override
	public Map<Class<?>, DatatypeMapper> getDatatypeMappers() {
		Map<Class<?>, DatatypeMapper> datatypeMappers = new HashMap<>();
		datatypeMappers.put(Boolean.class, new BooleanDatatypeMapper());
		datatypeMappers.put(BigDecimal.class, new BigDecimalDatatypeMapper());
		datatypeMappers.put(Long.class, new LongDatatypeMapper());
		datatypeMappers.put(Double.class, new DoubleDatatypeMapper());
		datatypeMappers.put(Float.class, new FloatDatatypeMapper());
		datatypeMappers.put(Integer.class, new IntegerDatatypeMapper());
		datatypeMappers.put(Short.class, new ShortDatatypeMapper());
		datatypeMappers.put(String.class, new StringDatatypeMapper());
		return datatypeMappers;
	}

}
