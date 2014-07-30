#!/bin/bash

# to be invoked from cron

# get filename: /opt/shibboleth-ds/logs/discovery-2011-08-31.log
# note: this script will be invoked from logrotate and the timestamp will be TODAY

TODAY=`date +%Y%m%d`
AAFDS_LOGS=/var/log/aafds

AAFDS_TODAY_LOG="$AAFDS_LOGS/wayf.log-$TODAY"

PUSH_LOG="$AAFDS_LOGS/push-aafds.log"

DIR_BASE=`dirname $0`
echo "`date` $0: invoking $DIR_BASE/parse-aafds-logs.pl $AAFDS_TODAY_LOG" >> $PUSH_LOG
$DIR_BASE/parse-aafds-logs.pl $AAFDS_TODAY_LOG >> $PUSH_LOG 2>&1

