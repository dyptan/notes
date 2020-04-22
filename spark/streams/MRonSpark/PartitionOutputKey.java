package com.comscore.hadoop.mapreduce.lib.output;

import org.apache.hadoop.io.Writable;

public interface PartitionOutputKey<W extends Writable> extends Writable {
	
	boolean isPartitioned();
	
	int getPartitionID();
	
	String getKeyword();
	
	W getNaturalKey();
	
}
