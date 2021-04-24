#!/bin/bash

hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar \
       -file $1/mapper.py \
       -mapper $1/mapper.py \
       -file $1/reducer.py  \
       -reducer $1/reducer.py \
       -input $2 \
       -output $3
