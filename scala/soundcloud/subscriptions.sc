val subscriptions =
  s"""event_time, event, user_id
2023-01-12 06:14:20, subscription_purchased, 1
2023-01-24 18:37:11, subscription_purchased, 4
"""

val tail = subscriptions.split("\n").toList.drop(1)


case class Row(event_time: String, event: String, user_id: Int)


val listOfRows = tail.map(_.split(", ")).map(a => Row(a(0),a(1), a(2).toInt ))

val users = List(1,2)

def parseEvent(event: String): Boolean = {
  event.contentEquals("subscription_purchased")
}


listOfRows.map {
  case row if users.contains(row.user_id) => s"${row.user_id} subscribed| ${parseEvent(row.event)}"
  case row => s"${row.user_id} subscribed| false"
}.foreach(println)

