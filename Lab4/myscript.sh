rm -rf DocAnalyzer.jar
javac -cp $(hadoop classpath) DocAnalyzer.java && \
    jar cf DocAnalyzer.jar *.class && \
    rm -rf *.class

hdfs dfs -rm -r /doc-analyzer-output
HADOOP_ROOT_LOGGER="WARN,DRFA" hadoop jar DocAnalyzer.jar DocAnalyzer /data-input/textcorpora /doc-analyzer-output && \
    hdfs dfs -cat /doc-analyzer-output/part*

