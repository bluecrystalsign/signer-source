package bluecrystal.domain.rest;

public class ResponseJwtPostSigned {
	private String postSign;


	public ResponseJwtPostSigned() {
		super();
	}


	public ResponseJwtPostSigned(String postSign) {
		super();
		this.postSign = postSign;
	}


	public String getPostSign() {
		return postSign;
	}


	public void setPostSign(String postSign) {
		this.postSign = postSign;
	}


	@Override
	public String toString() {
		return "ResponseJwtPostSigned [postSign=" + postSign + "]";
	}

	
}
