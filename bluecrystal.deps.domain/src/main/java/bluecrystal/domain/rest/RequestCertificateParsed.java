package bluecrystal.domain.rest;

public class RequestCertificateParsed {
	public String certb64;
	public boolean validate;
	public RequestCertificateParsed() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RequestCertificateParsed(String certb64, boolean validate) {
		super();
		this.certb64 = certb64;
		this.validate = validate;
	}
	public String getCertb64() {
		return certb64;
	}
	public void setCertb64(String certb64) {
		this.certb64 = certb64;
	}
	public boolean isValidate() {
		return validate;
	}
	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	@Override
	public String toString() {
		return "RequestCertificateParsed [certb64=" + certb64 + ", validate=" + validate + "]";
	}

}
