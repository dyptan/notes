#!/bin/bash

scriptPath=${0%/*}

source $scriptPath/set_env.sh
source $scriptPath/set_volume_infos.sh

BINARY_FILE=rsm-bigdata-elastic-indexer.jar

log4jPropertiesFile=$VOLUME_HOME/config/elastic-indexer-configuration.properties
configPropertiesFile=$VOLUME_HOME/config/elastic-indexer-log4j.properties

$MAPR_SCRIPTS_HOME/v1/user/login.sh -v $VOLUME_HOME

$MAPR_SCRIPTS_HOME/v1/spark/submit.sh -v $VOLUME_HOME -l elasticIndexer -j $BINARY_FILE -c de.rewe.rsm.bigdata.elastic.indexer.Driver -t forever -o "--conf spark.yarn.maxAppAttempts=2 --conf spark.yarn.am.attemptFailuresValidityInterval=1h --conf spark.yarn.dist.files=$log4jPropertiesFile,$configPropertiesFile --conf \"spark.driver.extraJavaOptions=-Dlog4j.configuration=elastic-indexer-log4j.properties\" --conf \"spark.executor.extraJavaOptions=-Dlog4j.configuration=elastic-indexer-log4j.properties\" --executor-memory 1G --num-executors 2 --executor-cores 3"
