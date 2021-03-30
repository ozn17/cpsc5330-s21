#!/bin/bash

/etc/init.d/ssh start
hdfs namenode -format
$HADOOP_HOME/sbin/start-dfs.sh

exec "$@"
