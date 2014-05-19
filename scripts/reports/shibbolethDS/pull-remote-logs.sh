#!/bin/bash

# to be invoked from cron

# get filename: /opt/shibboleth-ds/logs/discovery-2011-08-31.log
# note: this script will be invoked from logrotate and the timestamp will be TODAY

TODAY=`date +%Y%m%d`
DIR_BASE=`dirname $0`

PARSER_OPTIONS=""
if [ "$1" == "--dry-run" ] ; then
   PARSER_OPTIONS="--dry-run"
   shift
fi

# LOCAL DEV: # LOGS_BASE="$DIR_BASE/remote-logs"
LOGS_BASE="$DIR_BASE/../remote-logs"

if [ ! -e $LOGS_BASE ] ; then mkdir $LOGS_BASE ; fi

if [ ! -d $LOGS_BASE ] ; then
   echo "ERROR: remote logs directory $LOGS_BASE could not have been created"
   exit 2
fi
PULL_LOG="$LOGS_BASE/pull-remote-logs.log"

#expecing format (space delimited)
# LOG-ID TYPE URL_BASE [USERNAME PASSWORD]

cat $DIR_BASE/pull-remote-logs.conf | while read LOG_ID REMOTE_DS_HOST LOG_TYPE URL_BASE USERNAME PASSWORD ; do
    echo "`date` $0: processing logs $LOG_ID (URL_BASE $URL_BASE)" >> $PULL_LOG
    if [ "$LOG_TYPE" == "AAF-DS" ] ; then 
        LOG_PARSER="parse-aafds-logs.pl"
    elif [ "$LOG_TYPE" == "Tuakiri-DS" ] ; then
        LOG_PARSER="parse-ds-logs.pl"
    else
        echo "ERROR: Unknown LOG_TYPE=$LOG_TYPE"
        exit 2
    fi

    LOCAL_LOGS_DIR="$LOGS_BASE/$LOG_ID"
    if [ ! -e $LOCAL_LOGS_DIR ] ; then mkdir $LOCAL_LOGS_DIR ; fi

    if [ ! -d $LOCAL_LOGS_DIR ] ; then
       echo "ERROR: remote logs directory $LOCAL_LOGS_DIR could not have been created"
       exit 2
    fi

    for REMOTE_LOG_FILE in $( wget --quiet -O - $URL_BASE/ --user=$USERNAME --password=$PASSWORD 2>> $PULL_LOG | perl -e ' 
        while (<>) { 
            while ( /<a href="([^\/?][^"]*)">(.*$)/ ) { 
                $filename=$1 ; 
                $rest=$2; 
                if ( $filename =~ /^[-a-zA-Z0-9_:\.]*$/ ) { print "$filename\n" ; } ; 
                $_=$rest ; 
	    }; 
	};' ) ; do
        LOCAL_LOG_FILE="$LOCAL_LOGS_DIR/$REMOTE_LOG_FILE"
        if [ ! -f "$LOCAL_LOG_FILE" ] ; then
	    echo "`date` $0: fetching $URL_BASE/$REMOTE_LOG_FILE into $LOCALFILE_TODAY" >> $PULL_LOG
	    wget --quiet -O $LOCAL_LOG_FILE --user=$USERNAME --password=$PASSWORD $URL_BASE/$REMOTE_LOG_FILE >> $PULL_LOG 2>&1

	    echo "`date` $0: invoking $DIR_BASE/$LOG_PARSER $PARSER_OPTIONS --ds-host $REMOTE_DS_HOST $LOCAL_LOG_FILE" >> $PULL_LOG
	    $DIR_BASE/$LOG_PARSER $PARSER_OPTIONS --ds-host $REMOTE_DS_HOST $LOCAL_LOG_FILE >> $PULL_LOG 2>&1
        fi
    done
    echo "Done processing logs $LOG_ID (URL_BASE $URL_BASE)" >> $PULL_LOG
done


