package de.rewe.hbase.dao.mapper.datatypes.standard;

import org.apache.hadoop.hbase.util.Bytes;

import de.rewe.hbase.dao.mapper.datatypes.AbstractSimpleDatatypeMapper;

/**
 * Datatype mapping instance to convert {@link Long} into a <code>byte[]</code> and vice versa
 */
public class LongDatatypeMapper extends AbstractSimpleDatatypeMapper<Long> {

	@Override
	protected byte[] toBytesInternal(Long datatype) {
		return Bytes.toBytes(datatype);
	}

	@Override
	protected Long toDatatypeInternal(byte[] bytes) {
		return Bytes.toLong(bytes);
	}

}
