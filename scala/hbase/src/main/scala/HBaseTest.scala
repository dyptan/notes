import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.spark.sql.SparkSession

object HBaseTest {


  def getHbaseConnection(): org.apache.hadoop.hbase.client.Connection = ConnectionFactory.createConnection()

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.appName("HbaseTest").getOrCreate()
    println(getHbaseConnection())
  }
}
