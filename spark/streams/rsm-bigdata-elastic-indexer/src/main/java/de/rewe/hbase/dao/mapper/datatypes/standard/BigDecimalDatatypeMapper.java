package de.rewe.hbase.dao.mapper.datatypes.standard;

import java.math.BigDecimal;

import org.apache.hadoop.hbase.util.Bytes;

import de.rewe.hbase.dao.mapper.datatypes.AbstractSimpleDatatypeMapper;

/**
 * Datatype mapping instance to convert {@link BigDecimal} into a <code>byte[]</code> and vice versa
 */
public class BigDecimalDatatypeMapper extends AbstractSimpleDatatypeMapper<BigDecimal> {

	@Override
	protected byte[] toBytesInternal(BigDecimal datatype) {
		return Bytes.toBytes(datatype);
	}

	@Override
	protected BigDecimal toDatatypeInternal(byte[] bytes) {
		return Bytes.toBigDecimal(bytes);
	}

}
