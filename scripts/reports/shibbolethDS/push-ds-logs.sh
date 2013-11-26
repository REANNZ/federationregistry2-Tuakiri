#!/bin/bash

# to be invoked from cron

# get filename: /opt/shibboleth-ds/logs/discovery-2011-08-31.log

YESTERDAY=`date --date=yesterday +%Y-%m-%d`
DS_HOME=/opt/shibboleth-ds

DS_YESTERDAY_LOG="$DS_HOME/logs/discovery-$YESTERDAY.log"

DIR_BASE=`dirname $0`
$DIR_BASE/parse-ds-logs.pl $DS_YESTERDAY_LOG

