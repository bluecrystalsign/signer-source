package bluecrystal.service.jwt;

import java.util.HashMap;

import com.auth0.jwt.JWTSigner;

public class Signer {
//	private JWTSigner signer;
//
//	public Signer(Credential credential) {
//		super();
//		if(credential instanceof CredentialJHmac){
//			CredentialJHmac tmpCred = (CredentialJHmac) credential;
//			this.signer = new JWTSigner(tmpCred.getSecret());
//		} else {
//			CredentialPKey tmpCred = (CredentialPKey) credential;
//			this.signer = new JWTSigner(tmpCred.getPrivateKey());
//
//		}
//	}
//	
//	public String sign(Claim claim){
//		HashMap<String, Object> map = claim.toMap();
//		return signer.sign(map);
//	}
}
