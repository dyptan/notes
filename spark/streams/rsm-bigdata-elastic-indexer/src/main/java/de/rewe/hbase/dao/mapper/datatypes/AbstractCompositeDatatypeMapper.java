package de.rewe.hbase.dao.mapper.datatypes;

import java.util.Optional;

import de.rewe.hbase.dao.mapper.BusinesObjectMappingException;

/**
 * Abstract class to translate an object into a byte array and back to the object
 * 
 * @param <T>
 *            the object class
 */
public abstract class AbstractCompositeDatatypeMapper<T> implements DatatypeMapper {

	/**
	 * The number of columns the mapper fills or must read
	 */
	private final int numColums;

	/**
	 * Default constructor with the number of
	 * 
	 * @param numColums
	 */
	protected AbstractCompositeDatatypeMapper(int numColums) {
		this.numColums = numColums;
	}

	/**
	 * Translates a data object into multiple <code>byte[]</code>. Whenever the data is <code>null</code>, <code>null</code> will be returned.
	 * Otherwise the internal implementation {@link AbstractCompositeDatatypeMapper#toBytesInternal(Object)} is called.
	 * 
	 * @param data
	 *            the data to be transformed into a byte array
	 * @return the <code>byte[]</code> arrays representation of the object
	 */
	public final Optional<byte[][]> toBytes(T data) {
		if (data == null) {
			return Optional.empty();
		}
		byte[][] bytes = this.toBytesInternal(data);
		if (bytes != null && bytes.length != this.numColums) {
			throw new BusinesObjectMappingException("Wrong number of byte arrays created. Expected " + this.numColums + ", was " + bytes.length);
		}
		return Optional.of(bytes);
	}

	/**
	 * Translates the data into a array of <code>byte[]</code>. The data object is never null.
	 * 
	 * @param data
	 *            the data to be transformed
	 * @return the <code>byte[]</code> array representation of the data
	 */
	protected abstract byte[][] toBytesInternal(T data);

	/**
	 * Translates a <code>byte[]</code> array into a corresponding object. Whenever all bytes arrays are <code>null</code>, a <code>null</code> object
	 * will be returned. Otherwise the internal method {@link AbstractCompositeDatatypeMapper#toDatatypeInternal(byte[])} will be called.
	 * 
	 * @param bytes
	 *            the byte to transform into a object
	 * @return the corresponding object
	 */
	public final T toDatatype(byte[]... bytes) {
		if (bytes == null) {
			return null;
		}
		if (bytes.length != this.numColums) {
			throw new BusinesObjectMappingException("Wrong number of data columns specified. Expected " + this.numColums + ", was " + bytes.length);
		}
		return this.toDatatypeInternal(bytes);
	}

	/**
	 * Translates a array of <code>byte[]</code> into a Object. The given <code>byte[]</code> inside the arrays can never be null.
	 * 
	 * @param bytes
	 *            the byte to transform into a object
	 * @return the corresponding object
	 */
	protected abstract T toDatatypeInternal(byte[][] bytes);

}
