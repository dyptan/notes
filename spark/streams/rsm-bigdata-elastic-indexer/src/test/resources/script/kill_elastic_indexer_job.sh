#!/bin/bash

scriptPath=${0%/*}

source $scriptPath/set_env.sh
source $scriptPath/set_volume_infos.sh

${MAPR_SCRIPTS_HOME}/v1/user/login.sh --volume_home $VOLUME_HOME

$MAPR_SCRIPTS_HOME/v1/spark/kill.sh -v $VOLUME_HOME -l elasticIndexer
