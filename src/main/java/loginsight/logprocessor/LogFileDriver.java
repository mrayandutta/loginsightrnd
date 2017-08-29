package loginsight.logprocessor;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
  
public class LogFileDriver extends Configured implements Tool
{
    public static void main(String[] args) throws Exception 
    {
        int response =ToolRunner.run(new Configuration(), new LogFileDriver(), args);
        System.out.println("response :"+response);
    }
  
    public int run(String[] args) throws Exception 
    {
          Configuration conf = getConf();
          conf.set("mapred.textoutputformat.separator", ",");
           
          Job job = new Job(conf, getClass().getName());
           
          job.setJarByClass(LogFileDriver.class);
          job.setMapperClass(LogFileMapper.class);
          int noOfReducers =1;
          job.setNumReduceTasks(noOfReducers);
           
          job.setMapOutputKeyClass(Text.class);
          job.setMapOutputValueClass(Text.class);
          job.setOutputKeyClass(Text.class);
          job.setOutputValueClass(Text.class);
           
          job.setInputFormatClass(TextInputFormat.class);
          job.setOutputFormatClass(TextOutputFormat.class);
           
          /*
          int totalValidRecords= (int) job.getCounters().findCounter(LogCounter.TOTAL_RECORD_COUNT).getValue();
          int totalBadRecords= (int) job.getCounters().findCounter(LogCounter.TOTAL_RECORD_COUNT).getValue();
          System.out.println("totalValidRecords:"+totalValidRecords);
          System.out.println("totalBadRecords:"+totalBadRecords);
          */
           
          //Options is provided for running from command line as well as from eclipse
           
          String inputPath =args[0];
          String outputPath =args[1];
          //String inputPath =LogProcessorConstants.HDFS_INPUT_LOCATION;
          //String outputPath =LogProcessorConstants.HDFS_OUTPUT_LOCATION;      
          System.out.println("inputPath:"+inputPath);
          System.out.println("outputPath:"+outputPath);
          
          FileSystem fs = FileSystem.get(conf);
          /*Check if output path exist or not*/
          if(fs.exists(new Path(outputPath)))
          {
             /*If exist delete the output path*/
             fs.delete(new Path(outputPath),true);
          }
         
          FileInputFormat.addInputPath(job, new Path(inputPath));
          FileOutputFormat.setOutputPath(job, new Path(outputPath));
         
          int status =job.waitForCompletion(true)?0:1;
          //Counter recordCount = job.getCounters().findCounter(LogCounter.TOTAL_RECORD_COUNT);
          //System.out.println("record processed:"+recordCount.getValue());
          System.out.println("status:"+status);
          return status;
    }
}