#!/bin/bash

# to be invoked from cron

# get filename: /opt/shibboleth-ds/logs/discovery-2011-08-31.log
# note: this script will be invoked from logrotate and the timestamp will be TODAY

TODAY=`date +%Y%m%d`
DIR_BASE=`dirname $0`
AAFDS_LOGS="/var/log/aafds"
PULL_LOG="$AAFDS_LOGS/pull-aafds.log"

PARSER_OPTIONS=""
if [ "$1" == "--dry-run" ] ; then
   PARSER_OPTIONS="--dry-run"
   shift
fi

cat $DIR_BASE/pull-aafds.conf | while read URL USERNAME PASSWORD LOCALFILE ; do
    URL_TODAY="$URL-$TODAY"
    LOCALFILE_TODAY="$LOCALFILE-$TODAY"

    echo "`date` $0: fetching $URL_TODAY into $LOCALFILE_TODAY" >> $PULL_LOG
    wget --quiet -O $LOCALFILE_TODAY --user=$USERNAME --password=$PASSWORD $URL_TODAY >> $PULL_LOG 2>&1
    if [ $? -ne 0 ] ; then
         echo "`date` $0: ERROR: fetching $URL_TODAY failed, deleting local file $LOCALFILE_TODAY" >> $PULL_LOG
         rm $LOCALFILE_TODAY
         continue
    fi
    echo "`date` $0: invoking $DIR_BASE/parse-aafds-logs.pl $PARSER_OPTIONS $LOCALFILE_TODAY" >> $PULL_LOG
    $DIR_BASE/parse-aafds-logs.pl $PARSER_OPTIONS $LOCALFILE_TODAY >> $PULL_LOG 2>&1
    if [ $? -ne 0 ] ; then
         echo "`date` $0: ERROR: parsing $LOCALFILE_TODAY failed" >> $PULL_LOG
         continue
    fi
done


