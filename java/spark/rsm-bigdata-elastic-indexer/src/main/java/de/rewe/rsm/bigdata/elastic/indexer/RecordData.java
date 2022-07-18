package de.rewe.rsm.bigdata.elastic.indexer;

import java.io.Serializable;

public class RecordData implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String topic;
	private final Integer partition;
	private final Long offset;
	private final transient String data;

	private Exception exception;

	public RecordData(String topic, Integer partition, Long offset, String data) {
		super();
		this.topic = topic;
		this.partition = partition;
		this.offset = offset;
		this.data = data;
	}

	public String getTopic() {
		return this.topic;
	}

	public Integer getPartition() {
		return this.partition;
	}

	public Long getOffset() {
		return this.offset;
	}

	public String getData() {
		return this.data;
	}

	public Exception getException() {
		return this.exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	@Override
	public String toString() {
		return "RecordData [topic=" + this.topic + ", partition=" + this.partition + ", offset=" + this.offset + ", data=" + this.data + ", exception="
				+ this.exception + "]";
	}

}
