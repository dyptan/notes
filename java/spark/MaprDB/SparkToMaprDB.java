//yum install mapr-gateway
///opt/mapr/server/configure.sh -R

//[mapr@node4 ~]$ cat /opt/mapr/conf/mapr-clusters.conf 
//cyber.mapr.cluster secure=false node4.cluster.com:7222
//[mapr@node4 ~]$ maprcli cluster gateway set -dstcluster cyber.mapr.cluster -gateways localhost

// hadoop fs -put data-table.json /tmp

// mapr importJSON -idfield 'id' -mapreduce true -src /tmp/data-table.json -dst /tmp/data-table

// maprcli table index add -path /tmp/data-table1 -index ts1_idx -indexedfields ts1


import com.mapr.db.MapRDB;
import com.mapr.db.spark.api.java.MapRDBJavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.ojai.store.QueryCondition;
import org.ojai.types.OTimestamp;
public class SparkToMaprDB {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setAppName("SOME APP NAME").setMaster("local[*]");
        SparkContext sc = new SparkContext(sparkConf).getOrCreate();
        MapRDBJavaSparkContext maprSC= new MapRDBJavaSparkContext(sc);
        QueryCondition tradeCondition = MapRDB.newCondition().and()
            .is(args[1],
                    QueryCondition.Op.GREATER_OR_EQUAL,
                    new OTimestamp(2000,01,01,0,0,0,0))
            .is(args[1], QueryCondition.Op.LESS_OR_EQUAL, new OTimestamp(2019,03,31,0,0,0,0))
            .close().build();
        long count = maprSC.loadFromMapRDB(args[0]).where(tradeCondition).count();
        System.out.println("Count is: "+count);
    }
}