//yum install mapr-gateway
///opt/mapr/server/configure.sh -R

//[mapr@node4 ~]$ cat /opt/mapr/conf/mapr-clusters.conf 
//cyber.mapr.cluster secure=false node4.cluster.com:7222
//[mapr@node4 ~]$ maprcli cluster gateway set -dstcluster cyber.mapr.cluster -gateways localhost

// hadoop fs -put data-table.json /tmp

// mapr importJSON -idfield 'id' -mapreduce true -src /tmp/data-table.json -dst /tmp/data-table

// maprcli table index add -path /tmp/data-table1 -index ts1_idx -indexedfields ts1


---------------------------------------------------
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
            .is(args[1],
                    QueryCondition.Op.GREATER_OR_EQUAL,
                    new OTimestamp(2000,01,01,0,0,0,0))
            .is(args[1], QueryCondition.Op.LESS_OR_EQUAL, new OTimestamp(2019,03,31,0,0,0,0))
            .close().build();
        long count = maprSC.loadFromMapRDB(args[0]).where(tradeCondition).count();
        System.out.println("Count is: "+count);
    }
}

---------------------------------------------------
---------------------------------------------------
import com.mapr.db.MapRDB;
import org.apache.spark.SparkConf;
import org.ojai.types.OTimestamp;
import org.ojai.store.QueryCondition;
import com.mapr.db.spark._

sc.loadFromMapRDB("/tmp/data-table").where(MapRDB.newCondition()
.and()
.is("zoneddate",QueryCondition.Op.GREATER_OR_EQUAL,new OTimestamp(2000,1,1,0,0,0,0))
.is("zoneddate",QueryCondition.Op.LESS_OR_EQUAL,new OTimestamp(2019,3,31,0,0,0,0))
.close().build()
).count

---------------------------------------------------
---------------------------------------------------

[mapr@node2 ~]$ cat customer.json 
{"_id":"000000000213.1","BuySell":"S","ClusterTimestamp":{"$date":"2011-11-14T01:24:23.381Z"}}
{"_id":"000000000213.2","BuySell":"S","ClusterTimestamp":{"$date":"2011-11-14T06:22:44.278Z"}}

MapR-DB Shell
maprdb mapr:> find /tmp/customer
{"_id":"000000000213.1","BuySell":"S","ClusterTimestamp":{"$date":"2011-11-14T01:24:23.381Z"}}
{"_id":"000000000213.2","BuySell":"S","ClusterTimestamp":{"$date":"2011-11-14T06:22:44.278Z"}}
2 document(s) found.




