#!/bin/bash

# to be invoked from cron

# get filename: /opt/shibboleth-ds/logs/discovery-2011-08-31.log
# note: this script will be invoked from logrotate and the timestamp will be TODAY

TODAY=`date +%Y%m%d`
DIR_BASE=`dirname $0`
DRY_RUN=""

# read config file
# must set the following values
# ERRMAILTO      - email address to send error notifications to
# MAILFROM       - email address to use as sender in the notifications
# PARSER_OPTIONS - any options to pass to parser scripts
# WGET_OPTIONS   - any options to pass to wget (on all invocations)
# LOGS_BASE      - directory where to store remote logs
# LOG_SOURCE_IDS - define the list of sources (their IDs)
# define each source as 
# LOG_SOURCE_<id>="<REMOTE_DS_HOST> <LOG_TYPE> <URL_BASE> <USERNAME PASSWORD>"
# optionally define local WGET options for the source
# WGET_OPTIONS_<id>="--no-check-certificate"
. $DIR_BASE/pull-remote-logs.conf


# parse (minimum) options supported
if [ "$1" == "--dry-run" ] ; then
   PARSER_OPTIONS="$PARSER_OPTIONS --dry-run"
   DRY_RUN="--dry-run"
   shift
fi


# Local function to send an email message
# Arguments:
# 1: Subject line
# 2: Email message body (single line)
# 3... Recipient email addresses
# Environment: MAILFROM (sender address)

function send_email_message {
    SUBJECT="$1"
    shift
    EMAILTEXT="$1"
    shift
    while [ $# -ge 1 ] ; do
        { 
           echo "From: $MAILFROM"
           echo "To: $1"
           echo "Subject: $SUBJECT"
           echo
           echo $EMAILTEXT
        } | /usr/sbin/sendmail -f "$MAILFROM" "$1"
        shift
    done
}

# Local function to report an error 
# Arguments:
# 1. Error message
function report_error {
  ERR_MSG="$1"
  shift

  # write the error to stderr
  echo "ERROR: $ERR_MSG" >&2
  # write the error to the log file
  if [ -n "$PULL_LOG" ] ; then
      echo "`date` $0: ERROR: $ERR_MSG" >> $PULL_LOG
  fi
  # send an email
  send_email_message "Error report: $0 on `hostname`" "$ERR_MSG" $ERRMAILTO
}

# Local function to abort with an error 
# Arguments:
# 1. Error message
function abort_with_error {
  ERR_MSG="$1"
  shift

  report_error "$1"
  exit 2
}




if [ ! -e $LOGS_BASE ] ; then mkdir $LOGS_BASE ; fi

if [ ! -d $LOGS_BASE ] ; then
   abort_with_error "ERROR: remote logs directory $LOGS_BASE could not have been created"
fi
PULL_LOG="$LOGS_BASE/pull-remote-logs.log"

#expecing format (space delimited)
# LOG-ID TYPE URL_BASE [USERNAME PASSWORD]

for LOG_ID in $LOG_SOURCE_IDS ; do
    eval "echo \$LOG_SOURCE_$LOG_ID" | { read REMOTE_DS_HOST LOG_TYPE URL_BASE USERNAME PASSWORD
	echo "`date` $0: Processing logs $LOG_ID (URL_BASE $URL_BASE)" >> $PULL_LOG
	if [ "$LOG_TYPE" == "AAF-DS" ] ; then 
	    LOG_PARSER="parse-aafds-logs.pl"
	elif [ "$LOG_TYPE" == "Tuakiri-DS" ] ; then
	    LOG_PARSER="parse-ds-logs.pl"
	else
	    report_error "ERROR: Unknown LOG_TYPE=$LOG_TYPE (LOG_ID $LOG_ID)"
            continue
	fi

	LOCAL_LOGS_DIR="$LOGS_BASE/$LOG_ID"
	if [ ! -e $LOCAL_LOGS_DIR ] ; then mkdir $LOCAL_LOGS_DIR ; fi

	if [ ! -d $LOCAL_LOGS_DIR ] ; then
	   report_error "ERROR: remote logs directory $LOCAL_LOGS_DIR could not have been created"
           continue
	fi

        WGET_OPTIONS_LOCAL="$( eval echo "\${WGET_OPTIONS_$LOG_ID}" )"
        INDEX_FILE=`mktemp`
        wget $WGET_OPTIONS $WGET_OPTIONS_LOCAL -O "$INDEX_FILE" $URL_BASE/ --user=$USERNAME --password=$PASSWORD >> $PULL_LOG  2>&1
        WGET_RES=$?
	if [ $WGET_RES -ne 0 ] ; then
	     report_error "fetching $URL_BASE failed ($WGET_RES)"
	     rm "$INDEX_FILE"
	     continue
	fi
	for REMOTE_LOG_FILE in $( cat "$INDEX_FILE" | perl -e ' 
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
		echo "`date` $0: fetching $URL_BASE/$REMOTE_LOG_FILE into $LOCAL_LOG_FILE" >> $PULL_LOG
		wget $WGET_OPTIONS $WGET_OPTIONS_LOCAL -O "$LOCAL_LOG_FILE" --user=$USERNAME --password=$PASSWORD "$URL_BASE/$REMOTE_LOG_FILE" >> $PULL_LOG 2>&1
                WGET_RES=$?
		if [ $WGET_RES -ne 0 ] ; then
		    report_error "fetching $URL_TODAY failed ($WGET_RES), deleting local file $LOCALFILE_TODAY"
                    rm "$LOCAL_LOG_FILE"
		    continue
		fi

		echo "`date` $0: invoking $DIR_BASE/$LOG_PARSER $PARSER_OPTIONS --ds-host $REMOTE_DS_HOST $LOCAL_LOG_FILE" >> $PULL_LOG
		$DIR_BASE/$LOG_PARSER $PARSER_OPTIONS --ds-host $REMOTE_DS_HOST $LOCAL_LOG_FILE >> $PULL_LOG 2>&1
                PARSER_RES=$?

                # delete downloaded files if either DRY_RUN or if parser failed
                if [ $PARSER_RES -ne 0 -o -n "$DRY_RUN" ] ; then
		    if [ $PARSER_RES -ne 0 ] ; then
			report_error "invoking parser  $DIR_BASE/$LOG_PARSER $PARSER_OPTIONS --ds-host $REMOTE_DS_HOST $LOCAL_LOG_FILE failed ($PARSER_RES), deleting local file $LOCAL_LOG_FILE"
		    else # [ -n "$DRY_RUN" ] 
		        echo "`date` $0: running in dry run mode, deleting local file $LOCAL_LOG_FILE" >> $PULL_LOG
                    fi
                    rm "$LOCAL_LOG_FILE"
		    if [ $PARSER_RES -ne 0 ] ; then
			continue # next loop - not strictly needed but skip any
                                 # code that might appear at the end of this loop.
		    fi
                fi
	    fi
	done
        rm "$INDEX_FILE"
	echo "`date` $0: Done processing logs $LOG_ID (URL_BASE $URL_BASE)" >> $PULL_LOG
    } # end read block
done # end for loop (LOG_SOURCE_IDS)


