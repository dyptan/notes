package com.comscore.hadoop.mapreduce.lib.output;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapred.FileAlreadyExistsException;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;

import com.comscore.adeffx.oreo.utils.GeneralPartitioningUtils;

/**
 * 
 * There are two parameters required to be configured in order to use this outputformat class.
 * 
 * 1. com.comscore.mapreduce.outputformat.outputdirectory.path
 *    Output format needs to know what the desired output directory looks like. This value should provide an example path of output directory. This value should contain a valid partition id too. 
 *    eg., /tmp/vcemr/part_00000/pre_cume_agg_hisp/5205d
 *    
 * 2. com.comscore.mapreduce.outputformat.outputdirectory.pattern
 *    This value provides a regex that can be applied on the example output directory path in order to extract and replace partition id.
 * 3. com.comscore.mapreduce.total_partitions
 * 	  This value tells output format the total number of partitions.
 * 
 * @author yxiao
 * @param <K> output key class.
 * @param <V> output value class.
 */
public class PartitionWriteToLocalOutputFormat<K extends Writable, V> extends TextOutputFormat<PartitionOutputKey<K>, V>{
    
    private static final String DEFAULT_DELIMITER = "\t";
    
    // Output format creates output paths based on following configuration. They are required.
    /**
     * This is a Hadoop job configuration parameter which points to one of the output directory. Outputformat uses this path to build all output directories.
     */
    public static final String CONFIG_OUTPUT_DIRECTORY_PATH = "com.comscore.mapreduce.outputformat.outputdirectory.path";
    /**
     * This is a Hadoop job configuration parameter which defines how to parse the directory provided in CONFIG_OUTPUT_DIRECTORY_PATH. It is a regular expression to extract partition id from given path.
     */
    public static final String CONFIG_OUTPUT_DIRECTORY_PATTERN = "com.comscore.mapreduce.outputformat.outputdirectory.pattern";
    /**
     * This is a Hadoop job configuration parameter which controls how many output directories need to be created.
     */
    public static final String CONFIG_TOTAL_PARTITIONS = "com.comscore.mapreduce.total_partitions";
    
    // output format creates 0-byte output if there is no output corresponding to following paths. It's opitional configuration.
    /**
     * This is a Hadoop job configuration parameter which controls what files are mandatory to create. Files specified in this parameter will always be created.
     */
    public static final String CONFIG_PRE_CREATE_FILES = "com.comscore.outputformat.pre_create_files";  // comma separate
    
    // configure output paths
    private static String outputDirectoryExample;
    private static String outputPathPattern;
    
	private MultipleFilesOutputCommitter mulitpleFilesCommitter;
    
	/**
	 * {@inheritDoc}
	 */
    @Override
    public synchronized OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException {
        if (mulitpleFilesCommitter==null) {
            Path output = new Path(context.getConfiguration().get("mapred.output.dir"));
            mulitpleFilesCommitter = new MultipleFilesOutputCommitter(output,context);
        }
        
        return mulitpleFilesCommitter;
    }
    
    /**
     * A helper method that throws exception if given value is not configured.
     * @param value
     * @param error
     * @throws IllegalArgumentException 
     */
    private void throwConfigurationError(String value, String error) {
    	if (value==null || value.trim().length()==0) {
    		throw new IllegalArgumentException(error);
    	}
    }
    
    private static String generateOutputDirectory(String outputDirExample, String regex, String newPartitionId) {
    	
    	return GeneralPartitioningUtils.replacePartitionIdInPath(outputDirExample, regex, newPartitionId);
    	
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void checkOutputSpecs(JobContext context) throws IOException {
        try {
            super.checkOutputSpecs(context);
        } catch (FileAlreadyExistsException e) {
            System.err.println("Ignore the fact that output dir already exists...");
        }
        
        Configuration conf = context.getConfiguration();
        
        outputDirectoryExample = conf.get(CONFIG_OUTPUT_DIRECTORY_PATH);
        throwConfigurationError(outputDirectoryExample,
                                "cannot find an example output directory from configuration " + this.CONFIG_OUTPUT_DIRECTORY_PATH);
        
        outputPathPattern = conf.get(CONFIG_OUTPUT_DIRECTORY_PATTERN);
        throwConfigurationError(outputPathPattern, 
                                "canno find a pattern for output directory from configuration " + this.CONFIG_OUTPUT_DIRECTORY_PATTERN);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public RecordWriter<PartitionOutputKey<K>, V> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        boolean isCompressed = getCompressOutput(job);	
        Configuration conf = job.getConfiguration();
        
        outputDirectoryExample = conf.get(CONFIG_OUTPUT_DIRECTORY_PATH);
        throwConfigurationError(outputDirectoryExample,
                                "cannot find an example output directory from configuration " + this.CONFIG_OUTPUT_DIRECTORY_PATH);
        
        outputPathPattern = conf.get(CONFIG_OUTPUT_DIRECTORY_PATTERN);
        throwConfigurationError(outputPathPattern, 
                                "canno find a pattern for output directory from configuration " + this.CONFIG_OUTPUT_DIRECTORY_PATTERN);
        
        String keyValueSeparator= conf.get("mapred.textoutputformat.separator", DEFAULT_DELIMITER);
        CompressionCodec codec = null;
        String extension = "";
        if (isCompressed) {
            Class<? extends CompressionCodec> codecClass = getOutputCompressorClass(job, GzipCodec.class);
            codec = ReflectionUtils.newInstance(codecClass, conf);
            extension = codec.getDefaultExtension();
        }
        Path file = getDefaultWorkFile(job, extension);
        FileSystem fs = file.getFileSystem(conf);
        MultipleFilesOutputCommitter committer = (MultipleFilesOutputCommitter) getOutputCommitter(job);
        if (!isCompressed) {
            System.err.println("When creating record writer, it's not a compression stream.");
            return new WriteToLocalRecordWriter<K, V>(keyValueSeparator, job, committer, isCompressed, fs);
        }
        else {
            System.err.println("When creating record writer, it's a compression stream.");
            return new WriteToLocalRecordWriter<K, V>(keyValueSeparator, job, committer, isCompressed, fs);
        }
    }
    
    public static Path getDefaultWorkFile(FileOutputCommitter committer, String fileName) throws IOException{
    	System.err.println("what's workpath from committer? " + committer.getWorkPath());
    	return new Path(committer.getWorkPath(), fileName);
    }
    
    protected static class WriteToLocalRecordWriter<K extends Writable, V> extends RecordWriter<PartitionOutputKey<K>, V> {
    	
    	private static final char DASH = '-';
    	private static final String NULL = "null";
        private static final String UNDERSCORE = "_";
        private static final String COMMA = ",";
        private static final String FILE_SUFFIX = ".txt";
        private static final int FIVE = 5;
        
        private TaskAttemptContext taskContext;
        private MultipleFilesOutputCommitter outputCommitter;
        private boolean isCompressed = true;
        private CompressionCodec compressionCodec;
        private static Configuration conf;
        private int totalPartitions;
        private String defaultOutputName;
        private String outputExtension;
        private String separator;
        private StringBuilder stringBuilder = new StringBuilder();
        private Map<String, TextOutputFormat.LineRecordWriter<Writable, V>> recordWriters = new HashMap<String, TextOutputFormat.LineRecordWriter<Writable, V>>();
        private TextOutputFormat.LineRecordWriter<Writable, V> defaultRecordWriter;
        
        // pre create files
        private int partitionId;        
        
        private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
        static {
            NUMBER_FORMAT.setMinimumIntegerDigits(FIVE);
            NUMBER_FORMAT.setGroupingUsed(false);
        }
        
        public WriteToLocalRecordWriter(TaskAttemptContext context, MultipleFilesOutputCommitter committer, boolean isCompression, FileSystem fs)  
        		throws IOException {
            this(DEFAULT_DELIMITER, context, committer, isCompression, fs);
        }
        
        public WriteToLocalRecordWriter(String separator, TaskAttemptContext context, MultipleFilesOutputCommitter committer, boolean isCompression, FileSystem fs) 
        	throws IOException {
            taskContext = context;
            conf = taskContext.getConfiguration();
            outputCommitter = committer;
            isCompressed = conf.getBoolean("mapred.output.compress", false);
            if (isCompressed) {
                System.err.println("It's compression in constructor.");
                Class<? extends CompressionCodec> codecClass = getOutputCompressorClass(taskContext, GzipCodec.class);
                compressionCodec = ReflectionUtils.newInstance(codecClass, taskContext.getConfiguration());
                outputExtension = compressionCodec.getDefaultExtension();
            }
            if (outputExtension==null || outputExtension.equals(NULL) 
            		|| outputExtension.length()==0) {
                outputExtension = "";
            }
            this.separator = separator;
            
            defaultOutputName = conf.get(FileOutputFormat.BASE_OUTPUT_NAME, FileOutputFormat.PART);
            
            System.err.println("Using " + defaultOutputName + " as default output filename prefix.");
            
            totalPartitions = conf.getInt(CONFIG_TOTAL_PARTITIONS, 0);
            if (totalPartitions<1) {
            	throw new IOException("error in configuration " + CONFIG_TOTAL_PARTITIONS + ": totalPartitions="+totalPartitions);
            }
            
        }	// public WriteToLocalRecordWriter(String separator, TaskAttemptContext context, MultipleFilesOutputCommitter committer, boolean isCompression, FileSystem fs) 
        
        private String[] parsePreCreateFiles(String preCreateFiles) {
        	return preCreateFiles.split(COMMA,-1);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public synchronized void write(PartitionOutputKey<K> key, V value) throws IOException {
        
        	if (key.isPartitioned()) {
        		
        		// partitioned output
        		String keyword = key.getKeyword();
            	TextOutputFormat.LineRecordWriter<Writable, V> curRW = recordWriters.get(keyword);
            	if (curRW==null) {  // create a new output committer
            		
            		// pre create file
            		partitionId = key.getPartitionID();
            		
        			FileOutputCommitter committer = outputCommitter.getCommitter(keyword);
            		if (committer==null) {
            			String fileName = keyword 
            					+ UNDERSCORE + NUMBER_FORMAT.format(key.getPartitionID())
            					+ UNDERSCORE + NUMBER_FORMAT.format(totalPartitions)
            					+ FILE_SUFFIX + outputExtension;
            			// create output path
            			Path outputPath = new Path(generateOutputDirectory(outputDirectoryExample, outputPathPattern, NUMBER_FORMAT.format(key.getPartitionID())), fileName);
            			
            			// create new file output committer
            			committer = new FlexibleFileOutputCommitter(new Path(generateOutputDirectory(outputDirectoryExample, outputPathPattern, NUMBER_FORMAT.format(key.getPartitionID()))), taskContext);
            			Path workFile = getDefaultWorkFile(committer, fileName);
            			// debug
            			System.err.println();
            			System.err.println();
            			System.err.println("fileName=" + fileName);
            			System.err.println("generateOutputDirectory=" + generateOutputDirectory(outputDirectoryExample, outputPathPattern, NUMBER_FORMAT.format(key.getPartitionID())));
            			System.err.println("partition-id=" + key.getPartitionID());
            			System.err.println("outputDirectoryExample=" + outputDirectoryExample);
            			System.err.println("outputPathPattern=" + outputPathPattern);
            			System.err.println("workFile: " + workFile);
            			// debug ends
            			FSDataOutputStream fileOut = workFile.getFileSystem(conf).create(workFile, false);
                        if (!isCompressed) {
                            curRW = new TextOutputFormat.LineRecordWriter<Writable, V>(fileOut, separator);
                        }
                        else {
                            curRW = new TextOutputFormat.LineRecordWriter<Writable, V>(new DataOutputStream(compressionCodec.createOutputStream(fileOut)), separator);
                        }
                        recordWriters.put(keyword, curRW);
                        outputCommitter.addCommitter(keyword, committer);
                        
                        System.err.println("Output path " + outputPath + " has been added.");
            		}
            		
            	}
            	
            	curRW.write(key.getNaturalKey(), value);
            	
        	} else { 
        		
        		// default output location
        		if (defaultRecordWriter==null) {
    				Path workFile = getWorkFileInDefaultDir(taskContext, outputCommitter, key.getPartitionID(), defaultOutputName, outputExtension);
    				
    				// debug
    				System.err.println("work file: " + workFile.toString());
    	            
        			FSDataOutputStream fileOut = workFile.getFileSystem(conf).create(workFile, false);
        			if (!isCompressed) {
        				defaultRecordWriter = new TextOutputFormat.LineRecordWriter<Writable, V>(fileOut, separator);
        			} else {
        				defaultRecordWriter = new TextOutputFormat.LineRecordWriter<Writable, V>(new DataOutputStream(compressionCodec.createOutputStream(fileOut)), separator);
        			}
    			}
    			
    			defaultRecordWriter.write(key.getNaturalKey(), value);
        	}
        	
        }	// public void write(PartitionOutputKey<K> key, V value) throws IOException
        
        private Path getWorkFileInDefaultDir(TaskAttemptContext context, FileOutputCommitter committer, int partitionIdInTask, String name, String extension) throws IOException {
        	return new Path(committer.getWorkPath(), getDefaultUniqFile(context, partitionIdInTask, name, extension));
        }
        
        private String getDefaultUniqFile(TaskAttemptContext context, int partitionIdInTask, String name, String extension) {
        	stringBuilder.append(name);
        	stringBuilder.append(DASH);
        	stringBuilder.append(context.getTaskAttemptID().getTaskID().isMap()?'m':'r');
        	stringBuilder.append(DASH);
        	stringBuilder.append(NUMBER_FORMAT.format(partitionIdInTask));
        	stringBuilder.append(extension);
        	return stringBuilder.toString(); 
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close(TaskAttemptContext context) throws IOException {
        	
        	String preCreateFiles = conf.get(CONFIG_PRE_CREATE_FILES, "").trim();
            if (preCreateFiles.length()>0) {
            	System.err.println("create 0-byte file(s) for " + preCreateFiles);
            	String[] filePrefixes = parsePreCreateFiles(preCreateFiles);
            	for (int i=0; i<filePrefixes.length; i++) {
            		TextOutputFormat.LineRecordWriter<Writable, V> rw = recordWriters.get(filePrefixes[i]);
            		if (rw==null) {
            			
            			FileOutputCommitter fileOutputCommitter = outputCommitter.getCommitter(filePrefixes[i]);
            			
                		if (fileOutputCommitter==null) {
                			
                			String filename = filePrefixes[i] 
                					+ UNDERSCORE + NUMBER_FORMAT.format(partitionId)
                					+ UNDERSCORE + NUMBER_FORMAT.format(totalPartitions)
                					+ FILE_SUFFIX + outputExtension;
                			
                			// create a file output committer with desired output path
                			fileOutputCommitter = new FlexibleFileOutputCommitter(new Path(generateOutputDirectory(outputDirectoryExample, outputPathPattern, NUMBER_FORMAT.format(partitionId))), context);
                			Path workFile = getDefaultWorkFile(fileOutputCommitter, filename);
                			FSDataOutputStream fileOut = workFile.getFileSystem(conf).create(workFile, false);
                            
                			if (!isCompressed) {
                                rw = new TextOutputFormat.LineRecordWriter<Writable, V>(fileOut, separator);
                            }
                            else {
                                rw = new TextOutputFormat.LineRecordWriter<Writable, V>(new DataOutputStream(compressionCodec.createOutputStream(fileOut)), separator);
                            }
                            
                            System.err.println("Adding pre-create record writer for " + filePrefixes[i]);
                            recordWriters.put(filePrefixes[i], rw);
                            outputCommitter.addCommitter(filePrefixes[i], fileOutputCommitter);
                		}
            		}
            	}
            	
            	// debug
            	System.err.println("Pre create files: ");
            	for (String k: recordWriters.keySet()) {
            		System.err.println(k+DEFAULT_DELIMITER+recordWriters.get(k).toString());
            	}
            } else {
            	System.err.println("No files to pre created.");
            }
        	
        	if (defaultRecordWriter!=null) {
        		defaultRecordWriter.close(context);
        	}
            for (TextOutputFormat.LineRecordWriter<Writable, V> rw: recordWriters.values()) {
            	rw.close(context);
            }
        }	// public void close(TaskAttemptContext context) throws IOException
        
    }	// protected static class WriteToLocalRecordWriter<K extends Writable, V> extends RecordWriter<PartitionOutputKey<K>, V>
    
}
