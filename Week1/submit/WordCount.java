import java.io.IOException;
import java.util.StringTokenizer;
import java.util.*;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

  public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken().replaceAll("\\p{Punct}", "").trim().toLowerCase());
        context.write(word, one);
      }
    }
  }

  public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    // private IntWritable result = new IntWritable();
    TreeMap<Text, IntWritable> result = new TreeMap<Text, IntWritable>();

    public void reduce(Text key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.put(new Text(key), new IntWritable(sum));
    }


    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
  
      Set<Entry<Text, IntWritable>> set = result.entrySet();
      List<Entry<Text, IntWritable>> list = new ArrayList<Entry<Text, IntWritable>>(set);
      Collections.sort(list, new Comparator<Map.Entry<Text, IntWritable>>() {
        public int compare(Map.Entry<Text, IntWritable> o1, Map.Entry<Text, IntWritable> o2) {
          return (o2.getValue()).compareTo(o1.getValue());
        }
      });
      for (Map.Entry<Text, IntWritable> entry : list) {
  
        context.write(entry.getKey(), entry.getValue());
      }
    }
  }


  public static void main(String[] args) throws Exception {
    System.out.println("hello from WordCount!");
    Configuration conf = new Configuration();

    Job job = Job.getInstance(conf, "word count");

    job.setJarByClass(WordCount.class);

    job.setMapperClass(TokenizerMapper.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);

    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}