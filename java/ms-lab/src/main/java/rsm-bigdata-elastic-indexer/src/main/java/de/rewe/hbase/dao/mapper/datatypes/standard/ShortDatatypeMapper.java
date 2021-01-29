package de.rewe.hbase.dao.mapper.datatypes.standard;

import org.apache.hadoop.hbase.util.Bytes;

import de.rewe.hbase.dao.mapper.datatypes.AbstractSimpleDatatypeMapper;

/**
 * Datatype mapping instance to convert {@link Short} into a <code>byte[]</code> and vice versa
 */
public class ShortDatatypeMapper extends AbstractSimpleDatatypeMapper<Short> {

	@Override
	protected byte[] toBytesInternal(Short datatype) {
		return Bytes.toBytes(datatype);
	}

	@Override
	protected Short toDatatypeInternal(byte[] bytes) {
		return Bytes.toShort(bytes);
	}

}
