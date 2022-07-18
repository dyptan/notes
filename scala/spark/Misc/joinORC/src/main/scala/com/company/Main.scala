package com.company

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Main {

  def main(args: Array[String]) {
    val conf = new SparkConf()
    val spark = SparkSession.builder.config(conf).getOrCreate()

//    spark.read.option("header", "true").option("inferSchema", "true").csv("/tmp/1180/people1.csv")
//      .select("ID", "Last name")
//      .write.mode("overwrite")
//      .orc("/tmp/people/people1.orc")
//    val people0 = spark.read.orc("/tmp/people/people0.orc").select("ID", "last name").withColumnRenamed("last name", "ln")
//    val people1 = spark.read.orc("/tmp/people/people1.orc").select("ID", "first name", "age").withColumnRenamed("first name", "fn")
//    people0.join(people1, Seq("ID")).write.mode("overwrite").parquet("/tmp/people/joined")

    spark.sql("""select * from tmptestdr.test_engel""").show()

  }
}
