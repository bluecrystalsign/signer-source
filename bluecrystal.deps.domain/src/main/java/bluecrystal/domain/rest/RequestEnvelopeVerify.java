package bluecrystal.domain.rest;

public class RequestEnvelopeVerify {
	@Override
	public String toString() {
		return "RequestEnvelopeVerify [envelope=" + envelope + ", contentHash=" + contentHash + "]";
	}
	private String envelope;
	private String contentHash;
	public RequestEnvelopeVerify() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RequestEnvelopeVerify(String envelope, String contentHash) {
		super();
		this.envelope = envelope;
		this.contentHash = contentHash;
	}
	public String getEnvelope() {
		return envelope;
	}
	public void setEnvelope(String envelope) {
		this.envelope = envelope;
	}
	public String getContentHash() {
		return contentHash;
	}
	public void setContentHash(String contentHash) {
		this.contentHash = contentHash;
	}


}
