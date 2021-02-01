package de.rewe.hbase.dao.mapper.datatypes.standard;

import org.apache.hadoop.hbase.util.Bytes;

import de.rewe.hbase.dao.mapper.datatypes.AbstractSimpleDatatypeMapper;

/**
 * Datatype mapping instance to convert {@link Double} into a <code>byte[]</code> and vice versa
 */
public class DoubleDatatypeMapper extends AbstractSimpleDatatypeMapper<Double> {

	@Override
	protected byte[] toBytesInternal(Double datatype) {
		return Bytes.toBytes(datatype);
	}

	@Override
	protected Double toDatatypeInternal(byte[] bytes) {
		return Bytes.toDouble(bytes);
	}

}
