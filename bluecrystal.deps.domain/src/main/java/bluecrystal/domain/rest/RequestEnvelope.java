package bluecrystal.domain.rest;

//String hash_valueb64 = (String) request.getParameter("hash_value");
//String timeValue = (String) request.getParameter("time_value");
//String saValueb64 = (String) request.getParameter("sa_value");
//String signedValueb64 = (String) request.getParameter("signed_value");
//String certb64 = (String) request.getParameter("cert");
//String alg = (String) request.getParameter("alg");
public class RequestEnvelope {
	private String hashValueb64;
	private String timeValue;
	// "2010-10-27T11:58:22.973Z"
	// ISO 8601 DateTime
	private String saValueb64;
	private String signedValueb64;
	private String certb64;
	private String alg;
	public RequestEnvelope() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RequestEnvelope(String hashValueb64, String timeValue, String saValueb64, String signedValueb64,
			String certb64, String alg) {
		super();
		this.hashValueb64 = hashValueb64;
		this.timeValue = timeValue;
		this.saValueb64 = saValueb64;
		this.signedValueb64 = signedValueb64;
		this.certb64 = certb64;
		this.alg = alg;
	}
	public String getHashValueb64() {
		return hashValueb64;
	}
	public void setHashValueb64(String hashValueb64) {
		this.hashValueb64 = hashValueb64;
	}
	public String getTimeValue() {
		return timeValue;
	}
	public void setTimeValue(String timeValue) {
		this.timeValue = timeValue;
	}
	public String getSaValueb64() {
		return saValueb64;
	}
	public void setSaValueb64(String saValueb64) {
		this.saValueb64 = saValueb64;
	}
	public String getSignedValueb64() {
		return signedValueb64;
	}
	public void setSignedValueb64(String signedValueb64) {
		this.signedValueb64 = signedValueb64;
	}
	public String getCertb64() {
		return certb64;
	}
	public void setCertb64(String certb64) {
		this.certb64 = certb64;
	}
	public String getAlg() {
		return alg;
	}
	public void setAlg(String alg) {
		this.alg = alg;
	}
	@Override
	public String toString() {
		return "RequestEnvelope [hashValueb64=" + hashValueb64 + ", timeValue=" + timeValue + ", saValueb64="
				+ saValueb64 + ", signedValueb64=" + signedValueb64 + ", certb64=" + certb64 + ", alg=" + alg + "]";
	}
	
	
}
