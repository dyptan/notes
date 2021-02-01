//maprcli table index add -path /user/mapr/92499 -index date -indexedfields ts1
// maprcli table index list -path /user/mapr/92499 -json

import com.mapr.db.MapRDB;
import org.apache.spark.SparkConf;
import org.ojai.types.OTimestamp;
import org.ojai.store.QueryCondition;
import com.mapr.db.spark._
import org.apache.commons.lang3.time.StopWatch 
val cond1 = MapRDB.newCondition()
.and()
.is("ts1",QueryCondition.Op.GREATER_OR_EQUAL,"1000000000")
.is("ts1",QueryCondition.Op.LESS_OR_EQUAL,"1200000000")
.close().build()
val result1 = sc.setHintUsingIndex("time").loadFromMapRDB("/user/mapr/92499").where(cond1)
val stopWatch = StopWatch.createStarted()
result1.count
val end = stopWatch.getTime()
val cond2 = MapRDB.newCondition()
.and()
.is("ts2",QueryCondition.Op.GREATER_OR_EQUAL, 1000000000)
.is("ts2",QueryCondition.Op.LESS_OR_EQUAL, 1200000000)
.close().build()
val result2 = sc.loadFromMapRDB("/user/mapr/92499").where(cond2)
val stopWatch = StopWatch.createStarted()
result2.count
val end = stopWatch.getTime()
