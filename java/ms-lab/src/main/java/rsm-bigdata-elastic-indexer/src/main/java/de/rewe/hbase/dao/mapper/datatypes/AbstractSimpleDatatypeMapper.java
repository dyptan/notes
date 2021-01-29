package de.rewe.hbase.dao.mapper.datatypes;

import java.util.Optional;

/**
 * Abstract class to translate a object into a byte array and back to the object
 * 
 * @param <T>
 *            the object class
 */
public abstract class AbstractSimpleDatatypeMapper<T> implements DatatypeMapper {

	/**
	 * Translates a data object into a <code>byte[]</code>. Whenever the data is <code>null</code>, <code>null</code> will be returned. Otherwise the
	 * internal implementation {@link AbstractSimpleDatatypeMapper#toBytesInternal(Object)} is called.
	 * 
	 * @param data
	 *            the data to be transformed into a byte array
	 * @return the <code>byte[]</code> representation of the object
	 */
	public final Optional<byte[]> toBytes(T data) {
		if (data == null) {
			return Optional.empty();
		}
		return Optional.of(this.toBytesInternal(data));
	}

	/**
	 * Translates the data into a single <code>byte[]</code>. The data object is never null.
	 * 
	 * @param data
	 *            the data to be transformed
	 * @return the <code>byte[]</code> representation of the data
	 */
	protected abstract byte[] toBytesInternal(T data);

	/**
	 * Translates a <code>byte[]</code> into a corresponding object. Whenever bytes are <code>null</code>, a <code>null</code> object will be
	 * returned. Otherwise the internal method {@link AbstractSimpleDatatypeMapper#toDatatypeInternal(byte[])} will be called.
	 * 
	 * @param bytes
	 *            the byte to transform into a object
	 * @return the corresponding object
	 */
	public final T toDatatype(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		return this.toDatatypeInternal(bytes);
	}

	/**
	 * Translates a single <code>byte[]</code> into a Object. The given <code>byte[]</code> can never be null.
	 * 
	 * @param bytes
	 *            the byte to transform into a object
	 * @return the corresponding object
	 */
	protected abstract T toDatatypeInternal(byte[] bytes);

}
