#!/bin/bash

# configurable section

# Configure a command that produces a MySQL dump of the old FR1 database.
# If using SSH to connect to the FR1 host, you will need  password-less SSH
# access configured.
FR1_MYSQL_DUMP_COMMAND="ssh registry.OLD mysqldump -h DBSERVER.OLD -u fr --password=password federationregistry"
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
MYSQLDATA="$FRTMPDIR/FR1-mysql.sql"

echo "Fetching remote MySQL FR 1.x data..."
# Get the remote data from current Tuakiri-TEST FR
$FR1_MYSQL_DUMP_COMMAND > "$MYSQLDATA"

# check file exists and has size greater than zero
if [ ! -e $MYSQLDATA -o ! -s $MYSQLDATA ] ; then
   echo "ERROR: zero-sized file $MYSQLDATA"
   exit 2
fi

echo "Successfully received MySQL FR 1.x data: $MYSQLDATA"
ls -ld $MYSQLDATA

# Step 1: Stop tomcat
echo "Stopping Tomcat..."
sudo service tomcat7 stop

# Wipe the database
echo "Wiping the database..."
$DIR/wipe-db.sh

# Load the current database - FR 1.x
echo "Loading FR 1.x data into FR 2 database"
$FR2_MYSQL_COMMAND < $MYSQLDATA


# Run the script that migrates permission as such - creates target/customDataMigration.sql
mkdir "$FRTMPDIR/target"
FR_SRC_BASE="$HOME/federationregistry2-Tuakiri"
FR_UPDATE_SCRIPTS_BASE="$FR_SRC_BASE/scripts/upgrades/major-release/2"
echo "Invoking createSQLDataMigration.groovy"
( cd $FRTMPDIR ; groovy -cp "$FR_SRC_BASE/app/federationregistry/lib/mysql-connector-java-5.1.14-bin.jar" "$FR_UPDATE_SCRIPTS_BASE/createSQLDataMigration.groovy" )
echo "Created target/customDataMigration.sql"
ls -ld "$FRTMPDIR/target/customDataMigration.sql"

# Run three MYSQL scripts: schemaUpdateCreate.sql, target/customDataMigration.sql, schemaDelete.sql
echo "Invoking MySQL schemaUpdateCreate.sql ..."
$FR2_MYSQL_COMMAND < "$FR_UPDATE_SCRIPTS_BASE/schemaUpdateCreate.sql"
echo "Invoking MySQL target/customDataMigration.sql ..."
$FR2_MYSQL_COMMAND < "$FRTMPDIR/target/customDataMigration.sql"
echo "Invoking MySQL schemaDelete.sql ..."
$FR2_MYSQL_COMMAND < "$FR_UPDATE_SCRIPTS_BASE/schemaDelete.sql"
echo "Done invoking MySQL"

# OK to skip doing a new run of createAAFWorkflowScriptMigration.groovy - the
# contents of setup/src/workflow/scripts has not changed so the output is the
# same.  And the script does not work well outside its own directory.  So we
# reuse the existing  output instead.
cp "$FR_UPDATE_SCRIPTS_BASE/target/migrateAAFWorkflowScripts.groovy" "$FRTMPDIR/target/"
cp "$FR_UPDATE_SCRIPTS_BASE/createFRBaseEnvironment.groovy" "$FRTMPDIR"


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

echo "Tomcat is up, we now invoke scripts via bootstrap console"
# kill the background tail now
kill $!
wait

# Invoking via local hostname
echo "Invoking createFRBaseEnvironment.groovy"
wget --no-verbose --no-check-certificate -O $FRTMPDIR/createFRBaseEnvironment.out "https://$( hostname)/federationregistry/internal/console/execute?filename=$FRTMPDIR/createFRBaseEnvironment.groovy&captureStdout=on"
$DIR/refresh-db-json-result.py $FRTMPDIR/createFRBaseEnvironment.out

echo "Invoking migrateAAFWorkflowScripts.groovy"
wget --no-verbose --no-check-certificate -O $FRTMPDIR/migrateAAFWorkflowScripts.out "https://$( hostname)/federationregistry/internal/console/execute?filename=$FRTMPDIR/target/migrateAAFWorkflowScripts.groovy&captureStdout=on"
$DIR/refresh-db-json-result.py $FRTMPDIR/migrateAAFWorkflowScripts.out

echo "Invoking script to disable bootstrap"
echo 'config.aaf.fr.bootstrap = false' > $FRTMPDIR/disable-bootstrap.groovy
wget --no-verbose --no-check-certificate -O $FRTMPDIR/disable-bootstrap.out "https://$( hostname)/federationregistry/internal/console/execute?filename=$FRTMPDIR/disable-bootstrap.groovy&captureStdout=on"
$DIR/refresh-db-json-result.py $FRTMPDIR/disable-bootstrap.out

# Cleanup:
echo "Removing temporary files in $FRTMPDIR"
rm -f $MYSQLDATA
rm -f "$FRTMPDIR/createFRBaseEnvironment.groovy"
rm -f "$FRTMPDIR/target/migrateAAFWorkflowScripts.groovy"
rm -f "$FRTMPDIR/target/customDataMigration.sql"
rm -f "$FRTMPDIR/disable-bootstrap.groovy"
rm -f "$FRTMPDIR/disable-bootstrap.out"
rm -f "$FRTMPDIR/migrateAAFWorkflowScripts.out"
rm -f "$FRTMPDIR/createFRBaseEnvironment.out"
rm -f "$FRTMPDIR/catalina.out"
rmdir "$FRTMPDIR/target"
if [ -n "`ls $FRTMPDIR`" ] ; then
  echo "Files remaining in temporary directory - should be empty"
  ls -lR $FRTMPDIR
else
  echo "Removing empty temporary directory $FRTMPDIR"
  rmdir $FRTMPDIR
fi

