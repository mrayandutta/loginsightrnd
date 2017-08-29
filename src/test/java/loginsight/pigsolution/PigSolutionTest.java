package loginsight.pigsolution;

import java.io.IOException;

import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class PigSolutionTest 
{
	private static PigTest test;
	ClassLoader classLoader = PigSolutionTest.class.getClassLoader();
	@BeforeClass
	public static void setUp() 
	{
	    try 
	    {
	        FixHadoopOnWindows.runFix();
	    }
	    catch (Exception e) 
	    {
	    	e.printStackTrace();
	    }
	}
	
	@Test
	public void testLogParse() throws IOException, ParseException
	{
		//load pig script properly
		String scriptPath = classLoader.getResource("loginsight/pigsolution/logparse.pig").getPath();
    	System.out.println("scriptPath:"+scriptPath);
        test = new PigTest(scriptPath);           

        String[] input = {"10-05-2016:01:41:31 User2 Report12 74 IP0"};
        String[] output = {"(6,hello)"};
        test.assertOutput("logdata",input,"weblogs",output);
    }
	
	@Ignore
	@Test
	public void testWordcount() throws IOException, ParseException
	{
		//load pig script properly
		String scriptPath = classLoader.getResource("loginsight/pigsolution/wordcount.pig").getPath();
    	System.out.println("scriptPath:"+scriptPath);
        test = new PigTest(scriptPath);           

        String[] input = {"hello hello hello","hello hello hello"};
        String[] output = {"(6,hello)"};
        test.assertOutput("A",input,"D",output);
    }


}
