package bluecrystal.service.jwt;

import java.util.HashMap;

//https://tools.ietf.org/html/rfc7519

//4.1.  Registered Claim Names  . . . . . . . . . . . . . . . . .   9
//4.1.1.  "iss" (Issuer) Claim  . . . . . . . . . . . . . . . .   9
//4.1.2.  "sub" (Subject) Claim . . . . . . . . . . . . . . . .   9
//4.1.3.  "aud" (Audience) Claim  . . . . . . . . . . . . . . .   9
//4.1.4.  "exp" (Expiration Time) Claim . . . . . . . . . . . .   9
//4.1.5.  "nbf" (Not Before) Claim  . . . . . . . . . . . . . .  10
//4.1.6.  "iat" (Issued At) Claim . . . . . . . . . . . . . . .  10
//4.1.7.  "jti" (JWT ID) Claim  . . . . . . . . . . . . . . . .  10

public class Claim {
	private String iss;
	private String sub;
	private String aud;
	private Long exp;
	private Long nbf;
	private Long iat;
	private String jti;
	public Claim() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Claim(String issuer, String subject, String audience, Long expirationTime, Long notBefore, Long issuedAt,
			String jwtId) {
		super();
		this.iss = issuer;
		this.sub = subject;
		this.aud = audience;
		this.exp = expirationTime;
		this.nbf = notBefore;
		this.iat = issuedAt;
		this.jti = jwtId;
	}

	public String getIssuer() {
		return iss;
	}

	public String getSubject() {
		return sub;
	}

	public String getAudience() {
		return aud;
	}

	public Long getExpirationTime() {
		return exp;
	}

	public Long getNotBefore() {
		return nbf;
	}

	public Long getIssuedAt() {
		return iat;
	}

	public String getJwtId() {
		return jti;
	}

	public HashMap<String, Object> toMap(){
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		if(iss != null){
			claims.put("iss", iss);			
		}

		if(sub != null){
			claims.put("sub", sub);			
		}
		if(aud != null){
			claims.put("aud", aud);			
		}
		if(exp != null){
			claims.put("exp", exp);			
		}
		if(nbf != null){
			claims.put("nbf", nbf);			
		}
		if(iat != null){
			claims.put("iat", iat);			
		}
		if(jti != null){
			claims.put("jti", jti);			
		}
		return claims;
	}

	public boolean isEmpty() {
		if(iss != null){
			return false;			
		}

		if(sub != null){
			return false;			
		}
		if(aud != null){
			return false;			
		}
		if(exp != null){
			return false;			
		}
		if(nbf != null){
			return false;			
		}
		if(iat != null){
			return false;			
		}
		if(jti != null){
			return false;			
		}
		return true;
	}

}
