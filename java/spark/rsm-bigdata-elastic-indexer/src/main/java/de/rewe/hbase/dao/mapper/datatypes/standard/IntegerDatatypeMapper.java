package de.rewe.hbase.dao.mapper.datatypes.standard;

import org.apache.hadoop.hbase.util.Bytes;

import de.rewe.hbase.dao.mapper.datatypes.AbstractSimpleDatatypeMapper;

/**
 * Datatype mapping instance to convert {@link Integer} into a <code>byte[]</code> and vice versa
 */
public class IntegerDatatypeMapper extends AbstractSimpleDatatypeMapper<Integer> {

	@Override
	protected byte[] toBytesInternal(Integer datatype) {
		return Bytes.toBytes(datatype);
	}

	@Override
	protected Integer toDatatypeInternal(byte[] bytes) {
		return Bytes.toInt(bytes);
	}

}
