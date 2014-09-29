#!/bin/bash

# configurable section

# Configure a command that produces a MySQL dump of the source FR2 database.
# If using SSH to connect to the FR2 host, you will need  password-less SSH
# access configured.
FR2_MYSQL_DUMP_COMMAND="ssh registry.OLD mysqldump -h DBSERVER.OLD -u fr --password=password federationregistry2"
# Configure a command that invokes MySQL against the FR2 database
FR2_MYSQL_COMMAND="mysql -h DBSERVER.NEW -u fr --password=password federationregistry2"

# end configurable section

DIR=`dirname $0`
if [ -z "$DIR" ] ; then DIR=$HOME/bin ; fi

# honour TMPDIR, otherwise set it to /tmp for this script
if [ -z "$TMPDIR" ] ; then TMPDIR=/tmp ; fi

# create a temporary directory for this script
FRTMPDIR=$( mktemp -d "$TMPDIR/.tmp-FR-data-$( date '+%Y-%m-%d--%H-%M-%S' )-XXXXXX" )
if [ ! -d "$FRTMPDIR" ] ; then
   echo "ERROR: could not create temporary directory"
   exit 2
fi

echo "Initializing refresh run; temporary directory is $FRTMPDIR"
# this directory needs to be world readable/searchable for Tomcat to access
chmod a+rx $FRTMPDIR
MYSQLDATA="$FRTMPDIR/FR2-dump.sql"

echo "Fetching remote MySQL FR 2.x data..."
# Get the remote data from current Tuakiri-TEST FR
$FR2_MYSQL_DUMP_COMMAND > "$MYSQLDATA"

# check file exists and has size greater than zero
if [ ! -e $MYSQLDATA -o ! -s $MYSQLDATA ] ; then
   echo "ERROR: zero-sized file $MYSQLDATA"
   exit 2
fi

echo "Successfully received MySQL FR 2.x data: $MYSQLDATA"
ls -ld $MYSQLDATA

# Step 1: Stop tomcat
echo "Stopping Tomcat..."
sudo service tomcat7 stop

# Wipe the database
echo "Wiping the database..."
$DIR/wipe-db.sh

# Load the current database - FR 1.x
echo "Loading FR 2.x data into FR 2 database"
$FR2_MYSQL_COMMAND < $MYSQLDATA
echo "Done invoking MySQL"


FR_TOMCAT_LOG="$FRTMPDIR/catalina.out"
tail --follow=name -n 0 /var/log/tomcat7/catalina.out > $FR_TOMCAT_LOG &
TAIL_ID=$!
echo "Starting Tomcat"
sudo service tomcat7 start

# Wait for Tomcat to come up
echo "Waiting for Tomcat to come up"
# copy fresh parts of Tomcat log into our copy so that we can search for startup message
while ! grep -q '^INFO: Server startup in' $FR_TOMCAT_LOG ; do
  sleep 5
done

echo "Tomcat is up, we are now all done"
# kill the background tail now
kill $!
wait

# Cleanup:
echo "Removing temporary files in $FRTMPDIR"
rm -f $MYSQLDATA
rm -f "$FRTMPDIR/catalina.out"
if [ -n "`ls $FRTMPDIR`" ] ; then
  echo "Files remaining in temporary directory - should be empty"
  ls -lR $FRTMPDIR
else
  echo "Removing empty temporary directory $FRTMPDIR"
  rmdir $FRTMPDIR
fi

