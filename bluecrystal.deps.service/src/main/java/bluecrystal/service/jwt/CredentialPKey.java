package bluecrystal.service.jwt;

import java.security.PrivateKey;
import java.security.PublicKey;

import bluecrystal.bcdeps.helper.PkiOps;

public class CredentialPKey implements Credential {
    private PrivateKey privateKey;
    private PublicKey publicKey;

	public CredentialPKey(PrivateKey privateKey, PublicKey publicKey) {
		super();
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}
	
	public CredentialPKey(String publicKey) throws Exception {
		super();
		PkiOps ops = new PkiOps();
		this.privateKey = null;
		this.publicKey = ops.createPublicKey(publicKey);
//		byte[] pkBytes = Base64.decode(publicKey);
//		this.publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pkBytes));
	}

	public CredentialPKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public AppAlgorithm getAlgorithn() {
		return AppAlgorithm.RS256;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

}
