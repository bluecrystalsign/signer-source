package bluecrystal.service.jwt;

public class CredentialJHmac implements Credential {
	private byte[] secret;

	public CredentialJHmac() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CredentialJHmac(byte[] secret) {
		super();
		this.secret = secret;
	}

	public byte[] getSecret() {
		return secret;
	}

	@Override
	public AppAlgorithm getAlgorithn() {
		return AppAlgorithm.HS256;
	}
	
}
