package de.rewe.hbase.dao.mapper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import de.rewe.hbase.dao.mapper.datatypes.AbstractCompositeDatatypeMapper;
import de.rewe.hbase.dao.mapper.datatypes.AbstractSimpleDatatypeMapper;
import de.rewe.hbase.dao.mapper.datatypes.DatatypeMapper;
import de.rewe.hbase.dao.mapper.datatypes.DatatypeMapperRepository;

/**
 * Abstraction for all mapping instances used in BigData. A mapping instance is a definition how to translate the column byte arrays of a table into
 * Objects which are bundled within a single business object. </br>
 * The goal is to translate data within the same column as same byte array representation (e.g. as integer) so extraction is always the same.
 * 
 * @param T
 *            The BO which needs to be stored or loaded from BigData
 */
public abstract class AbstractDaoMapper<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final byte[] columnFamilyBytes;

	protected AbstractDaoMapper(byte[] columnFamilyBytes) {
		this.columnFamilyBytes = columnFamilyBytes;
	}

	public abstract T mapToBo(Result result);

	/**
	 * Creates a put action for the given business object.
	 * 
	 * @param bo
	 *            the business object to create a put for
	 * @return the put action writing all data from the business object into the database
	 * 
	 * @throws BusinesObjectMappingException
	 *             whenever a transformation can not be done
	 */
	public final Put createPut(T bo) {
		this.validateKeyInformation(bo);
		Put put = new Put(this.createKey(bo));
		this.enhancePut(put, bo);
		return put;
	}

	/**
	 * Creates the key bytes for the given business object.
	 * 
	 * @param bo
	 *            the business object to create the key bytes for
	 * @return the key bytes for the given business object
	 * 
	 * @throws BusinesObjectMappingException
	 *             whenever a transformation can not be done
	 */
	protected abstract byte[] createKey(T bo);

	/**
	 * Enables validation of any element within the business object before writing the key
	 * 
	 * @param bo
	 *            the business object validate
	 * @throws BusinesObjectMappingException
	 *             whenever a key information restriction is hurt
	 */
	protected abstract void validateKeyInformation(T bo);

	/**
	 * Enhances the {@link Put} with all values from the given business object.
	 * 
	 * @throws BusinesObjectMappingException
	 *             whenever a transformation can not be done
	 */
	protected abstract void enhancePut(Put put, T bo);

	/**
	 * Returns the table name the mapping instance is responsible for
	 * 
	 * @return the table name
	 */
	public abstract String getTableName();

	/**
	 * Translate a byte array from given <code>Result</code> into the defined data type (<code>DT</code>) by using the given column bytes. The boolean
	 * flag <code>required</code> defines weather or not <code>null</code> values are allowed while getting the byte-array from the database.</br>
	 * Uses the {@link DatatypeMapperRepository#getDatatypeMapper(String, Class)} to translate the given byte-array into the object.
	 * 
	 * @param clazz
	 *            The class of the resulting data object (e.g. Long.class)
	 * @param hbaseResult
	 *            The result from the HBase
	 * @param columnBytes
	 *            byte[] the column bytes the value is taken from
	 * @param required
	 *            <code>true</code> if the data type is required (a value must exist in the given hbaseResult for the given columnBytes) otherwise
	 *            <code>false</code>
	 * @return the object from the database
	 * @throws BusinesObjectMappingException
	 *             whenever a data type is marked as required but not available within the result
	 */
	protected final <D> D buildDatatype(Class<D> clazz, Result hbaseResult, byte[] columnBytes, boolean required) {
		byte[] value = hbaseResult.getValue(this.columnFamilyBytes, columnBytes);
		if (required && value == null) {
			throw new BusinesObjectMappingException(
					"null value is not allowed. Found for key " + this.translateKeyToReadableStringForException(hbaseResult.getRow()) + " in table "
							+ this.getTableName() + ", column " + Bytes.toString(columnBytes));
		}
		DatatypeMapper datatypeMapper = DatatypeMapperRepository.getInstance().getDatatypeMapper(clazz);
		if (!(datatypeMapper instanceof AbstractSimpleDatatypeMapper)) {
			throw new BusinesObjectMappingException(
					"Wrong datatype mapper found or wrong function called. Class: " + clazz + ", MapperClass: " + datatypeMapper.getClass());
		}
		return ((AbstractSimpleDatatypeMapper<D>) datatypeMapper).toDatatype(value);
	}

	/**
	 * Adds the object as byte array to the given put action within the column defined by the given columnBytes.</br>
	 * If the value is defined as required an {@link BusinesObjectMappingException} is thrown whenever the object is <code>null</code>.
	 * 
	 * @param put
	 *            the put action to append the byte array representing the object
	 * @param columnBytes
	 *            the byte[] defining the column where to put the byte[]
	 * @param data
	 *            the data as object
	 * @param required
	 *            <code>true</code> if the data is mandatory and an {@link BusinesObjectMappingException} will be thrown on <code>null</code>,
	 *            otherwise false
	 * @throws BusinesObjectMappingException
	 *             Whenever the data is marked as required but the given Object is <code>null</code> or no mapper (see
	 *             {@link DatatypeMapperRepository} and {@link DatatypeMapper}) is defined
	 */
	protected final <D> void addColumn(Put put, byte[] columnBytes, D data, boolean required) {
		if (data == null) {
			if (required) {
				throw new BusinesObjectMappingException("null value is not allowed. In table " + this.getTableName() + " the column "
						+ Bytes.toString(columnBytes) + " is defined as not null!");
			} else {
				return;
			}
		}

		DatatypeMapper datatypeMapper = DatatypeMapperRepository.getInstance().getDatatypeMapper(data.getClass());
		if (datatypeMapper == null) {
			throw new BusinesObjectMappingException("No datatype mapper defined for class " + data.getClass());
		}
		if (!(datatypeMapper instanceof AbstractSimpleDatatypeMapper)) {
			throw new BusinesObjectMappingException("Wrong datatype mapper defined or wrong method used. Requested class: " + data.getClass());
		}
		Optional<byte[]> bytes = ((AbstractSimpleDatatypeMapper<D>) datatypeMapper).toBytes(data);

		if (!bytes.isPresent()) {
			if (required) {
				throw new BusinesObjectMappingException("null value is not allowed. In table " + this.getTableName() + " the column "
						+ Bytes.toString(columnBytes) + " is defined as not null!");
			} else {
				return;
			}
		}
		put.addColumn(this.columnFamilyBytes, columnBytes, bytes.get());
	}

	/**
	 * Translate a array of byte arrays from given <code>Result</code> into the defined datatype (<code>DT</code>) by using the given columns bytes.
	 * The boolean flag <code>required</code> defines weather or not <code>null</code> values are allowed while getting the byte-arrays from the
	 * database. The check is done over all columns so at least one column has to be not <code>null</code> to pass the check.</br>
	 * Uses the {@link DatatypeMapperRepository#getDatatypeMapper(String, Class)} to translate the given byte-array into the object.
	 * 
	 * @param clazz
	 *            The class of the resulting data object (e.g. Long.class)
	 * @param hbaseResult
	 *            The result from the HBase
	 * @param columnsBytes
	 *            the columns bytes the values are taken from
	 * @param required
	 *            <code>true</code> if the datatype is required (at least one value must exist in the given hbaseResult for the given columnsBytes)
	 *            otherwise <code>false</code>
	 * @return the object from the database
	 * @throws BusinesObjectMappingException
	 *             whenever a datatype is marked as required but not available within the result
	 */
	protected final <D> D buildCompositeDatatype(Class<D> clazz, Result hbaseResult, byte[][] columnsBytes, boolean required) {
		byte[][] values = new byte[columnsBytes.length][];
		for (int i = 0; i < columnsBytes.length; i++) {
			values[i] = hbaseResult.getValue(this.columnFamilyBytes, columnsBytes[i]);
		}
		if (required && allValuesNull(values)) {
			throw new BusinesObjectMappingException(
					"null values are not allowed. Found for key " + this.translateKeyToReadableStringForException(hbaseResult.getRow()) + " in table "
							+ this.getTableName() + ", columns " + Arrays.toString(this.translateColumnsToReadableStringForException(columnsBytes)));
		}
		DatatypeMapper datatypeMapper = DatatypeMapperRepository.getInstance().getDatatypeMapper(clazz);
		if (!(datatypeMapper instanceof AbstractCompositeDatatypeMapper<?>)) {
			throw new BusinesObjectMappingException(
					"Wrong datatype mapper found or wrong function called. Class: " + clazz + ", MapperClass: " + datatypeMapper.getClass());
		}
		return ((AbstractCompositeDatatypeMapper<D>) datatypeMapper).toDatatype(values);
	}

	/**
	 * Adds the object as byte arrays over multiple columns to the given put action within the columns defined by the given columnBytes.</br>
	 * If the value is defined as required an {@link BusinesObjectMappingException} is thrown whenever the object is <code>null</code>.
	 * 
	 * @param put
	 *            the put action to append the byte array representing the object
	 * @param columnsBytes
	 *            the byte[] array defining the columns where to put the byte[]
	 * @param data
	 *            the data as object
	 * @param required
	 *            <code>true</code> if the data is mandatory and an {@link BusinesObjectMappingException} will be thrown on <code>null</code>,
	 *            otherwise false
	 * @throws BusinesObjectMappingException
	 *             Whenever the data is marked as required but the given Object is <code>null</code> or no mapper (see
	 *             {@link DatatypeMapperRepository} and {@link DatatypeMapper}) is defined
	 */
	protected final <D> void addCompositeColumn(Put put, byte[][] columnsBytes, D data, boolean required) {
		if (data == null) {
			if (required) {
				throw new BusinesObjectMappingException("null value is not allowed. In table " + this.getTableName() + " the columns "
						+ Arrays.toString(this.translateColumnsToReadableStringForException(columnsBytes)) + " is defined as not null!");
			} else {
				return;
			}
		}

		DatatypeMapper datatypeMapper = DatatypeMapperRepository.getInstance().getDatatypeMapper(data.getClass());
		if (datatypeMapper == null) {
			throw new BusinesObjectMappingException("No datatype mapper defined for class " + data.getClass());
		}
		if (!(datatypeMapper instanceof AbstractCompositeDatatypeMapper<?>)) {
			throw new BusinesObjectMappingException("Wrong datatype mapper defined or wrong method used. Requested class: " + data.getClass());
		}
		Optional<byte[][]> bytes = ((AbstractCompositeDatatypeMapper<D>) datatypeMapper).toBytes(data);

		if (!bytes.isPresent() || allValuesNull(bytes.get())) {
			if (required) {
				throw new BusinesObjectMappingException("null value is not allowed. In table " + this.getTableName() + " the column "
						+ Arrays.toString(this.translateColumnsToReadableStringForException(columnsBytes)) + " is defined as not null!");
			} else {
				return;
			}
		}
		if (bytes.get().length != columnsBytes.length) {
			throw new BusinesObjectMappingException(
					"The number of created byte arrays does not match the number of given column names for the table " + this.getTableName() + " and data type "
							+ data.getClass() + ". Created columns: " + bytes.get().length + ", given column names: " + columnsBytes.length);
		}
		for (int i = 0; i < columnsBytes.length; i++) {
			if (bytes.get()[i] != null) {
				put.addColumn(this.columnFamilyBytes, columnsBytes[i], bytes.get()[i]);
			}
		}
	}

	/**
	 * Method to translate the key bytes into a human readable string for exception reasons
	 * 
	 * @param keyBytes
	 *            the bytes of the key
	 * @return a human readable version of the key
	 */
	protected String translateKeyToReadableStringForException(byte[] keyBytes) {
		return Bytes.toString(keyBytes);
	}

	protected String[] translateColumnsToReadableStringForException(byte[][] columnsBytes) {
		String[] columnsAsString = new String[columnsBytes.length];
		for (int i = 0; i < columnsBytes.length; i++) {
			if (columnsBytes[i] != null) {
				columnsAsString[i] = Bytes.toString(columnsBytes[i]);
			}
		}
		return columnsAsString;
	}

	private static boolean allValuesNull(byte[][] values) {
		for (byte[] bs : values) {
			if (bs != null) {
				return false;
			}
		}
		return true;
	}

}
