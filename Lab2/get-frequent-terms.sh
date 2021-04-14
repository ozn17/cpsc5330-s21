hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar \
       -file $1/mapper.py \
       -mapper $1/mapper.py \
       -file $1/reducer.py  \
       -reducer $1/reducer.py \
       -input /input/textcorpora/austen* \
       -output /output/word_count

hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar \
       -D mapred.output.key.comparator.class=org.apache.hadoop.mapred.lib.KeyFieldBasedComparator \
       -D mapred.text.key.comparator.options=-nr \
       -file $1/mapper2.py \
       -mapper $1/mapper2.py \
       -file $1/reducer2.py  \
       -reducer $1/reducer2.py \
       -input /output/word_count/part* \
       -output /output/after_reverse

hdfs dfs -cat /output/after_reverse/part* | head
