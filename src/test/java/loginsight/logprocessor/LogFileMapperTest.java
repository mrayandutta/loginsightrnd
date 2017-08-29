package loginsight.logprocessor;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
//@RunWith(PowerMockRunner.class)
public class LogFileMapperTest 
{
	private MapDriver<LongWritable, Text, Text, Text> mapDriver;
	
	@Before
	  public void setUp() 
	{
	    LogFileMapper logFileMapper = new LogFileMapper();
	    mapDriver = MapDriver.newMapDriver(logFileMapper);
	}
	
	//@Ignore
	@Test
	  public void testMapperOutput() throws IOException 
	{
	    mapDriver.
	    withInput(new LongWritable(), new Text("21-10-2015:09:56:02 User3 Report2 32 192.168.04.01")).
	    withInput(new LongWritable(), new Text("This is a sample log line to be filtered")).//Line to be filtered
	    withInput(new LongWritable(), new Text("01-03-2015:07:25:40 User10 report5 10 127.145.01.01"));
	    mapDriver.
	    withOutput(new Text("User3"), new Text("Report2,32,21,10,2015,192.168.04.01")).
	    withOutput(new Text("User10"), new Text("report5,10,1,3,2015,127.145.01.01"));
	    boolean orderMatters = false;
	    final List<Pair<Text, Text>> result = mapDriver.run(orderMatters);
	}
	
	@Ignore
	@Test
	 public void testMapperCounter() throws IOException 
	{
	    mapDriver.
	    withInput(new LongWritable(), new Text("21-10-2015:09:56:02 User3 Report2 32 192.168.04.01")).
	    withInput(new LongWritable(), new Text("This is a sample log line to be filtered")).//Line to be filtered
	    withInput(new LongWritable(), new Text("01-03-2015:07:25:40 User10 report5 10 127.145.01.01"));
	    mapDriver.
	    withOutput(new Text("User3"), new Text("Report2,32,21,10,2015,192.168.04.01")).
	    withOutput(new Text("User10"), new Text("report5,10,1,3,2015,127.145.01.01"));
	    boolean orderMatters = false;
	    mapDriver.runTest(orderMatters);
	    Assert.assertEquals(2, mapDriver.getCounters().findCounter(LogCounter.TOTAL_RECORD_COUNT).getValue());
	    Assert.assertEquals(1, mapDriver.getCounters().findCounter(LogCounter.BAD_RECORD_COUNT).getValue());
	}

	@Ignore
	@Test
	 public void testMapperOutputWithGeneratedLogInput() throws IOException 
	{
	    mapDriver.
	    withInput(new LongWritable(), new Text("10-05-2016:01:41:31 User2 Report12 74 192.168.04.01"));
	    mapDriver.
	    withOutput(new Text("User3"), new Text("Report12,74,10,5,2016,IP0"));
	    boolean orderMatters = false;
	    mapDriver.runTest(orderMatters);
	}
}
