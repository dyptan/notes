package com.comscore.data.processing

import java.io.Serializable
import java.text.NumberFormat

import com.beust.jcommander.{JCommander, Parameter, ParameterException}
import com.comscore.adeffx.oreo.utils.GeneralPartitioningUtils
import com.comscore.data.io.SerializableText
import com.comscore.hadoop.mapreduce.lib.output.{PartitionOutputKeyPair, PartitionWriteToLocalOutputFormat}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{Text, WritableComparator}
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}
import org.apache.spark.sql.{Encoders, SparkSession}
import org.apache.hadoop.mapreduce.{Job => NewAPIHadoopJob, OutputFormat => NewOutputFormat}

object MaprTestClass {

  sealed class CommandLineArgs extends Serializable {

    @Parameter(names = Array("-input"), required = true, description = "input")
    var beaconInput:String = null
    @Parameter(names = Array("-output_prefix"), required = true, description = "output file prefix")
    var outputPrefix:String = null
    @Parameter(names = Array("-output"), required = true, description = "part 0 path of the partitioned output")
    var output:String = null

    // optional params
    @Parameter(names = Array("-partitions"), description = "number of partitions. defaults to 512")
    var numPartitions:Int = 512
    @Parameter(names = Array("-partition_pattern"), description = "regex to match partition id. defaults to \"part_\\d{5})\"")
    var partitionPattern:String = "part_(\\d{5})"

  }

  case class InputCase (
                       Number1:Int,
                       uid:String,
                       unused1:String,
                       unused2:String,
                       Number2:Int
                       )

  def main(args:Array[String]):Unit = {
    val cmdArgs = new CommandLineArgs
    var jCommander: JCommander = null
    try {
      jCommander = new JCommander(cmdArgs, args:_*)
    } catch {
      case e: ParameterException => {
        System.err.println(e.getMessage)
        jCommander.usage
        System.exit(1)
      }
    }
    val conf = new SparkConf().setAppName("")
    val sc = new SparkContext(conf)

    val ss: SparkSession = SparkSession.builder.getOrCreate()
    val fs = FileSystem.get(sc.hadoopConfiguration)
    fs.delete(new Path(cmdArgs.output), true)

    val input = ss.read.option("header", false)
      .option("delimiter", "\t")
      //.option("mode", "FAILFAST")
      .option("mode", "DROPMALFORMED")
      .option("nullValue", "")
      .schema(Encoders.product[InputCase].schema)
      .csv(cmdArgs.beaconInput)

    import ss.implicits._
    val output = input.as[InputCase].map(x => x.productIterator.mkString("\t")).rdd

    write(sc, output, cmdArgs, 1)
  }

  def write(sc:SparkContext, rdd:RDD[String], cmdArgs:CommandLineArgs, uidIndex:Int): Unit = {
    val hadoopConf = sc.hadoopConfiguration
    val format: NumberFormat = NumberFormat.getInstance
    format.setMinimumIntegerDigits(5)
    format.setGroupingUsed(false)

    // set configuration variables needed by Partitioned output
    hadoopConf.setInt(PartitionWriteToLocalOutputFormat.CONFIG_TOTAL_PARTITIONS, cmdArgs.numPartitions)
    hadoopConf.set(PartitionWriteToLocalOutputFormat.CONFIG_OUTPUT_DIRECTORY_PATH, cmdArgs.output)
    hadoopConf.set(PartitionWriteToLocalOutputFormat.CONFIG_OUTPUT_DIRECTORY_PATTERN, cmdArgs.partitionPattern)
    hadoopConf.set(PartitionWriteToLocalOutputFormat.CONFIG_PRE_CREATE_FILES, preCreateFiles(cmdArgs.outputPrefix))

    val toOutput = rdd
      .map(record => (record.split("\t")(uidIndex), record))
      .repartitionAndSortWithinPartitions(new PsuedoTextPartitioner(cmdArgs.numPartitions))
      .mapPartitions(records => {
        for((uid, record) <- records) yield {
          val tokens = record.split("\t", -1)
          val outputKey = new PartitionOutputKeyPair
          outputKey.setNaturalKey(tokens(0))
          outputKey.setKeyword(tokens.last.split("_").head + "/" + cmdArgs.outputPrefix)
          outputKey.setPartitionID(GeneralPartitioningUtils.getPartitionIdFromUid(new SerializableText(uid), cmdArgs.numPartitions))
          outputKey -> new SerializableText(tokens.drop(1).mkString("\t"))
        }
      }, preservesPartitioning = true)

    customSaveAsNewAPIHadoopFile(toOutput, cmdArgs.output, classOf[PartitionOutputKeyPair], classOf[SerializableText],
        classOf[PartitionWriteToLocalOutputFormat[PartitionOutputKeyPair, SerializableText]], hadoopConf)
  }
  def customSaveAsNewAPIHadoopFile(
                                    inputRDD: RDD[(PartitionOutputKeyPair, SerializableText)],
                                    path: String,
                                    keyClass: Class[_],
                                    valueClass: Class[_],
                                    outputFormatClass: Class[_ <: NewOutputFormat[_, _]],
                                    conf: Configuration): Unit = {

    // Rename this as hadoopConf internally to avoid shadowing (see SPARK-2038).
    val hadoopConf = conf
    val job = NewAPIHadoopJob.getInstance(hadoopConf)
    job.setOutputKeyClass(keyClass)
    job.setOutputValueClass(valueClass)
    job.setOutputFormatClass(outputFormatClass)
    val jobConfiguration = job.getConfiguration
    jobConfiguration.set("mapred.output.dir", path)
    jobConfiguration.set("mapreduce.output.fileoutputformat.outputdir", path)
//    print(jobConfiguration.getInt("mapreduce.fileoutputcommitter.algorithm.version", 0))
//    jobConfiguration.setInt("mapreduce.fileoutputcommitter.algorithm.version", 1)
//    print(jobConfiguration.getInt("mapreduce.fileoutputcommitter.algorithm.version", 0))
    inputRDD.saveAsNewAPIHadoopDataset(jobConfiguration)
  }


  class PsuedoTextPartitioner[K,V](partitions: Int) extends Partitioner {
    override def numPartitions: Int = partitions
    override def getPartition(key: Any): Int = {
      key match {
        case t:String => {
          val bb = Text.encode(t.toString)
          (WritableComparator.hashBytes(bb.array, bb.limit) & java.lang.Integer.MAX_VALUE) % partitions
        }
        case _ => throw new UnsupportedOperationException(s"Argument class: ${key.getClass} unsupported.")
      }
    }
  }

  private def preCreateFiles(outputPrefix:String): String = {
    (for {
      platform <- Seq("app", "browser", "pc")
    } yield s"$platform/$outputPrefix").mkString(",")
  }
}
