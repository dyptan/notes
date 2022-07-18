package de.rewe.hbase.dao.mapper.datatypes.standard;

import org.apache.hadoop.hbase.util.Bytes;

import de.rewe.hbase.dao.mapper.datatypes.AbstractSimpleDatatypeMapper;

/**
 * Datatype mapping instance to convert {@link String} into a <code>byte[]</code> and vice versa
 */
public class StringDatatypeMapper extends AbstractSimpleDatatypeMapper<String> {

	@Override
	protected byte[] toBytesInternal(String datatype) {
		return Bytes.toBytes(datatype);
	}

	@Override
	protected String toDatatypeInternal(byte[] bytes) {
		return Bytes.toString(bytes);
	}

}
