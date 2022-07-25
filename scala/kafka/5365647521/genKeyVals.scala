val writer = new java.io.FileWriter("/tmp/out.txt")
val range = Range(1, 10000)
val keyVals = range.map(x => s"key-${x},val").mkString("\n")
writer.write(keyVals)
writer.flush()

//sh: maprcli stream create -path /user/mapr/ingenico -produceperm u:mapr -consumeperm u:mapr -topicperm u:mapr
//:sh cat /tmp/out.txt | /opt/mapr/kafka/kafka-1.1.1/bin/kafka-console-producer.sh --broker-list localhost:1234 --topic /user/mapr/ingenico:topic0 --property "parse.key=true" --property "key.separator=,"
