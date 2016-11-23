package bluecrystal.chrome.sign;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SignAppletP11Test {
	SignAppletP11 signer = new SignApplet();

	@Before
	public void setUp() throws Exception {
		signer.init(
				"aetpkss1.dll;eTPKCS11.dll;asepkcs.dll;libaetpkss.dylib;libeTPkcs11.dylib",
				"/usr/local/lib");
	}

	@Test
	public void testListCerts() throws Exception {
		String json = signer.listCerts(0, "");
		System.out.println(json);
	}
}
