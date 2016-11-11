package bluecrystal.domain.rest;

public class ResponseEnvelope {

	private String signedContent;
	private int signStatus;
	private String statusMessage;
	private String certB64;
	private String certSubject;
	public ResponseEnvelope() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ResponseEnvelope(String signedContent, int signStatus, String statusMessage, String certB64,
			String certSubject) {
		super();
		this.signedContent = signedContent;
		this.signStatus = signStatus;
		this.statusMessage = statusMessage;
		this.certB64 = certB64;
		this.certSubject = certSubject;
	}
	public String getSignedContent() {
		return signedContent;
	}
	public void setSignedContent(String signedContent) {
		this.signedContent = signedContent;
	}
	public int getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(int signStatus) {
		this.signStatus = signStatus;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public String getCertB64() {
		return certB64;
	}
	public void setCertB64(String certB64) {
		this.certB64 = certB64;
	}
	public String getCertSubject() {
		return certSubject;
	}
	public void setCertSubject(String certSubject) {
		this.certSubject = certSubject;
	}
	
	@Override
	public String toString() {
		return "ResponseEnvelope [signedContent=" + signedContent + ", signStatus=" + signStatus + ", statusMessage="
				+ statusMessage + ", certB64=" + certB64 + ", certSubject=" + certSubject + "]";
	}
	
}
