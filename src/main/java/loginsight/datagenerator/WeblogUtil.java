package loginsight.datagenerator;
 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
 
public class WeblogUtil
{
	public static String LOG_FILE_EXTENSION=".txt";
	//public static String LOG_FILE_DIRECTORY=File.separator+"home"+File.separator+"user"+File.separator+"Desktop"+File.separator+"logs";
	public static String LOG_FILE_DIRECTORY="C:"+File.separator+"Loginsight";
    public static String LOG_FILE_NAME_PREFIX="startrack";
    public static String LOG_FILE_NAME=LOG_FILE_DIRECTORY+File.separator+"startrack"+LOG_FILE_EXTENSION;
     
    public static int NUMBER_OF_LOG_FILES=1;
     
    public static int SIZE_IN_KB=1*100*100;
    public static int LINE_COUNT=500*SIZE_IN_KB;
    public static int USER_COUNT=5;
     
 
    public static String USER="user";
    public static String KPI="KPI";
    public static String SESSION_ID="SESSION_ID";
    public static String REPORT="report";
    public static String IP="192.168.04.01";
    public static Date  logTimeStamp=new Date();
    private static Calendar calendar = Calendar.getInstance();

     
    public static String SPACE=" ";
    public static String NEWLINE="\n";
     
    public static String getUserString(int userCount)
    {
        Random r = new Random();
        int low = 1;
        int high = userCount;
        int randomNumber = r.nextInt(high-low) + low;
        //System.out.println("low:"+low+",high:"+high+",randomNumber:"+randomNumber);
        String userStr = "User"+randomNumber;
        //System.out.println("userStr:"+userStr);
        return userStr;
    }
     
    public static String getReportName()
    {
        Random r = new Random();
        int low = 1;
        int high = 20;
        int randomNumber = r.nextInt(high-low) + low;
        String reportName = "Report"+randomNumber;
        //System.out.println("userStr:"+userStr);
        return reportName;
    }
     
    public static String getReportExecutionTime()
    {
        Random r = new Random();
        int low = 5;
        int high = 100;
        int randomNumber = r.nextInt(high-low) + low;
        String executionTime = String.valueOf(randomNumber);
        return executionTime;
    }
    public static String getRandomKPI(int kpiHighestValue)
    {
        Random r = new Random();
        int low = 1;
        int high = kpiHighestValue;
        int randomNumber = r.nextInt(high-low) + low;
        String kpiStr = ""+randomNumber;
        return kpiStr;
    }
     
    public static String createDataString(int index)
    {
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy:hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < LINE_COUNT; i++) 
        {
        	logTimeStamp =new Date(logTimeStamp.getTime()+i*1000*60*60);
            logTimeStamp.setMinutes(logTimeStamp.getMinutes()+i);
            calendar.setTime(logTimeStamp);
            //Reset The year to 2015
            calendar.set(Calendar.YEAR, 2015);
            logTimeStamp = calendar.getTime(); 
            String timestampStr = sdf.format(logTimeStamp);
            sb.append(timestampStr+SPACE+getUserString(USER_COUNT)+SPACE+getReportName()+SPACE+getReportExecutionTime()+SPACE+IP+NEWLINE);
             
        }
        String dataStr=sb.toString();
        return dataStr;
    }
     
    public static void addContentToFile(int index,String fileLocation)
    {
        try
        {
            String content = createDataString(index);
            File file = new File(fileLocation);
  
            // if file doesnt exists, then create it
            if (file.exists()) 
            {
                FileWriter fw = new FileWriter(fileLocation,true); //the true will append the new data
                fw.write(content);//appends the string to the file
                fw.close();
            }
            else
            {
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
     
    public static void createSingleLogFile(String fileLocation)
    {
        System.out.println("Trying to create log "+fileLocation);
        for (int i = 0; i < USER_COUNT; i++) 
        {
            addContentToFile(i,fileLocation);
        }
        System.out.println("Log created.");
    }
    public static void createMultipleLogFile(int fileCount,String logFilePrefix,String fileExtension)
    {
        System.out.println("Trying to create log files at "+LOG_FILE_DIRECTORY);
        for (int i = 1; i <= fileCount; i++) 
        {
            String fileName =logFilePrefix+i;
            String fileLocation = LOG_FILE_DIRECTORY+File.separator+fileName+fileExtension;
            createSingleLogFile(fileLocation);
        }
        System.out.println("Total log files created :"+fileCount);
    }
 
    public static void main(String[] args) 
    {
        /*
    	if(args!=null || args[0]!=null)
        {
        	LOG_FILE_DIRECTORY = args[0];
        }
        */
    	//createSingleLogFile(LOG_FILE_NAME);
        createMultipleLogFile(NUMBER_OF_LOG_FILES,LOG_FILE_NAME_PREFIX,LOG_FILE_EXTENSION);
        System.out.println("Done");
    }
 
}