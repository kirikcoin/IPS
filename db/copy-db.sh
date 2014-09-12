#!/bin/bash
#
#   Copies a specified MySQL within the same host.
#
#   Fill in DB_SOURCE for source DB, DB_TARGET for the target one (surprisingly).
#

DB_USER=user
DB_PASSWORD=password
DB_SOURCE=ips_migration_test
DB_TARGET=
DB_SERVER=localhost
MYSQL=mysql

stmtCreateTable=""
stmtInsertData=""

echo "Copying database '$DB_SOURCE' -> '$DB_TARGET'"
DBCONN="-h ${DB_SERVER} -u ${DB_USER} --password=${DB_PASSWORD}"

echo "DROP DATABASE IF EXISTS ${DB_TARGET}" | ${MYSQL} ${DBCONN}
echo "CREATE DATABASE ${DB_TARGET}" | ${MYSQL} ${DBCONN}

for TABLE in `echo "SHOW TABLES" | ${MYSQL} ${DBCONN} ${DB_SOURCE} | tail -n +2`; do
    createTable=`echo "SHOW CREATE TABLE ${TABLE}" | ${MYSQL} -B -r ${DBCONN} ${DB_SOURCE}|tail -n +2|cut -f 2-`
    stmtCreateTable="${stmtCreateTable} ; ${createTable}"
    insertData="INSERT INTO ${DB_TARGET}.${TABLE} SELECT * FROM ${DB_SOURCE}.${TABLE}"
    stmtInsertData="${stmtInsertData} ; ${insertData}"
done;

#echo ${stmtCreateTable} > create-table.sql
#echo ${stmtInsertData} > insert-data.sql

stmt="SET foreign_key_checks = 0; $stmtCreateTable ; $stmtInsertData ; SET foreign_key_checks = 1;"
echo "${stmt}" | ${MYSQL} ${DBCONN} ${DB_TARGET}
