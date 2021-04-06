
hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar \
       -file mapper.py \
       -mapper mapper.py \
       -file reducer.py  \
       -reducer reducer.py \
       -input /input/textcorpora \
       -output /output/word-count
