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

val cond = MapRDB.newCondition().and()
    .is("ts1",QueryCondition.Op.GREATER_OR_EQUAL,new OTimestamp(2019,1,1,0,0,0,0))
    .is("ts1", QueryCondition.Op.LESS_OR_EQUAL, new OTimestamp(2019,3,31,0,0,0,0)).close().build()