package bluecrystal.service.jwt;

import com.auth0.jwt.JWTAlgorithmException;

public enum AppAlgorithm {

	HS256("HmacSHA256"), HS384("HmacSHA384"), HS512("HmacSHA512"), RS256("SHA256withRSA"), RS384(
			"SHA384withRSA"), RS512("SHA512withRSA");

	private AppAlgorithm(final String value) {
			this.value = value;
		}

	private String value;

	public String getValue() {
		return value;
	}

	public static AppAlgorithm findByName(final String name) throws JWTAlgorithmException {
		try {
			return AppAlgorithm.valueOf(name);
		} catch (IllegalArgumentException e) {
			throw new JWTAlgorithmException("Unsupported algorithm: " + name);
		}
	}

}
