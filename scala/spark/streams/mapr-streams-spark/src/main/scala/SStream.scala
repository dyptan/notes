import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object SStream {

  val spark = SparkSession.builder().appName("test").master("local[1]").getOrCreate()


  def readFromES(args: Array[String]) = {
    val kafkaDF: DataFrame = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers","localhost:9092")
      // .option("kafka.group.id",args.lift(2).getOrElse("StructuredStreamingClient"))
      .option("kafka.startingoffsets",args.lift(1).getOrElse("earliest"))
      .option("subscribe", args.lift(0).getOrElse("/user/mapr/pump:topic0"))
      .load()

    kafkaDF
      .select(col("topic"), expr("cast(value as string) as actualValue"))
      .writeStream
      .format("console")
      .outputMode("append")
      .start()
      .awaitTermination()
  }

  def main(args: Array[String]) = {
    readFromES(args)
  }

}
