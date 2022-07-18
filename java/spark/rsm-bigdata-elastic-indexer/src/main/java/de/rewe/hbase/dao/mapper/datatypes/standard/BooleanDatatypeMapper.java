package de.rewe.hbase.dao.mapper.datatypes.standard;

import de.rewe.hbase.dao.mapper.datatypes.AbstractSimpleDatatypeMapper;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Datatype mapping instance to convert {@link Boolean} into a <code>byte[]</code> and vice versa
 */
public class BooleanDatatypeMapper extends AbstractSimpleDatatypeMapper<Boolean> {

	@Override
	protected byte[] toBytesInternal(Boolean datatype) {
		return Bytes.toBytes(datatype);
	}

	@Override
	protected Boolean toDatatypeInternal(byte[] bytes) {
		return Bytes.toBoolean(bytes);
	}

}
