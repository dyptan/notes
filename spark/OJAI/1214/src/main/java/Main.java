import com.mapr.db.MapRDB;
import com.mapr.db.spark.api.java.MapRDBJavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.ojai.store.QueryCondition;
import org.ojai.types.OTimestamp;


public class Main {

    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().setAppName("SOME APP NAME").setMaster("local[*]");
        SparkContext sc = new SparkContext(sparkConf).getOrCreate();
        MapRDBJavaSparkContext maprSC= new MapRDBJavaSparkContext(sc);

        QueryCondition tradeCondition = MapRDB.newCondition().and()
            .is(args[2],
                    QueryCondition.Op.GREATER_OR_EQUAL,
                    new OTimestamp(2018,01,01,0,0,0,0))
            .is(args[3], QueryCondition.Op.LESS_OR_EQUAL, new OTimestamp(2019,03,31,0,0,0,0))
            .close().build();

        long count = maprSC.loadFromMapRDB(args[0]).where(tradeCondition).select(args[1]).count();

        System.out.println("Count is: "+count);


    }


}
