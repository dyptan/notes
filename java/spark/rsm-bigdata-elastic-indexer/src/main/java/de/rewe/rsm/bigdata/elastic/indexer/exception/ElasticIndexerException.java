package de.rewe.rsm.bigdata.elastic.indexer.exception;

public class ElasticIndexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ElasticIndexerException() {
		super();
	}

	public ElasticIndexerException(String message) {
		super(message);
	}

	public ElasticIndexerException(Throwable cause) {
		super(cause);
	}

	public ElasticIndexerException(String message, Throwable cause) {
		super(message, cause);
	}

}
