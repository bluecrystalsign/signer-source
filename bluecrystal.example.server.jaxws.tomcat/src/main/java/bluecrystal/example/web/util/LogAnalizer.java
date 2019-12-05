package bluecrystal.example.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;

public class LogAnalizer {
	private String logPath;

	public String getLogPath() {
		if (logPath == null) {
			System.out.println("***LoggerContext");
			LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
			List<Logger> loggerList = lc.getLoggerList();
			if(loggerList == null){
				System.out.println("** loggerList é nulo!");
			} else {
				System.out.println("** loggerList tamanho: "+loggerList.size());				
			}
			for (Logger next : loggerList) {
				System.out.println("->"+next.toString());
				
				try {
					Appender<ILoggingEvent> appender = next.getAppender("fileAppender");
					if (appender != null) {
						FileAppender fileApp = (FileAppender) appender;
						logPath = fileApp.getFile();
						return logPath;
					} else {
						System.out.println("** log appender é nulo!");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
		return logPath;
	}

	public int countLines() throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(logPath));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	public String getFullContent() {
		try {
			String top = "<table class=\"table table-condensed\">\n" + "<thead>\n" + "<tr>\n" 
					+ "<th>Conteudo</th>\n"
					+ "<th>Data / Hora</th>\n"
					+ "<th>Classe</th>\n"
					+ "<th>Evento</th>\n"
					+ "</tr>\n" + "</thead>\n" + "<tbody>\n";

			String tail = "</tbody>\n" + "</table>\n";
			String content = "";

			InputStream is = new FileInputStream(logPath);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
			String line = null;
//			String pattern = "(.*)DEBUG(.*)";
			String pattern = "(^.*)(\\,.*\\s)(\\[.*\\])\\s(TRACE|DEBUG|INFO|WARN|ERROR)\\s(.*)(\\s\\-\\s)(.*$)";
			Pattern r = Pattern.compile(pattern);
			while ((line = buffer.readLine()) != null) {
				Matcher m = r.matcher(line);

				String levelLabel = " <span class=\"label label-warning\">"+"." +"</span>";
				if (m.find( )) {
					String lcontent = "<tr>\n";
					String level = m.group(4);
					
					switch (level) {
					case "TRACE":
						levelLabel = " <span class=\"label label-default\">"+"TRACE" +"</span>";
						break;
					case "DEBUG":
						levelLabel = " <span class=\"label label-success\">"+"DEBUG" +"</span>";
						break;
					case "INFO":
						levelLabel = " <span class=\"label label-info\">"+"INFO" +"</span>";
						break;
					case "WARN":
						levelLabel = " <span class=\"label label-warning\">"+"WARN" +"</span>";
						break;
					case "ERROR":
						levelLabel = " <span class=\"label label-danger\">"+"ERROR" +"</span>";
						break;

					default:
						break;
					}
					lcontent += "<td><h6>" + levelLabel + "</h6></td>\n"; //m.group(4)
					lcontent += "<td><h6>" + dateFormat(m.group(1)) + "</h6></td>\n";
					lcontent += "<td><h6>" + m.group(5) + "</h6></td>\n";
					lcontent += "<td><h6>" + m.group(7) + "</h6></td>\n";
					lcontent += "</tr>\n";
					content = lcontent + content;
				} else {
					content += "<tr>\n";
					content += "<td>-</td>\n";
					content += "<td>-</td>\n";
					content += "<td>-</td>\n";
					content += "<td>"+line+"</td>\n";
					content += "</tr>\n";
				}

			}
			return top + content + tail;
		} catch (Exception e) {
			return "<p>Não foi possivel carregar o conteudo</p>";
		}
	}

	public static String exceptionToString(Throwable e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString(); // stack trace as a string
		
	}
	private String dateFormat(String group) {
//		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM kk:mm:ss Z yyyy");
//		Date signingTime = sdf.parse("Fri Jun 03 14:58:08 BRT 2016");
//		2016-06-05 15:31:25
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			Date date = sdf.parse(group);

			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");

			return sdf2.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NONE";
	}
	
	
}
