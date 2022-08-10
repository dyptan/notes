
import org.apache.spark.sql.types._
import org.apache.spark.sql._

val somerecords1 = Seq(Row(1, 1657817163),Row(2,1657817164))
val myRDD = sc.parallelize(somerecords1)
val mySchema = StructType(Seq(StructField("id",IntegerType,true), StructField("time",IntegerType,true)))
val myDF1 = spark.createDataFrame(myRDD, mySchema)

// Shorthand

val somerecords2 = Seq((1, "1999-01-01 00:00:00"),(2,"9999-01-01 00:00:00"))
val myDF2 = spark.createDataFrame(somerecords2).toDF("id","time")
val myDFtime = myDF.withColumn("time", to_timestamp(col("time")))

myDF.write.mode("overwrite").parquet("tmp/myfile.par")


// CREATE TABLE mytable (id Int, time Timestamp)
// STORED AS parquet
// LOCATION 'maprfs:///user/mapr/tmp/myfile.par';