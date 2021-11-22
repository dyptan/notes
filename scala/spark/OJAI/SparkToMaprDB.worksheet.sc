// import com.mapr.db.spark.sql._
// val peopleDf = spark.loadFromMapRDB[Person]("/apps/people")

import com.mapr.db.spark._

sc.loadFromMapRDB("/tmp/data-table").where(MapRDB.newCondition().and()
.is("ts1",QueryCondition.Op.GREATER_OR_EQUAL,"1000000000")
.is("ts1",QueryCondition.Op.LESS_OR_EQUAL, "1200000000")
.close().build()
).count

sc.loadFromMapRDB("/tmp/test6").where(MapRDB.newCondition().and().is("zoneddate",QueryCondition.Op.GREATER_OR_EQUAL, new OTimestamp(2000,3,31,0,0,0,0))
.is("zoneddate",QueryCondition.Op.LESS_OR_EQUAL, new OTimestamp(2010,3,31,0,0,0,0))
.close().build()
).count

//find /tmp/test5 --limit 20 --where {"$and":[{"$gt":{"$date":"2005-03-31T00:00:00.000Z"}},{"$lt":{"$date":"2005-03-31T00:00:00.000Z"}}]}