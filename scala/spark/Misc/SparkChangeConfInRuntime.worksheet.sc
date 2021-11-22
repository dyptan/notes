import org.apache.log4j.{Logger, PropertyConfigurator}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.util.Random

object extract_cdc_main {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setMaster("yarn")
      .setAppName("test")
      .set("spark.sql.warehouse.dir", "/user/hive/warehouse")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrationRequired", "true")
      .set("spark.driver.memory","20g")
      .set("spark.driver.cores","2")
      .set("spark.executor.instances","50")
      .set("spark.executor.cores","5")
      .set("spark.executor.memory","6g")

    //Create the spark session
    val spark: SparkSession = SparkSession
      .builder()
      .config(conf)
      .enableHiveSupport()
      .getOrCreate()

    println("[INFO] Application id : "+spark.sparkContext.applicationId)
    println("Spark session 1")
    println(spark.sparkContext.getConf.toDebugString)

    val df_1 = spark.sparkContext.parallelize(Seq.fill(5000000){(Random.nextInt(4), Random.nextInt(4), Random.nextInt(4))}, 10)
    val llc : DataFrame = spark.createDataFrame(df_1).toDF("key","c1","c2")

    llc.sort("key")


    //2nd Process
    val executor_list = 1 to 50 map(x => x.toString)
    spark.sparkContext.killExecutors(executor_list)

    val conf2: SparkConf = new SparkConf()
      .setMaster("yarn")
      .setAppName("test2")
      .set("spark.sql.warehouse.dir", "/user/hive/warehouse")

    println("Executors : "+ spark.conf.get("spark.executor.instances"))
    println("Executor Cores : "+ spark.conf.get("spark.executor.cores"))

    SparkSession.clearActiveSession()
    SparkSession.clearDefaultSession()

    conf2.set("spark.driver.memory","20g")
      .set("spark.driver.cores","5")
      .set("spark.executor.cores","1")
      .set("spark.executor.instances", "10")

    //Create the spark session
    val spark2: SparkSession = SparkSession
      .builder()
      .config(conf2)
      .enableHiveSupport()
      .getOrCreate()

    SparkSession.setActiveSession(spark2)
    SparkSession.setDefaultSession(spark2)
    println("Spark Session 2")
    println(spark2.sparkContext.getConf.toDebugString)
    println("Executor Cores : "+ spark2.conf.get("spark.executor.cores"))
    println("Executors : "+ spark2.conf.get("spark.executor.instances"))
    spark2.sparkContext.requestExecutors(10)

    val df_2 = spark.sparkContext.parallelize(Seq.fill(5000000){(Random.nextInt(4), Random.nextInt(4), Random.nextInt(4))}, 10)
    val source :DataFrame = spark.createDataFrame(df_2).toDF("key","c1","c2")
    source.sort("key")

    spark2.close()
  }
}
