package bluecrystal.example.web.domain;

public class LogStructure {
	private String logPath;
	private long 	lineCount;
	private String fullContent;
	public String getLogPath() {
		return logPath;
	}
	public long getLineCount() {
		return lineCount;
	}
	public String getFullContent() {
		return fullContent;
	}
	public LogStructure(String logPath, long lineCount, String fullContent) {
		super();
		this.logPath = logPath;
		this.lineCount = lineCount;
		this.fullContent = fullContent;
	}

	
	

}
