package de.rewe.hbase.dao.mapper;

/**
 * Exception for datatype mapping
 */
public class BusinesObjectMappingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BusinesObjectMappingException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinesObjectMappingException(String message) {
		super(message);
	}

}
