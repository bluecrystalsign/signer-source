package bluecrystal.domain.rest;

import java.util.Arrays;

import bluecrystal.domain.NameValue;

public class ResponseCertificateParsed {
	private NameValue[] certificate;
	
	
//	@DynamoDBAttribute(attributeName="subjectRdn")
	private NameValue[] subjectRdn;
	private NameValue[] issuerRdn;
	private int status;
	public ResponseCertificateParsed() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseCertificateParsed(NameValue[] certificate, NameValue[] subjectRdn, NameValue[] issuerRdn,
			int status) {
		super();
		this.certificate = certificate;
		this.subjectRdn = subjectRdn;
		this.issuerRdn = issuerRdn;
		this.status = status;
	}

	public NameValue[] getCertificate() {
		return certificate;
	}
	public void setCertificate(NameValue[] certificate) {
		this.certificate = certificate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public NameValue[] getSubjectRdn() {
		return subjectRdn;
	}

	public void setSubjectRdn(NameValue[] subjectRdn) {
		this.subjectRdn = subjectRdn;
	}

	public NameValue[] getIssuerRdn() {
		return issuerRdn;
	}

	public void setIssuerRdn(NameValue[] issuerRdn) {
		this.issuerRdn = issuerRdn;
	}

	@Override
	public String toString() {
		return "ResponseCertificateParsed [certificate=" + Arrays.toString(certificate) + ",\nsubjectRdn="
				+ Arrays.toString(subjectRdn) + ",\nissuerRdn=" + Arrays.toString(issuerRdn) + ",\nstatus=" + status
				+ "]";
	}
	
}
