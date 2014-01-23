#!/bin/bash

# wipe an entire database,deleting all table constraints first

DATABASE="federationregistry2"
MYSQL="mysql -h DB-SERVER.HOST -u fr --password=password --skip-column-names --disable-pager $DATABASE"

DEL_CONSTRAINTS=`mktemp`
# get all constraints; create commands to delete them
echo "select table_name, constraint_name from information_schema.table_constraints where constraint_schema='$DATABASE' and constraint_type = 'FOREIGN KEY';"  | $MYSQL | while read TABLE CONSTRAINT ; do echo "ALTER TABLE $TABLE DROP FOREIGN KEY $CONSTRAINT;" ; done >> $DEL_CONSTRAINTS

cat $DEL_CONSTRAINTS | $MYSQL

rm $DEL_CONSTRAINTS


DEL_TABLES=`mktemp`
echo 'SHOW TABLES;' | $MYSQL | while read TABLE ; do echo "DROP TABLE $TABLE;" ; done >> $DEL_TABLES

cat $DEL_TABLES | $MYSQL

rm $DEL_TABLES;

