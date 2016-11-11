package bluecrystal.domain.rest;

public class RequestSignable {
	public String certb64;
	public String alg;
	public String contentHash;
	public RequestSignable() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RequestSignable(String certb64, String alg, String contentHash) {
		super();
		this.certb64 = certb64;
		this.alg = alg;
		this.contentHash = contentHash;
	}
	@Override
	public String toString() {
		return "RequestSignable [certb64=" + certb64 + ", alg=" + alg + ", contentHash=" + contentHash + "]";
	}
	public String getCertb64() {
		return certb64;
	}
	public String getAlg() {
		return alg;
	}
	public String getContentHash() {
		return contentHash;
	}

}
