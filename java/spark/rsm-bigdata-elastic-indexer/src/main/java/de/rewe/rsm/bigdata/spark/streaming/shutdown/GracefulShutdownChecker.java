package de.rewe.rsm.bigdata.spark.streaming.shutdown;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to shutdown the Spark-Streaming context through the availability of a file. If a shutdown file is found, the streaming context will be
 * terminated. After termination the file is deleted and a new, empty file with the new suffix ".shutdown.SUCCESS" is created.
 */
public class GracefulShutdownChecker {

	private static final Logger LOGGER = LoggerFactory.getLogger(GracefulShutdownChecker.class);

	private static final long FILE_CHECK_INTERVAL_DEFAULT = TimeUnit.SECONDS.toMillis(60);

	private static final String TRIGGER_SUFFIX = ".shutdown";
	private static final String SUCCESS_SUFFIX = ".shutdown.SUCCESS";

	private String triggerFilePrefix;
	private final Path triggerFilePath;
	private final Path successFilePath;

	private final JavaStreamingContext ssc;

	/**
	 * Creates a new instance
	 *
	 * @param ssc
	 *            Streaming-Context, which will be terminated when the shutdown file is found
	 * @param triggerDirectory
	 *            the directory to look for the trigger file
	 * @param triggerFilePrefix
	 *            the trigger file prefix
	 */
	public GracefulShutdownChecker(JavaStreamingContext ssc, String triggerDirectory, String triggerFilePrefix) {
		this.ssc = ssc;
		this.triggerFilePrefix = triggerFilePrefix;

		this.triggerFilePath = new Path(triggerDirectory + Path.SEPARATOR + this.triggerFilePrefix + TRIGGER_SUFFIX);
		this.successFilePath = new Path(triggerDirectory + Path.SEPARATOR + this.triggerFilePrefix + SUCCESS_SUFFIX);

		LOGGER.info("Create file '" + this.triggerFilePath.toString() + "' in order to shutdown the application");
	}

	/**
	 * Check if the shutdown file exists within the specified folder and if so, shuts down the streaming context
	 */
	public void shutdownContextIfNeeded() {
		try {
			FileSystem fs = this.triggerFilePath.getFileSystem(new Configuration());
			if (fs.exists(this.triggerFilePath)) {
				LOGGER.info("Shutdown trigger for '" + this.triggerFilePrefix + "' found. Shutting down application...");

				try {
					/*
					 * Shutdown of the application using false, true. This is because after shutting down the streaming context the shutdown file will
					 * be removed and a new file will be written so access to the file system is needed.
					 */
					this.ssc.stop(false, true);
				} catch (Exception e) {
					LOGGER.error("Error while stopping spark streaming context", e);
				}

				LOGGER.info("Streaming application shutdown successfully. Deleting trigger file and creating success file...");

				// delete trigger file
				fs.delete(this.triggerFilePath, false);

				// write success file
				fs.createNewFile(this.successFilePath);

				LOGGER.info("Trigger directory updated.");
			}
		} catch (IOException e) {
			LOGGER.error("Error while reading/writing trigger files", e);
		}
	}

	/**
	 * Waits for termination of the context using {@link GracefulShutdownChecker#awaitTermination(long)} and the default check interval
	 * {@link GracefulShutdownChecker#FILE_CHECK_INTERVAL_DEFAULT}. The termination could be requested using a trigger file or through other evens
	 * like getting killed by Yarn.
	 */
	public void awaitTermination() {
		this.awaitTermination(FILE_CHECK_INTERVAL_DEFAULT);
	}

	/**
	 * Wait for termination of the context. The termination could be requested using a trigger file or through other evens like getting killed by
	 * Yarn.
	 *
	 * @param fileCheckInterval
	 *            the interval to check for the trigger file in milliseconds
	 */
	public void awaitTermination(long fileCheckInterval) {
		boolean isStopped = false;
		while (!isStopped) {
			try {
				isStopped = this.ssc.awaitTerminationOrTimeout(fileCheckInterval);
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage());
				//TODO 
				throw new RuntimeException();
			}
			if (!isStopped) {
				this.shutdownContextIfNeeded();
			}
		}
	}

}
