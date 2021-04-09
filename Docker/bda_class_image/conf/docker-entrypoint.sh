#!/bin/bash

/etc/init.d/ssh start
hdfs namenode -format
$HADOOP_HOME/sbin/start-dfs.sh

# Hive directories
hdfs dfs -mkdir /tmp
hdfs dfs -chmod g+w /tmp
hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/hive
hdfs dfs -mkdir /user/hive/warehouse
hdfs dfs -chmod g+w /user/hive/warehouse

/opt/hive/bin/schematool -initSchema -dbType derby

exec "$@"
