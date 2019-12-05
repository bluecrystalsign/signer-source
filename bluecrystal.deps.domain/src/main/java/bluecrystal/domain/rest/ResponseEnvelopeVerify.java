package bluecrystal.domain.rest;

public class ResponseEnvelopeVerify {
	@Override
	public String toString() {
		return "ResponseEnvelopeVerify [signStatus=" + signStatus
				+ ",\nstatusMessage=" + statusMessage + ",\ncertB64=" + certB64 + ",\ncertSubject=" + certSubject + "]";
	}
	private int signStatus;
	private String statusMessage;
	private String certB64;
	private String certSubject;
	public ResponseEnvelopeVerify() {
		super();
	}
	public ResponseEnvelopeVerify(int signStatus, String statusMessage, String certB64, String certSubject) {
		super();
		this.signStatus = signStatus;
		this.statusMessage = statusMessage;
		this.certB64 = certB64;
		this.certSubject = certSubject;
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
	
}
