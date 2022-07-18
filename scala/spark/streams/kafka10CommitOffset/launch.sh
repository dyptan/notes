#!/bin/bash
/opt/mapr/spark/spark-2.4.4/bin/spark-submit --master yarn --deploy-mode cluster --class DStreamProvided --properties-file conf/spark-defaults.conf target/scala-2.11/stream-mapr-spark_2.11-1.0.jar "/user/mapr/pump:topic0"
