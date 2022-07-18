package de.rewe.hbase.dao;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rewe.hbase.connection.HConnectionProvider;
import de.rewe.hbase.dao.mapper.AbstractDaoMapper;

/**
 * Abstract class for all DAOs using a mapping implementation to translate data between database an BO. </br>
 * Each inherited dao will have a reference to its own table but will reuse an existing connection as long as possible. So creating a AbstractHBaseDao
 * instance is very efficient and should be done each time data is stored or loaded from the database. Also the {@code AutoCloseable#close()} method
 * is implemented so the instantiation can be don within a try-statement.
 * 
 * @param <T>
 *            The business object class which holds all information within the table
 */
public abstract class AbstractHBaseDao<T extends Serializable, M extends AbstractDaoMapper<T>> implements AutoCloseable {

	private static final int BULK_SIZE_DEFAULT = 5000;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected Table table;

	private M mapper;

	private int bulkSizePut;

	private int bulkSizeDelete;

	protected AbstractHBaseDao(String namespacePrefix, M mapper) throws IOException {
		this(namespacePrefix, mapper, BULK_SIZE_DEFAULT, BULK_SIZE_DEFAULT);
	}

	protected AbstractHBaseDao(String namespacePrefix, M mapper, int bulkSizePut, int bulkSizeDelete) throws IOException {
		this.mapper = mapper;
		this.bulkSizePut = bulkSizePut;
		this.bulkSizeDelete = bulkSizeDelete;
		if (namespacePrefix == null) {
			throw new IllegalArgumentException(
					"Namespace for tables not set. Should be set using a system property or within the cluster using configuration properties.");
		}
		String tableName = this.mapper.getTableName();
		if (tableName == null) {
			throw new IllegalArgumentException("TableName must be specified by mapping instance: " + mapper.getClass());
		}
		if (tableName.contains(":") || tableName.contains("/")) {
			throw new IllegalArgumentException("Tablename must not contain ':' or '/', found " + tableName);
		}
		this.table = HConnectionProvider.getInstance().getTable(TableName.valueOf(namespacePrefix + tableName));
	}

	/**
	 * the mapping instance to translate data from an object to the corresponding byte arrays and back
	 */
	protected M getMapper() {
		return this.mapper;
	}

	/**
	 * Reads informations from database based on the list of key bytes. It will produce a bulk load and then translate all data using the
	 * {@link AbstractDaoMapper#mapToBo(Result)} method.
	 * 
	 * @param keys
	 *            the keys for each entry to read
	 * @param required
	 *            weather or not each row is required to be found within the database
	 * @return the list of business objects stored within database for the keys
	 * @throws IOException
	 *             whenever an exception occurs while communicating with the database
	 */
	protected List<T> get(List<byte[]> keys, boolean required) throws IOException {
		this.logger.debug("Requesting Data for keys {}", keys);
		// create list of gets
		List<Get> getterList = keys.stream().map(Get::new).collect(Collectors.toList());

		// read data
		Result[] resultFromDb = this.table.get(getterList);

		// translate
		List<T> boList = Arrays.asList(resultFromDb).stream().map(result -> this.translateToBo(result, required)).collect(Collectors.toList());
		this.logger.debug("Read BOs {}", boList);
		return boList;
	}

	/**
	 * Reads information from database based on the key bytes.
	 * 
	 * @param key
	 *            the key for the entry to read
	 * @param required
	 *            whether or not each row is required to be found within the database
	 * @return the list of business objects stored within database for the keys
	 * @throws IOException
	 *             whenever an exception occurs while communicating with the database
	 */
	protected T get(byte[] key, boolean required) throws IOException {
		this.logger.debug("Requesting Data for get {}", key);
		Result result = this.table.get(new Get(key));
		return this.translateToBo(result, required);
	}

	/**
	 * Deletes all found elements for the scan in this table using {@link AbstractHBaseDao#delete(Scan, Predicate)} without filter.
	 * 
	 * @param scan
	 *            The scan for elements to filter with the batch size of scanned elements loaded
	 * @throws IOException
	 *             Whenever an exception occurs while communicating with the database
	 */
	protected void delete(Scan scan) throws IOException {
		this.delete(scan, null);
	}

	/**
	 * Deletes all found elements for the scan in this table. Using the additional filter found elements can be left out depending on other decisions.
	 * The delete is done within a bulk using the {@link AbstractHBaseDao#bulkSizeDelete} size.
	 * 
	 * @param scan
	 *            The scan for elements to filter with the batch size of scanned elements loaded
	 * @param filter
	 *            a possible filter or <code>null</code> whenever all found elements should be deleted
	 * @throws IOException
	 *             Whenever an exception occurs while communicating with the database
	 */
	protected void delete(Scan scan, Predicate<Result> filter) throws IOException {
		try (ResultScanner scanner = this.table.getScanner(scan);) {
			List<Delete> deletes = new LinkedList<>();
			Result resultFromHBase;
			while ((resultFromHBase = scanner.next()) != null) {
				if (filter == null || filter.test(resultFromHBase)) {
					deletes.add(new Delete(resultFromHBase.getRow()));
					if (deletes.size() >= this.bulkSizeDelete) {
						this.table.delete(deletes);
						deletes.clear();
					}
				}
			}
			if (!deletes.isEmpty()) {
				this.table.delete(deletes);
			}
		}
	}

	/**
	 * Puts a business object into the database
	 * 
	 * @param bo
	 *            the business object to store
	 * @throws IOException
	 *             Whenever an exception occurs while communicating with the database
	 */
	public void put(T bo) throws IOException {
		this.table.put(this.getMapper().createPut(bo));
	}

	/**
	 * Puts a list of business object into the database using the bulk operation with a bulk size depending on {@link AbstractHBaseDao#bulkSizePut}.
	 * 
	 * @param bos
	 *            the list of business objects to store
	 * @throws IOException
	 *             Whenever an exception occurs while communicating with the database
	 */
	public void put(List<T> bos) throws IOException {
		List<Put> puts = new LinkedList<>();
		for (T bo : bos) {
			puts.add(this.getMapper().createPut(bo));
			if (puts.size() >= this.bulkSizePut) {
				this.table.put(puts);
				puts.clear();
			}
		}
		if (!puts.isEmpty()) {
			this.table.put(puts);
		}
	}

	/**
	 * Scans for all elements within the table defined by the scan operation. </br>
	 * Ensure that the scan has a proper {@link Scan#setCaching(int)} size.
	 * 
	 * @param scan
	 *            The scan operation looking for the business objects to return
	 * @throws IOException
	 *             Whenever an exception occurs while communicating with the database
	 */
	protected List<T> scan(Scan scan) throws IOException {
		List<T> result = new LinkedList<>();
		try (ResultScanner scanner = this.table.getScanner(scan);) {
			Result next = null;
			while ((next = scanner.next()) != null) {
				result.add(this.getMapper().mapToBo(next));
			}
		}
		return result;
	}

	@Override
	public void close() throws IOException {
		this.table.close();
	}

	/**
	 * Translates a result set from the database to the corresponding business object using {@link AbstractDaoMapper#mapToBo(Result)}.</br>
	 * Whenever the result from the database is not available and the value is not required a <code>null</code> value is returned. Otherwise
	 * {@link AbstractDaoMapper#mapToBo(Result)} is called.
	 * 
	 * @param result
	 *            The result from the database
	 * @param required
	 *            Flag weather the result is required or not
	 * @return the corresponding business object
	 * @throws IllegalStateException
	 *             whenever a translation exception occurred (e.g. a mandatory value is not within the database row)
	 */
	protected T translateToBo(Result result, boolean required) {
		if (!required && (result == null || result.isEmpty())) {
			return null;
		}

		return this.mapper.mapToBo(result);
	}

}
