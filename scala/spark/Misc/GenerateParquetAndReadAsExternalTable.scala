
val somerecords = Seq(Row(1, 1657817163),Row(2,1657817164))
val myRDD = sc.parallelize(somerecords)

val mySchema = StructType(Seq(StructField("id",IntegerType,true), StructField("time",IntegerType,true)))

val myDF = spark.createDataFrame(myRDD, mySchema)

val myDFtime = myDF.withColumn("time", to_timestamp(col("time")))

myDFtime.write.mode("overwrite").parquet("tmp/myfile.par")
val readFile = spark.read.parquet("tmp/myfile.par")
readFile.show

spark-sql>
CREATE TABLE mytable (id Int, time Timestamp)
STORES AS parquet
LOCATION 'maprfs:///user/mapr/tmp/myfile.par';