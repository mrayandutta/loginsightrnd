package loginsight.pigsolution;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;


	public class FixHadoopOnWindows 
	{
		private static final String TEMP_PIG_LOCATION = FixHadoopOnWindows.class.getClassLoader().getResource("loginsight/pigtemp").getPath();

	    /**
	     * Fix the followind Hadoop problem on Windows:
	     * 1) mapReduceLayer.Launcher: Backend error message during job submission 
	     * java.io.IOException: Failed to set permissions of path: \tmp\hadoop-MyUsername\mapred\staging\
	     * 2) java.io.IOException: Failed to set permissions of path: bla-bla-bla\.staging to 0700
	     */

	    public static void runFix() throws NotFoundException, CannotCompileException 
	    {
	        if( isWindows() ) 
	        { // run fix only on Windows
	            setUpSystemVariables();
	            fixCheckReturnValueMethod();
	        }
	    }

	    // set up correct temporary directory on windows
	    private static void setUpSystemVariables() 
	    {
	    	System.getProperties().setProperty("java.io.tmpdir", TEMP_PIG_LOCATION);
	        System.getProperties().setProperty("pig.temp.dir", TEMP_PIG_LOCATION);
	        System.getProperties().setProperty("hadoop.tmp.dir", TEMP_PIG_LOCATION);
	    }

	    /**
	     * org.apache.hadoop.fs.FileUtil#checkReturnValue doesn't work on Windows at all
	     * so, let's change method body with Javassist on empty body
	     */
	    private static void fixCheckReturnValueMethod() throws NotFoundException, CannotCompileException {
	        ClassPool cp = new ClassPool(true);
	        CtClass ctClass = cp.get("org.apache.hadoop.fs.FileUtil");
	        CtMethod ctMethod = ctClass.getDeclaredMethod("checkReturnValue");
	        ctMethod.setBody("{  }");
	        ctClass.toClass();
	    }

	    private static boolean isWindows() {
	        String OS = System.getProperty("os.name");
	        return OS.startsWith("Windows");
	    }

	    private FixHadoopOnWindows() { }
	}
