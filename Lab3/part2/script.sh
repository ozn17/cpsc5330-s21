

hdfs dfs -rm -r /data-output/*
hdfs dfs -mkdir /data-output
# hdfs dfs -mkdir /data-output/1-term-count
# hdfs dfs -mkdir /data-output/2-term-count-document
# hdfs dfs -mkdir /data-output/3-split-doc-term
# hdfs dfs -mkdir /data-output/4-df

###### STEP 1
hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar \
       -file step1/mapper.py \
       -mapper step1/mapper.py \
       -file step1/reducer.py  \
       -reducer step1/reducer.py \
       -input /input/textcorpora/austen* \
       -output /data-output/tmp

hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar \
       -D mapred.output.key.comparator.class=org.apache.hadoop.mapred.lib.KeyFieldBasedComparator \
       -D mapred.text.key.comparator.options=-nr \
       -file step1/mapper2.py \
       -mapper step1/mapper2.py \
       -file step1/reducer2.py  \
       -reducer step1/reducer2.py \
       -input /data-output/tmp/part* \
       -output /data-output/1-term-count

hdfs dfs -cat /data-output/1-term-count/part* | head


###### STEP 2
hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar \
       -file step2/mapper.py \
       -mapper step2/mapper.py \
       -file step2/reducer.py  \
       -reducer step2/reducer.py \
       -input /input/textcorpora/austen* \
       -output /data-output/2-term-count-document

hdfs dfs -cat /data-output/2-term-count-document/part* | head


###### STEP 3
hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar \
       -file step3/mapper.py \
       -mapper step3/mapper.py \
       -input /data-output/1-term-count/part* \
       -output /data-output/3-split-doc-term

hdfs dfs -cat /data-output/3-split-doc-term/part* | head


##### STEP 4
hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar \
       -file step4/mapper.py \
       -mapper step4/mapper.py \
       -file step4/reducer.py  \
       -reducer step4/reducer.py \
       -input /input/textcorpora/austen* \
       -output /data-output/4-df

hdfs dfs -cat /data-output/4-df/part* | head
