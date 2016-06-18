package bluecrystal.service.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogManager {
	public static String exceptionToString(Throwable e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString(); // stack trace as a string
		
	}
	
}
