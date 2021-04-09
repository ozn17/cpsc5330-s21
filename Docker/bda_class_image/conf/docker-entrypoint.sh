#!/bin/bash

/etc/init.d/ssh start
/etc/init.d/mysql start
hdfs namenode -format
$HADOOP_HOME/sbin/start-dfs.sh

# Disgusting!  To run sqoop with --bindir /tmp and Hadoop will find the generated jar.
export HADOOP_CLASSPATH=`(hadoop classpath)`:/tmp

echo 'create database world;' | mysql --user root
mysql world --user root < /tmp/world.sql
echo "create user training identified by 'training';" | mysql --user root
echo "grant all privileges on *.* to training;" | mysql --user root
exec "$@"
