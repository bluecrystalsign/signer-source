package bluecrystal.chrome.sign;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class P11ManagerTest {
	Pkcs11Wrapper p11 = new Pkcs11Wrapper(
			"aetpkss1.dll;eTPKCS11.dll;asepkcs.dll;libaetpkss.dylib;libeTPkcs11.dylib",
			"/usr/local/lib");

	@Test
	public void testListCerts() throws Exception {
		if (!hasPIN())
			return;

		String json = NativeMessagingHost.listCerts(p11);

		assertTrue(json
				.contains("\"alias\":\"RENATO DO AMARAL CRIVANO MACHADO:12031\""));
	}

	@Test
	public void testGetCertificate() throws Exception {
		if (!hasPIN())
			return;

		String json = NativeMessagingHost.listCerts(p11);
		String cert = p11.getCert("RENATO DO AMARAL CRIVANO MACHADO:12031");
		assertTrue(cert.startsWith("MII"));
	}

	@Test
	public void testGetSubject() throws Exception {
		if (!hasPIN())
			return;

		String json = NativeMessagingHost.listCerts(p11);
		String subject = p11
				.getSubject("RENATO DO AMARAL CRIVANO MACHADO:12031");
		assertTrue(subject.startsWith("CN="));
	}

	@Test
	public void testGetKeySize() throws Exception {
		if (!hasPIN())
			return;

		String json = NativeMessagingHost.listCerts(p11);
		int keySize = p11.getKeySize("RENATO DO AMARAL CRIVANO MACHADO:12031");
		assertEquals(2048, keySize);
	}

	@Test
	public void testSign() throws Exception {
		if (!hasPIN())
			return;

		String hashADRB21 = "MYIBxDAcBgkqhkiG9w0BCQUxDxcNMTUwOTI1MTgxNTMxWjCBlAYLKoZIhvcNAQkQAg8xgYQwgYEGCGBMAQcBAQIBMC8wCwYJYIZIAWUDBAIBBCDdV8mKQxO8E5jOZUPTgCRYlXz3Fq4ylOxNjCYlEpHmwTBEMEIGCyqGSIb3DQEJEAUBFjNodHRwOi8vcG9saXRpY2FzLmljcGJyYXNpbC5nb3YuYnIvUEFfQURfUkJfdjJfMS5kZXIwgcEGCyqGSIb3DQEJEAIvMYGxMIGuMIGrMIGoBCAyGmv3J9MEA4+9whhexPeq/gdXg0FvpI7Rxo/9GOkQ9zCBgzB3pHUwczELMAkGA1UEBhMCQlIxEzARBgNVBAoTCklDUC1CcmFzaWwxNTAzBgNVBAsTLEF1dG9yaWRhZGUgQ2VydGlmaWNhZG9yYSBkYSBKdXN0aWNhIC0gQUMtSlVTMRgwFgYDVQQDEw9BQyBDQUlYQS1KVVMgdjICCA2MnrldA+dkMC8GCSqGSIb3DQEJBDEiBCAQyMBGVEPRb3+Wv/LWynedl/+5THAqKXTd1SI6G93YNTAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcB";
		String signatureADRB21 = "JDjU/3WROow3b4MDjC+ZRlblOBIccJwSq6xo1OGlTZUUk4abU7Bie4pZrBFFt6M2vyiJSoSFjAXWNxwk4ofpgJUQ1z7MDjTqOzmZ5UeUpd7coS0DCHtgjQlPalrCv2Om6DLmWHJapKNbvDFtYNACLKqX/qwYsNakE8aovpO0O0lTKL8u49QueHx3szGIULUv67o+tGrzy0j68ngiM7GJLUFLU1hGCHShl/mb3xu+os2ncjrAMqUOKtJEO4WyU/fL3ewSb5eDu/ABr2izU4oFsxFBhLCxzMXldZZBKNbU8/6rEDhsE5sRsIbZPmimxUaQrWPFH3qwmilAUTb+si3oVA==";

		String json = NativeMessagingHost.listCerts(p11);
		NativeMessagingHost.current.alias = "RENATO DO AMARAL CRIVANO MACHADO:12031";
		NativeMessagingHost.current.userPIN = getPIN();
		String signature = NativeMessagingHost.p11Sign(p11, 2, hashADRB21);
		assertEquals(signatureADRB21,
				signature.replace("\n", "").replace("\r", ""));
	}

	private String getPIN() {
		return System.getProperty("TOKEN_PASSWORD");
	}

	private boolean hasPIN() {
		if (getPIN() == null) {
			System.out
					.println("No TOKEN_PASSWORD property available, skipping test.");
			return false;
		}
		return true;
	}

}
