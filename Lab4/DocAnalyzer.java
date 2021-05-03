
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.util.*;

import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class DocAnalyzer {
  public static class TokenizerMapper extends Mapper<Object, Text, Text, Text> {
    private Text outToken = new Text();
    private Text outCategory = new Text();

    private String wordCategory(String word) {
      return STOP_WORDS.contains(word) ? "stopWord" : "notstopWord";
    }

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      String fileName = ((FileSplit) context.getInputSplit()).getPath().getName().replaceAll(".txt", "");
      outToken.set(fileName);
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        String nextToken = itr.nextToken();
        nextToken = nextToken.replaceAll("[^a-zA-Z]", "").toLowerCase();
        if (nextToken.length() > 0) {
          String out = wordCategory(nextToken) + "\t" + nextToken + "\t" + Integer.toString(nextToken.length());
          outCategory.set(out);
          context.write(outToken, outCategory);
        }
      }
    }
  }

  public static class TokenCategoryReducer extends Reducer<Text, Text, Text, Text> {
    private Text outStats = new Text();
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      int stopWordCount = 0;
      int nonstopWordCount = 0;
      double nonstopWordLen = 0;
      int numUnknown = 0;
      int allWords = 0;
      int maxLen = 0;
      String maxLenWord = "";
      String prevNonStopWord = "";
      Set<String> hashSet = new HashSet<String>();
      for (Text val : values) {
        allWords += 1;
        String[] splitWords = val.toString().split("\t");
        if (splitWords[0].equals("stopWord")) {
          stopWordCount += 1;
        } else if (splitWords[0].equals("notstopWord")) {
          hashSet.add(splitWords[1]);
          if (splitWords[1].length() > maxLen) {
            maxLenWord = splitWords[1];
            maxLen = splitWords[1].length();
          }
          nonstopWordCount += 1;
          nonstopWordLen += splitWords[1].length();
        } else {
          numUnknown += 1;
        }
      }
      outStats.set(allWords + " " + stopWordCount + " " + hashSet.size() + " " + (nonstopWordLen/nonstopWordCount) + " " + maxLenWord);
      context.write(key, outStats);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();

    Job job = Job.getInstance(conf, "DocAnalyzer");

    job.setJarByClass(DocAnalyzer.class);

    job.setMapperClass(TokenizerMapper.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);

    job.setReducerClass(TokenCategoryReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }

  private static final Set<String> STOP_WORDS = new HashSet<String>(Arrays.asList(new String[] { "a", "as", "able",
      "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against",
      "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am",
      "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways",
      "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask",
      "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes",
      "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best",
      "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot",
      "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes",
      "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding",
      "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different",
      "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight",
      "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every",
      "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff",
      "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four",
      "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone",
      "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having",
      "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon",
      "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id",
      "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate",
      "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll",
      "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later",
      "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look",
      "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more",
      "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly",
      "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non",
      "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off",
      "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others",
      "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular",
      "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides",
      "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards",
      "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly",
      "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious",
      "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody",
      "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified",
      "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th",
      "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves",
      "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon",
      "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly",
      "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward",
      "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless",
      "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value",
      "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were",
      "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever",
      "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which",
      "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with",
      "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre",
      "youve", "your", "yours", "yourself", "yourselves", "zero" }));

}
