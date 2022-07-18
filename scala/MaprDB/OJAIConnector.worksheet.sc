import org.ojai.store.DriverManager
val connection = DriverManager.getConnection("ojai:mapr:")

val qs = "{\"$between\":{\"ts2\":[1000000000,1200000000]}}".format(r.getDoc.getId.getString)

val  query = connection.newQuery().select("_id").setOption(com.mapr.ojai.store.impl.OjaiOptions.OPTION_USE_INDEX, "mypeopleindex").where(qs).build()
      val iterator = store.find(query).iterator()
      if (iterator.hasNext) {
        dm += iterator.next().asJsonString()
      }

