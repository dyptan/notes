package de.rewe.hbase.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.util.Bytes;

import de.rewe.hbase.util.RsmHBaseUtils.HTypes;


/**
 * Not Thread Safe. The instance of this class can be used for composite key generation and for reading the key from hbase.
 * 
 * Each part of a compound key has a position number. Ranging from 0 to number of parts -1;
 * Separator is also a part of a compound key;  
 *  
 * Basic pattern to write: 
 * Use HKeySchema to create keys 
 * 
 * byte[] key = new HKeySchema().appendLong(42L).appendInt(23).appendString("HelloWorld!").appendSeparator().appendString("ThisIsTheEnd").getRawBytes();   
 * Put put = new Put(key);
 * ...
 * Basic pattern for reading data, create schema once, for multiple read operations (assumption that all the ):
 * 
 * HKeySchema schema = new HKeySchema().shemaLong().schemaInt().schemaString("HelloWorld!".length()).schemaSeparator().schemaString("ThisIsTheEnd".length()); 
 * ...
 * for(Result result : resultScanner){
 * 		byte[] rawBytes = result.getRow();
 * 		schema.initRead(rawBytes); 
 * 		long firstPartOfaKey = schema.readLong(0);	
 * 		...
 * 
 * }
 * .. 
 * if a compound key consists of strings such as part1-part2-part3, a {@link #initStringSchemaRead(byte[])} method for reading data can be used.  
 *  
 * 
 * @author A10366A
 * 
 */
public class HKeySchema {

	public static final String DEFAULT_SEPARATOR_STRING = "-";
	public static final byte[] DEFAULT_SEPARATOR = Bytes
			.toBytes(DEFAULT_SEPARATOR_STRING);

	/**
	 * 
	 * @author a10366a
	 * 
	 */
	private static final class MetaInfoRecord {

		HTypes htype;
		int offset;
		int length;

		public MetaInfoRecord(HTypes htype, int offset, int length) {
			super();
			this.htype = htype;
			this.offset = offset;
			this.length = length;
		}

		public int getLastIndex() {
			return offset + length - 1;
		}
	}

	/**
	 * contains Types and Size, the key will be generated according this schema
	 * stores also offsets and length
	 */
	private List<MetaInfoRecord> schemaTypes;
	/**
	 * holds the state
	 */
	private byte[] key;
	/**
	 * 
	 */
	private boolean write = false;

	/**
	 * creates an empty skeleton for a key
	 * 
	 */
	public HKeySchema() {
		schemaTypes = new ArrayList<>();
		write = true;
	}


	/**
	 * 
	 * @param key
	 */
	public void initRead(byte[] key){
		write = false;
		this.key = key;
	}
	
	/**
	 * 
	 * @param part
	 *            throws IllegalStateException if the key has not been initialized for write operations
	 */
	private void append(byte[] part) {
		if (write) {
			if (key == null) {
				key = part;
			} else {
				key = Bytes.add(key, part);
			}
		} else {
			throw new IllegalStateException("Key is not initialized for write operation");
		}
	}

	/**
	 * 
	 * @param keyPart
	 * @return
	 * @throws IllegalStateException
	 *             if the key has not been initialized for write operations
	 */
	public HKeySchema appendLong(long keyPart) {
		append(Bytes.toBytes(keyPart));
//		return appendLong();
		return this;
	}

	/**
	 * 
	 * @param keyPart
	 * @return
	 * @throws IllegalStateException
	 *             if the key has not been initialized for write operations
	 */
	public HKeySchema appendByte(byte keyPart) {
		append(Bytes.toBytes(keyPart));
//		return appendByte();
		return this;
	}

	/**
	 * 
	 * @param keyPart
	 * @return
	 * @throws IllegalStateException
	 *             if the key has not been initialized for write operations
	 */
	public HKeySchema appendShort(short keyPart) {
		append(Bytes.toBytes(keyPart));
//		return appendShort();
		return this;
	}

	/**
	 * 
	 * @param keyPart
	 * @return
	 * @throws IllegalStateException
	 *             if the key has not been initialized for write operations
	 */
	public HKeySchema appendInt(int keyPart) {
		append(Bytes.toBytes(keyPart));
//		return appendInt();
		return this;
	}

	/**
	 * 
	 * @param keyPart
	 * @return
	 * @throws IllegalStateException
	 *             if the key has not been initialized for write operations
	 */
	public HKeySchema appendString(String keyPart) {
		byte[] b = Bytes.toBytes(keyPart);
		append(b);
//		return appendString(byteSize);
		return this;
	}

	/**
	 * wrapper for String.format aka printf
	 * 
	 * @param format
	 *            format used by String.format or Print f
	 * @param keyPart
	 * @return
	 */
	public HKeySchema appendString(String format, Object ... args) {
		String string = String.format(format, args);
		return this.appendString(string);
	}

	/**
	 * 
	 * @return
	 * @throws IllegalStateException
	 *             if the key has not been initialized for write operations
	 */
	public HKeySchema appendSeparator() {
		append(DEFAULT_SEPARATOR);
//		return appendSeparator();
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public HKeySchema schemaLong() {
		// get last
		MetaInfoRecord record = null;
		if (!schemaTypes.isEmpty()) {
			MetaInfoRecord metaInfo = this.schemaTypes
					.get(schemaTypes.size() - 1);
			record = new MetaInfoRecord(HTypes.LONG,
					metaInfo.getLastIndex() + 1, 8);
		} else {
			record = new MetaInfoRecord(HTypes.LONG, 0, 8);
		}
		this.schemaTypes.add(record);
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public HKeySchema schemaByte() {
		// get last
		MetaInfoRecord record = null;
		if (!schemaTypes.isEmpty()) {
			MetaInfoRecord metaInfo = this.schemaTypes
					.get(schemaTypes.size() - 1);
			record = new MetaInfoRecord(HTypes.BYTE,
					metaInfo.getLastIndex() + 1, 1);
		} else {
			record = new MetaInfoRecord(HTypes.BYTE, 0, 1);
		}
		this.schemaTypes.add(record);
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public HKeySchema schemaShort() {
		// get last
		MetaInfoRecord record = null;
		if (!schemaTypes.isEmpty()) {
			MetaInfoRecord metaInfo = this.schemaTypes
					.get(schemaTypes.size() - 1);
			record = new MetaInfoRecord(HTypes.SHORT,
					metaInfo.getLastIndex() + 1, 2);
		} else {
			record = new MetaInfoRecord(HTypes.SHORT, 0, 2);
		}
		this.schemaTypes.add(record);
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public HKeySchema schemaInt() {
		// get last
		MetaInfoRecord record = null;
		if (!schemaTypes.isEmpty()) {
			MetaInfoRecord metaInfo = this.schemaTypes
					.get(schemaTypes.size() - 1);
			record = new MetaInfoRecord(HTypes.INT,
					metaInfo.getLastIndex() + 1, 4);
		} else {
			record = new MetaInfoRecord(HTypes.INT, 0, 4);
		}
		this.schemaTypes.add(record);
		return this;
	}

	/**
	 * 
	 * @param stringSize
	 * @return
	 */
	public HKeySchema schemaString(int stringSize) {
		// get last
		MetaInfoRecord record = null;
		if (!schemaTypes.isEmpty()) {
			MetaInfoRecord metaInfo = this.schemaTypes
					.get(schemaTypes.size() - 1);
			record = new MetaInfoRecord(HTypes.STRING,
					metaInfo.getLastIndex() + 1, stringSize);
		} else {
			record = new MetaInfoRecord(HTypes.STRING, 0, stringSize);
		}
		this.schemaTypes.add(record);
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public HKeySchema schemaSeparator() {
		// get last
		MetaInfoRecord record = null;
		if (!schemaTypes.isEmpty()) {
			MetaInfoRecord metaInfo = this.schemaTypes
					.get(schemaTypes.size() - 1);
			record = new MetaInfoRecord(HTypes.STRING,
					metaInfo.getLastIndex() + 1, 1);
		} else {
			record = new MetaInfoRecord(HTypes.STRING, 0, 1);
		}
		this.schemaTypes.add(record);
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public byte[] getRawBytes() {
		return this.key;
	}

	/**
	 * 
	 * @param index
	 * @return
	 * @throws IllegalStateException
	 *             if the key is not initialized for reading
	 * @throws IllegalArgumentException
	 *             if the type at the index position is not a long
	 */
	public long readLong(int index) {
		return Bytes.toLong(read(index, HTypes.LONG));
	}

	/**
	 * 
	 * @param index
	 * @return
	 * @throws IllegalStateException
	 *             if the key is not initialized for reading
	 * @throws IllegalArgumentException
	 *             if the type at the index position is not an int
	 */
	public int readInt(final int index) {
		return Bytes.toInt(read(index, HTypes.INT));
	}

	/**
	 * 
	 * @param index
	 * @return
	 * @throws IllegalStateException
	 *             if the key is not initialized for reading
	 * @throws IllegalArgumentException
	 *             if the type at the index position is not a short
	 */
	public short readShort(final int index) {
		return Bytes.toShort(read(index, HTypes.SHORT));
	}

	/**
	 * 
	 * @param index
	 * @return
	 * @throws IllegalStateException
	 *             if the key is not initialized for reading
	 * @throws IllegalArgumentException
	 *             if the type at the index position is not a string
	 */
	public String readString(final int index) {
		return Bytes.toString(read(index, HTypes.STRING));
	}

	/**
	 * 
	 * @param index
	 * @return
	 * @throws IllegalStateException
	 *             if the key is not initialized for reading
	 * @throws IllegalArgumentException
	 *             if the type at the index position is not byte
	 */
	public byte readByte(final int index) {
		return read(index, HTypes.BYTE)[0];
	}

	/**
	 * 
	 * @param index
	 * @return
	 * @throws IllegalStateException
	 *             if the key is not initialized for reading
	 * @throws IllegalArgumentException
	 *             if the type does not match
	 */
	private byte[] read(final int index, final HTypes type) {
		if (write || key == null) {
			throw new IllegalStateException("Not init for read");
		}

		MetaInfoRecord record = this.schemaTypes.get(index);
		if (record.htype != type) {
			throw new IllegalArgumentException("Datatype conflict! ");
		}
		byte[] buf = new byte[record.length];
		System.arraycopy(key, record.offset, buf, 0, record.length);
		return buf;
	}

	/**
	 * Uses DEFAULT_SEPARATOR_STRING : - (minus ASCII Character)
	 * 
	 * @param bytes
	 * @return
	 */
	public HKeySchema initStringSchemaRead(byte[] bytes) {
		initStringSchemaRead(bytes, DEFAULT_SEPARATOR_STRING);
		return this;
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public HKeySchema initStringSchemaRead(byte[] bytes, String separator) {
		String[] parts = Bytes.toString(bytes).split(separator);
		for (int i = 0; i < parts.length; i++) {
			this.schemaString(parts[i].length());
			if (i < parts.length - 1) {
				this.schemaSeparator();
			}
		}
		this.initRead(bytes);
		return this;
	}

	/**
	 * Number of parts incl. separators.
	 *
	 * @return
	 */
	public int length() {
		return this.schemaTypes.size();
	}
}
