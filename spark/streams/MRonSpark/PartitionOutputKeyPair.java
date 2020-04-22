package com.comscore.hadoop.mapreduce.lib.output;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class PartitionOutputKeyPair implements PartitionOutputKey<Text> {

	private boolean isPartitioned = true;
	private int partitionID;
	private String keyword;
	private Text naturalKey = new Text();
	
	@Override
    public String toString() {
	    return "PartitionOutputKeyPair [isPartitioned=" + isPartitioned + ", partitionID=" + partitionID + ", keyword="
	            + keyword + ", naturalKey=" + naturalKey + "]";
    }
	
	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + (isPartitioned ? 1231 : 1237);
	    result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
	    result = prime * result + ((naturalKey == null) ? 0 : naturalKey.hashCode());
	    result = prime * result + partitionID;
	    return result;
    }

	@Override
    public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null) return false;
	    if (getClass() != obj.getClass()) return false;
	    PartitionOutputKeyPair other = (PartitionOutputKeyPair) obj;
	    if (isPartitioned != other.isPartitioned) return false;
	    if (keyword == null) {
		    if (other.keyword != null) return false;
	    } else if (!keyword.equals(other.keyword)) return false;
	    if (naturalKey == null) {
		    if (other.naturalKey != null) return false;
	    } else if (!naturalKey.equals(other.naturalKey)) return false;
	    if (partitionID != other.partitionID) return false;
	    return true;
    }

	@Override
    public void write(DataOutput out) throws IOException {
	    if(naturalKey==null) {
	    	NullWritable.get().write(out);
	    } else {
	    	naturalKey.write(out);
	    }
    }

	@Override
    public void readFields(DataInput in) throws IOException {
		naturalKey.readFields(in);
    }

	@Override
    public boolean isPartitioned() {
	    return this.isPartitioned;
    }

	@Override
    public int getPartitionID() {
	    return this.partitionID;
    }

    public void setPartitionID(int id) {
	    this.partitionID = id;
    }

	@Override
    public String getKeyword() {
	    return this.keyword;
    }

    public void setKeyword(String keyword) {
	    this.keyword = keyword;
	    
    }

	@Override
    public Text getNaturalKey() {
	    return this.naturalKey;
    }

    public void setNaturalKey(Text key) {
	    this.naturalKey = key;
    }
    
    public void setNaturalKey(String key) {
    	this.naturalKey.set(key);
    }

}
