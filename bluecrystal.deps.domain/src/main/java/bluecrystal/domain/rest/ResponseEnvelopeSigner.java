package bluecrystal.domain.rest;

public class ResponseEnvelopeSigner {
	private String certificateB64;

	public ResponseEnvelopeSigner() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseEnvelopeSigner(String certificateB64) {
		super();
		this.certificateB64 = certificateB64;
	}

	public String getCertificateB64() {
		return certificateB64;
	}

	public void setCertificateB64(String certificateB64) {
		this.certificateB64 = certificateB64;
	}

	@Override
	public String toString() {
		return "ResponseEnvelopeSigner [certificateB64=" + certificateB64 + "]";
	}
	
	

}
