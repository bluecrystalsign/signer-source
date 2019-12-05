package bluecrystal.service.jwt;

import java.util.Map;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

public class Verifier {
	private JWTVerifier verifier;

//	public Verifier(Credential credential) {
//		super();
//		if(credential instanceof CredentialJHmac){
//			CredentialJHmac tmpCred = (CredentialJHmac) credential;
//			this.verifier = new JWTVerifier(tmpCred.getSecret());
//		} else {
//			CredentialPKey tmpCred = (CredentialPKey) credential;
//			this.verifier = new JWTVerifier(tmpCred.getPublicKey());			
//		}
//	}
//
//	public boolean verify(String token) throws Exception {
//		try {
//		    final Map<String,Object> claims= verifier.verify(token);
//		    return true;
//		} catch (JWTVerifyException e) {
//		}
//		return false;
//	}
//	public Claim verify(String token) throws Exception {
//		try {
//		    final Map<String,Object> claimMap= verifier.verify(token);
//		    Integer exp = (Integer) claimMap.get("exp");
//		    Integer nbf = (Integer) claimMap.get("nbf");
//		    Integer iat = (Integer) claimMap.get("iat");
//			Claim claims = new Claim((String) claimMap.get("iss"),(String) claimMap.get("sub"),(String) claimMap.get("aud"), 
//		    		(Long) (exp != null?exp.longValue():null), 
//		    		(Long) (nbf != null?exp.longValue():null), 
//		    		(Long) (iat != null?exp.longValue():null), 
//		    		(String) claimMap.get("jti"));
//		    return claims;
//		} catch (JWTVerifyException e) {
//		}
//		return new Claim();
//	}
}
