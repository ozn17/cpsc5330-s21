rm -rf part-r-00000
hdfs dfs -rm -r $3
hadoop jar $1.jar $1 $2 $3
echo ""
echo ""
hdfs dfs -ls /code/output
echo ""
echo ""
hdfs dfs -get /code/output/part-r-00000 .
