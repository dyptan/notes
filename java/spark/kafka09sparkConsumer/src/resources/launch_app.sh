o#!/bin/bash

##Filling both topics :
#cat messages_topic1.msg | /opt/mapr/kafka/kafka-1.0.1/bin/kafka-console-producer.sh --broker-list "10.108.99.230:9092,10.108.99.232:9092,10.108.99.234:9092" --topic cdc_drx.b2bvirh1.sourcedb.vidh001.dbrh.vita074-dev-json
#cat messages_topic2.msg | /opt/mapr/kafka/kafka-1.0.1/bin/kafka-console-producer.sh --broker-list "10.108.99.230:9092,10.108.99.232:9092,10.108.99.234:9092" --topic cdc_drx.b2bvirh1.sourcedb.vidh001.dbrh.vita078-dev-json

# Checking group in Kafka 
#/opt/mapr/kafka/kafka-1.0.1/bin/kafka-consumer-groups.sh --bootstrap-server 10.108.99.230:9092,10.108.99.232:9092,10.108.99.234:9092 --group test6 --describe

#TOPIC and Brokers
#cdc_drx.b2bvirh1.sourcedb.vidh001.dbrh.vita074-dev-json
#cdc_drx.b2bvirh1.sourcedb.vidh001.dbrh.vita078-dev-json
#10.108.99.230:9092,10.108.99.232:9092,10.108.99.234:9092
## FUNCTIONS
# Affiche l'usage
usage () {
	    echo "USAGE :"
		echo "-group		: consumer group id"
		echo "-topic1		: first topic name to read"
		echo "-topic2		: second topic name to read"
		echo "-pollms		: parameter for setting the spark streaming property - spark.streaming.kafka.consumer.driver.poll.ms"
		echo "*-mode		: (optionnal) deploy mode (client or cluster) for the spark streaming app. default : cluster"		
		echo "*-brokers     : (optionnal) kafka brokers list [if reading from kafka]. default : empty"
		echo "*-nbworker    : (optionnal) number of spark executors. default : 2"		
	    echo "-h : show help"
}

# getting folder of script
scriptpath="$( cd  "$(dirname "$0")" ; pwd -P )"

# checking if there is at least one argument
[ "$#" = "0" ] && usage && exit

# Parsing arguments
while [ "$1" != "" ]; do
    case $1 in
        -group)   	shift
					group=$1
					;;
        -topic1)   	shift
					topic1=$1
					;;
        -topic2)   	shift
					topic2=$1
					;;
        -pollms)   	shift
					pollms=$1
					;;
        -mode)   	shift
					mode=$1
					;;					
        -brokers)   shift
					brokers=$1
					;;
        -nbworker) 	shift
					nbworker=$1
					;;							
        -h)     	usage
					exit 1
					;;
        * )     	usage
					exit 1
					;;
    esac
    shift
done

# Execution
exec () {
	export SPARK_MAJOR_VERSION=2
	
    #Spark application name
    application_name="credit-agricol-sparkstreaming"
	
	# Spark Options
	SparkOptions="--class streamingapp.SparkStreamingSubsAllConsumer --num-executors ${nbworker} --master yarn --deploy-mode ${mode} --conf spark.driver.memory=2g --conf spark.executor.memory=2g --conf spark.yarn.submit.waitAppCompletion=false"
	
	# JarWD
	JarWD=${scriptpath}/streamingapp-0.0.1-SNAPSHOT-jar-with-dependencies.jar
	
	/opt/mapr/spark/spark-2.2.1/bin/spark-submit ${SparkOptions} --name ${application_name} ${JarWD} ${group} ${pollms} ${topic1} ${topic2} ${brokers}
	exit $?;
	
}

if [[ "${group}" == "" ]]; then
	usage
	exit 1
fi

if [[ "${topic1}" == "" ]]; then
	usage
	exit 1
fi

if [[ "${pollms}" == "" ]]; then
	usage
	exit 1
fi

if [[ "${mode}" == "" ]]; then
	mode="cluster"
fi

if [[ "${nbworker}" == "" ]]; then
	nbworker=2
fi

exec

# This exit is never reached.
exit 99          
