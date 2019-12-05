package bluecrystal.service.jwt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.auth0.jwt.JWTAlgorithmException;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AppJWTSigner {
	private byte[] secret;
	private PrivateKey privateKey;

	// Default algorithm HMAC SHA-256 ("HS256")
	protected final static AppAlgorithm DEFAULT_ALGORITHM = AppAlgorithm.HS256;

    public AppJWTSigner(final String secret) {
        this.secret =  secret.getBytes();
    }

    public AppJWTSigner(final byte[] secret) {
        this.secret =  secret;
    }
	
        
	public AppJWTSigner(PrivateKey privateKey) {
		super();
		this.privateKey = privateKey;
	}

	public String sign(final Map<String, Object> claims, final AppAlgorithm algorithm) throws Exception {
		String preSign = preSign(claims, algorithm);
		byte[] preEncoded = signByAlg(preSign, algorithm);
		String signed = postSign(preEncoded, preSign);
		return signed;
	}    
    
	public String preSign(final Map<String, Object> claims, final AppAlgorithm alg) {
		final AppAlgorithm algorithm = (alg != null) ? alg : DEFAULT_ALGORITHM;
		final List<String> segments = new ArrayList<>();
		try {
			segments.add(encodedHeader(algorithm));
			segments.add(encodedPayload(claims));
			String joinPreSign = join(segments, ".");

			return joinPreSign;
		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
	}

	public String postSign( 
			byte[] preEncoded, //			final AppAlgorithm alg, 
			String preSign) {
		final List<String> segments = new ArrayList<>();
		try {
			String post = postEncodedSignature(preEncoded);
			segments.add(preSign);
			segments.add(post);
			String joinPostSign = join(segments, ".");

			return joinPostSign;
		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
	}


	
	/**
	 * Generate the header part of a JSON web token.
	 */
	private String encodedHeader(final AppAlgorithm algorithm) throws UnsupportedEncodingException {
		// Validate.notNull(algorithm);
		// create the header
		final ObjectNode header = JsonNodeFactory.instance.objectNode();
		header.put("typ", "JWT");
		header.put("alg", algorithm.name());
		return base64UrlEncode(header.toString().getBytes("UTF-8"));
	}

	/**
	 * Generate the JSON web token payload string from the claims.
	 *
	 * @param options
	 */
	private String encodedPayload(final Map<String, Object> _claims) throws IOException {
		final Map<String, Object> claims = new HashMap<>(_claims);
		// enforceStringOrURI(claims, "iss");
		// enforceStringOrURI(claims, "sub");
		// enforceStringOrURICollection(claims, "aud");
		// enforceIntDate(claims, "exp");
		// enforceIntDate(claims, "nbf");
		// enforceIntDate(claims, "iat");
		// enforceString(claims, "jti");
		// if (options != null) {
		// processPayloadOptions(claims, options);
		// }
		final String payload = new ObjectMapper().writeValueAsString(claims);
		return base64UrlEncode(payload.getBytes("UTF-8"));
	}

	private String base64UrlEncode(final byte[] str) {
		// Validate.notNull(str);
		return new String(Base64.encodeBase64URLSafe(str));
	}

	private String join(final List<String> input, final String separator) {
		// Validate.notNull(input);
		// Validate.notNull(separator);
		return StringUtils.join(input.iterator(), separator);
	}

	public byte[] signByAlg(final String signingInput, final AppAlgorithm algorithm)
			throws NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException,
			JWTAlgorithmException {
		// Validate.notNull(signingInput);
		// Validate.notNull(algorithm);
		switch (algorithm) {
		case HS256:
		case HS384:
		case HS512:
			return signHmac(algorithm, signingInput, secret);
		case RS256:
		case RS384:
		case RS512:
			return signRs(algorithm, signingInput, privateKey);
		default:
			throw new JWTAlgorithmException("Unsupported signing method");
		}
	}

	private String postEncodedSignature(byte[] preEncoded)
			throws NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException,
			JWTAlgorithmException {
			return base64UrlEncode(preEncoded);
		}

//	private String encodedSignature(final String signingInput, final AppAlgorithm algorithm) throws Exception {
//		byte[] preEncoded = signByAlg(signingInput, algorithm);
//		return postEncodedSignature(preEncoded);
//	}

	
//	private String encodedSignature(final String signingInput, final AppAlgorithm algorithm)
//			throws NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException,
//			JWTAlgorithmException {
//		// Validate.notNull(signingInput);
//		// Validate.notNull(algorithm);
//		switch (algorithm) {
//		case HS256:
//		case HS384:
//		case HS512:
//			return base64UrlEncode(signHmac(algorithm, signingInput, secret));
//		case RS256:
//		case RS384:
//		case RS512:
//			return base64UrlEncode(signRs(algorithm, signingInput, privateKey));
//		default:
//			throw new JWTAlgorithmException("Unsupported signing method");
//		}
//	}

	
	
	
	private static byte[] signHmac(final AppAlgorithm algorithm, final String msg, final byte[] secret)
			throws NoSuchAlgorithmException, InvalidKeyException {
		// Validate.notNull(algorithm);
		// Validate.notNull(msg);
		// Validate.notNull(secret);
		final Mac mac = Mac.getInstance(algorithm.getValue());
		mac.init(new SecretKeySpec(secret, algorithm.getValue()));
		return mac.doFinal(msg.getBytes());
	}

	private static byte[] signRs(final AppAlgorithm algorithm, final String msg, final PrivateKey privateKey)
			throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
//		Validate.notNull(algorithm);
//		Validate.notNull(msg);
//		Validate.notNull(privateKey);
		final byte[] messageBytes = msg.getBytes();
		final Signature signature = Signature.getInstance(algorithm.getValue(), "BC");
		signature.initSign(privateKey);
		signature.update(messageBytes);
		return signature.sign();
	}

}