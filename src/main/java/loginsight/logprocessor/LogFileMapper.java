package loginsight.logprocessor;
  
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
  
public class LogFileMapper extends Mapper<LongWritable, Text, Text, Text> 
  {
    private Text userKey = new Text();
    private Text valueText = new Text();
     
    public void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException 
    {
        String line = value.toString();
        //Sample record 21-10-2085:09:56:02 User3 Report2 32 192.168.04.01
        String logEntryPattern ="(\\d{2}-\\d{2}-\\d{4}:\\d{2}:\\d{2}:\\d{2})\\s(\\w+)\\s(\\w+)\\s(\\w+)"
                + "\\s(\\d{3}.\\d{3}.\\d{2}.\\d{2})";
        Pattern pattern = Pattern.compile(logEntryPattern);
        Matcher m = pattern.matcher(line);
        if (m.find( ) && m.groupCount()==5) 
        {
                //context.getCounter(LogCounter.TOTAL_RECORD_COUNT).increment(1);
                String timestamp = m.group(1);
                String timestampStr = getDayMonthYearStrFromTS(timestamp);
                 
                String user = m.group(2);
                String reportName = m.group(3);
                String reportExecutionTime = m.group(4);
                String ip = m.group(5);
                String keyStr = user.trim();
                String valueStr = reportName+","+reportExecutionTime+","+timestampStr+","+ip;
                userKey.set(keyStr);
                valueText.set(valueStr);
                context.write(userKey,valueText);
                //System.out.println("m.group("+i+"):"+m.group(i));
                System.out.println("timestamp:"+timestamp);
                System.out.println("user:"+user);
        } 
        else
        {
            System.out.println("Invalid Record !!!,inputArray:"+line);
            //context.getCounter(LogCounter.BAD_RECORD_COUNT).increment(1);
        }
    }
     
    private String getDayMonthYearStrFromTS(String inputDateStr)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh:mm:ss");
        String dateStr ="";
        try
        {
            Date date = sdf.parse(inputDateStr);
            Calendar calendar =Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
             
            dateStr =calendar.get(Calendar.DAY_OF_MONTH)+","+(calendar.get(Calendar.MONTH)+1)+","+calendar.get(Calendar.YEAR);
        } 
        catch (ParseException ex) 
        {
            ex.printStackTrace();
        }
        return dateStr;
    }
  }