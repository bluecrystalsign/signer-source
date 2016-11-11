package bluecrystal.domain.rest;

public class ResponseJwtPreSigned {
	private String preSign;

	public ResponseJwtPreSigned(String preSign) {
		super();
		this.preSign = preSign;
	}

	public ResponseJwtPreSigned() {
		super();
	}

	public String getPreSign() {
		return preSign;
	}

	@Override
	public String toString() {
		return "ResponseJwtPreSigned [preSign=" + preSign + "]";
	}

	public void setPreSign(String preSign) {
		this.preSign = preSign;
	}
	
}
