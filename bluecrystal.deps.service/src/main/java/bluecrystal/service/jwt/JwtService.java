package bluecrystal.service.jwt;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

public class JwtService {

	public String sign(Claim claim, Credential credential) throws Exception {
		String preSign  = preSign(claim, credential);
		byte[] preEncoded = sign(claim, credential, preSign);
		return postSign(credential, preSign, preEncoded);
	}

	public String preSign(Claim claim, Credential credential) throws Exception {
		AppJWTSigner signer = null;
		signer = createSigner(credential);
		HashMap<String, Object> claimMap = claim.toMap();

		AppAlgorithm alg = credential.getAlgorithn();
		String preSign = signer.preSign(claimMap, alg);
		return preSign;
	}

	public byte[] sign(Claim claim, Credential credential, String preSign) throws Exception {
		AppJWTSigner signer = null;
		signer = createSigner(credential);

		AppAlgorithm alg = credential.getAlgorithn();
		byte[] preEncoded = signer.signByAlg(preSign, alg);
		return preEncoded;
	}

	private AppJWTSigner createSigner(Credential credential) {
		AppJWTSigner signer;
		if (credential instanceof CredentialJHmac) {
			CredentialJHmac tmpCred = (CredentialJHmac) credential;
			signer = new AppJWTSigner(tmpCred.getSecret());
		} else {
			CredentialPKey tmpCred = (CredentialPKey) credential;
			signer = new AppJWTSigner(tmpCred.getPrivateKey());

		}
		return signer;
	}

	public String postSign(Credential credential, String preSign, byte[] preEncoded) throws Exception {
		AppJWTSigner signer = null;
		signer = createSigner(credential);
		String signed = signer.postSign(preEncoded, preSign);
		return signed;
	}

	public Claim verify(String token, Credential credential) throws Exception {
		JWTVerifier verifier = null;
		verifier = createVerifier(credential, verifier);
		try {
			return buildClaim(token, verifier);
		} catch (JWTVerifyException e) {
			e.printStackTrace();
		}
		return new Claim();

	}

	private JWTVerifier createVerifier(Credential credential, JWTVerifier verifier) {
		if (credential instanceof CredentialJHmac) {
			CredentialJHmac tmpCred = (CredentialJHmac) credential;
			verifier = new JWTVerifier(tmpCred.getSecret());
		} else {
			CredentialPKey tmpCred = (CredentialPKey) credential;
			verifier = new JWTVerifier(tmpCred.getPublicKey());			

		}
		return verifier;
	}

	private Claim buildClaim(String token, JWTVerifier verifier)
			throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException, JWTVerifyException {
		final Map<String, Object> claimMap = verifier.verify(token);
		Integer exp = (Integer) claimMap.get("exp");
		Integer nbf = (Integer) claimMap.get("nbf");
		Integer iat = (Integer) claimMap.get("iat");
		Claim claims = new Claim((String) claimMap.get("iss"), (String) claimMap.get("sub"),
				(String) claimMap.get("aud"), 
				(Long) (exp != null ? exp.longValue() : null),
				(Long) (nbf != null ? nbf.longValue() : null), 
				(Long) (iat != null ? iat.longValue() : null),
				(String) claimMap.get("jti"));
		return claims;
	}
}
