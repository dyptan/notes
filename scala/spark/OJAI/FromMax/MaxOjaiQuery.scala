case class Person(_id: String, firstName: String, lastName: String, date: String)

import com.mapr.db.spark.sql._

val peopleDf = spark.loadFromMapRDB[Person]("/apps/people")

import org.ojai.store.DriverManager
val connection = DriverManager.getConnection("ojai:mapr:")

val qs = "{\"$between\":{\"ts2\":[1000000000,1200000000]}}".format(r.getDoc.getId.getString)

val  query = connection.newQuery().select("_id").setOption(com.mapr.ojai.store.impl.OjaiOptions.OPTION_USE_INDEX, "mypeopleindex").where(qs).build()
      val iterator = store.find(query).iterator()
      if (iterator.hasNext) {
        dm += iterator.next().asJsonString()
      }


===========================

// import com.mapr.db.spark.sql._
import com.mapr.db.spark._
import org.ojai.store.QueryCondition
import org.ojai.types._
import com.mapr.db.MapRDB

MapRDB.createTable("/tmp/testTable")
val table = MapRDB.getTable("/tmp/testTable")
table.insertOrReplace(MapRDB.newDocument()
            .set("_id", "jdoe")
            .set("first_name", "John")
            .set("last_name", "Doe")
            .set("dob", ODate.parse("1970-06-23"))
      )
sc.loadFromMapRDB("/tmp/testTable").where(MapRDB.newCondition().is("_id", QueryCondition.Op.EQUAL, "jdoe").build())

val cond = MapRDB.newCondition().and().is("ts1",QueryCondition.Op.GREATER_OR_EQUAL,new OTimestamp(2019,1,1,0,0,0,0)).is("ts1", QueryCondition.Op.LESS_OR_EQUAL, new OTimestamp(2019,3,31,0,0,0,0)).close().build()



=============================================


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