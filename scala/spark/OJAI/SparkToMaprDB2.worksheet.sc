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



// [mapr@node2 ~]$ cat customer.json 
// {"_id":"000000000213.1","BuySell":"S","ClusterTimestamp":{"$date":"2011-11-14T01:24:23.381Z"}}
// {"_id":"000000000213.2","BuySell":"S","ClusterTimestamp":{"$date":"2011-11-14T06:22:44.278Z"}}

// MapR-DB Shell
// maprdb mapr:> find /tmp/customer
// {"_id":"000000000213.1","BuySell":"S","ClusterTimestamp":{"$date":"2011-11-14T01:24:23.381Z"}}
// {"_id":"000000000213.2","BuySell":"S","ClusterTimestamp":{"$date":"2011-11-14T06:22:44.278Z"}}
// 2 document(s) found.




