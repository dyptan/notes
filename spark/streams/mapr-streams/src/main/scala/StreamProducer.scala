import org.apache.spark.sql.SparkSession
object StreamProducer {

    def main(args: Array[String]){
    val spark = SparkSession.builder().master("local[*]").appName("test").getOrCreate()
    printf(spark.version)
}
}
