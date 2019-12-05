package bluecrystal.domain.rest;


public class RequestJwtPreSigned {
	private String issuer;
	private String subject;
	private String audience;
	private Long expirationTime;
	private Long notBefore;
	private Long issuedAt;
	private String jwtId;
	
	private String credential;
	private int credentialType;
	
	public RequestJwtPreSigned() {
		super();
	}

	public RequestJwtPreSigned(String issuer, String subject, String audience, Long expirationTime, Long notBefore,
			Long issuedAt, String jwtId, String credential, int credentialType) {
		super();
		this.issuer = issuer;
		this.subject = subject;
		this.audience = audience;
		this.expirationTime = expirationTime;
		this.notBefore = notBefore;
		this.issuedAt = issuedAt;
		this.jwtId = jwtId;
		this.credential = credential;
		this.credentialType = credentialType;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getSubject() {
		return subject;
	}

	public String getAudience() {
		return audience;
	}

	public Long getExpirationTime() {
		return expirationTime;
	}

	public Long getNotBefore() {
		return notBefore;
	}

	public Long getIssuedAt() {
		return issuedAt;
	}

	public String getJwtId() {
		return jwtId;
	}

	public String getCredential() {
		return credential;
	}

	public int getCredentialType() {
		return credentialType;
	}

	@Override
	public String toString() {
		return "RequestJwtPreSigned [issuer=" + issuer + ", subject=" + subject + ", audience=" + audience
				+ ", expirationTime=" + expirationTime + ", notBefore=" + notBefore + ", issuedAt=" + issuedAt
				+ ", jwtId=" + jwtId + ", credential=" + credential + ", credentialType=" + credentialType + "]";
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public void setExpirationTime(Long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public void setNotBefore(Long notBefore) {
		this.notBefore = notBefore;
	}

	public void setIssuedAt(Long issuedAt) {
		this.issuedAt = issuedAt;
	}

	public void setJwtId(String jwtId) {
		this.jwtId = jwtId;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public void setCredentialType(int credentialType) {
		this.credentialType = credentialType;
	}
	
}
