package bluecrystal.domain.rest;

public class ResponseSignable {
	private String timeValue;
	private String saValue;
	public ResponseSignable() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public ResponseSignable(String timeValue, String saValue) {
		super();
		this.timeValue = timeValue;
		this.saValue = saValue;
	}

	public String getTimeValue() {
		return timeValue;
	}
	public void setTimeValue(String timeValue) {
		this.timeValue = timeValue;
	}
	public String getSaValue() {
		return saValue;
	}
	public void setSaValue(String saValue) {
		this.saValue = saValue;
	}
	@Override
	public String toString() {
		return "ResponseSignable [timeValue=" + timeValue + ", saValue=" + saValue + "]";
	}
	
}
