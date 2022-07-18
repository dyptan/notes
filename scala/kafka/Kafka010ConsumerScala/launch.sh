#!/bin/bash
java -cp '/opt/mapr/spark/spark-2.4.4/jars/*':`mapr classpath`:target/StreamsKafka10Consumer.jar -Dlog4j.configuration=file:conf/log4j.properties com.example.Kafka10Consumer "/user/mapr/pump:topic0"
