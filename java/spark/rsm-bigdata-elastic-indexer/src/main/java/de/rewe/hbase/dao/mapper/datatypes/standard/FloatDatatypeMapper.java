package de.rewe.hbase.dao.mapper.datatypes.standard;

import org.apache.hadoop.hbase.util.Bytes;

import de.rewe.hbase.dao.mapper.datatypes.AbstractSimpleDatatypeMapper;

/**
 * Datatype mapping instance to convert {@link Float} into a <code>byte[]</code> and vice versa
 */
public class FloatDatatypeMapper extends AbstractSimpleDatatypeMapper<Float> {

	@Override
	protected byte[] toBytesInternal(Float datatype) {
		return Bytes.toBytes(datatype);
	}

	@Override
	protected Float toDatatypeInternal(byte[] bytes) {
		return Bytes.toFloat(bytes);
	}

}
