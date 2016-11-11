package bluecrystal.deps.pkcs11;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.Provider;

public class Pkcs11Ref {
	
	private String PKCS11Library = null;
	private String configString = null;
	private ByteArrayInputStream configStream = null;
	private KeyStore keyStore = null;
	private Provider pkcs11Provider = null;
	public String getPKCS11Library() {
		return PKCS11Library;
	}
	public void setPKCS11Library(String pKCS11Library) {
		PKCS11Library = pKCS11Library;
	}
	public String getConfigString() {
		return configString;
	}
	public void setConfigString(String configString) {
		this.configString = configString;
	}
	public ByteArrayInputStream getConfigStream() {
		return configStream;
	}
	public void setConfigStream(ByteArrayInputStream configStream) {
		this.configStream = configStream;
	}
	public KeyStore getKeyStore() {
		return keyStore;
	}
	public void setKeyStore(KeyStore keyStore) {
		this.keyStore = keyStore;
	}
	public Provider getPkcs11Provider() {
		return pkcs11Provider;
	}
	public void setPkcs11Provider(Provider pkcs11Provider) {
		this.pkcs11Provider = pkcs11Provider;
	}

}
