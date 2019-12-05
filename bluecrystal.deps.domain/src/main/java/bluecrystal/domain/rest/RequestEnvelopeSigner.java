package bluecrystal.domain.rest;

public class RequestEnvelopeSigner {
private String envelopeSignedB64;


public RequestEnvelopeSigner() {
	super();
	// TODO Auto-generated constructor stub
}


public RequestEnvelopeSigner(String envelopeSigned) {
	super();
	this.envelopeSignedB64 = envelopeSigned;
}


public String getEnvelopeSignedB64() {
	return envelopeSignedB64;
}


public void setEnvelopeSignedB64(String envelopeSignedB64) {
	this.envelopeSignedB64 = envelopeSignedB64;
}


@Override
public String toString() {
	return "RequestEnvelopeSigner [envelopeSignedB64=" + envelopeSignedB64 + "]";
}



}
