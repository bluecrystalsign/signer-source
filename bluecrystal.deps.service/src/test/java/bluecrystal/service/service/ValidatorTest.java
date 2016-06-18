package bluecrystal.service.service;

import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import bluecrystal.domain.NameValue;

public class ValidatorTest {
	private static ValidatorSrv srv;

	@BeforeClass
	public static void oneTimeSetUp() {
		// one-time initialization code
		System.out.println("@BeforeClass - oneTimeSetUp");
		srv = new Validator();
	}

	@AfterClass
	public static void oneTimeTearDown() {
		// one-time cleanup code
		System.out.println("@AfterClass - oneTimeTearDown");
		srv = null;
	}

//	@Test
	public void testParseCertificate() {
		try {
			String certB64 = "MIIFQjCCBCqgAwIBAgIRAM86T9MP6UgerviYQqE96CswDQYJKoZIhvcNAQELBQAwgZsxCzAJBgNVBAYTAkdCMRswGQYDVQQIExJHcmVhdGVyIE1hbmNoZXN0ZXIxEDAOBgNVBAcTB1NhbGZvcmQxGjAYBgNVBAoTEUNPTU9ETyBDQSBMaW1pdGVkMUEwPwYDVQQDEzhDT01PRE8gU0hBLTI1NiBDbGllbnQgQXV0aGVudGljYXRpb24gYW5kIFNlY3VyZSBFbWFpbCBDQTAeFw0xNjA0MjUwMDAwMDBaFw0xNzA0MjUyMzU5NTlaMCYxJDAiBgkqhkiG9w0BCQEWFXNlcmdpby5sZWFsQGdtYWlsLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAOPeujEjVvpLqyactBMhXeeWXSPtANZ9rRxNDB77jhrFwrrEl7UVWl5YiIuUpH1/hYnCjvxsvyxYFeNeYq8lFTk0hbsyrOq7Y19eWjqVlmvMDLUOLJPOYP0KYwtiJBvYAuZRbj04zPxL3QOGKmVBoHpEU4ZoFeibNYk0h7Y8S2+nTBmFUflHAB3VGDZ9FxYrIhDUzr+B56MbVXjYVpRZkHEz2ehgyExZpdoLjqYoeeTQoX99P5AQ7IsQ8dn4C5IlpxSH5b4w6w+PqFb0j6rsO1siESHXkCtzHoQPhtLWRrYZApeE4FELaTIdw2VvCRyGTeGcyCph6eKASgrIgY5YSBMCAwEAAaOCAfMwggHvMB8GA1UdIwQYMBaAFJJha4LhoqCqT+xn8cKj97SAAMHsMB0GA1UdDgQWBBRugGUmugGwJg8NNlUB8dBerdimozAOBgNVHQ8BAf8EBAMCBaAwDAYDVR0TAQH/BAIwADAgBgNVHSUEGTAXBggrBgEFBQcDBAYLKwYBBAGyMQEDBQIwEQYJYIZIAYb4QgEBBAQDAgUgMEYGA1UdIAQ/MD0wOwYMKwYBBAGyMQECAQEBMCswKQYIKwYBBQUHAgEWHWh0dHBzOi8vc2VjdXJlLmNvbW9kby5uZXQvQ1BTMF0GA1UdHwRWMFQwUqBQoE6GTGh0dHA6Ly9jcmwuY29tb2RvY2EuY29tL0NPTU9ET1NIQTI1NkNsaWVudEF1dGhlbnRpY2F0aW9uYW5kU2VjdXJlRW1haWxDQS5jcmwwgZAGCCsGAQUFBwEBBIGDMIGAMFgGCCsGAQUFBzAChkxodHRwOi8vY3J0LmNvbW9kb2NhLmNvbS9DT01PRE9TSEEyNTZDbGllbnRBdXRoZW50aWNhdGlvbmFuZFNlY3VyZUVtYWlsQ0EuY3J0MCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5jb21vZG9jYS5jb20wIAYDVR0RBBkwF4EVc2VyZ2lvLmxlYWxAZ21haWwuY29tMA0GCSqGSIb3DQEBCwUAA4IBAQAMMA7KBsy/WFM6uY2C1B8970HskwDl/2Zs3HUM7PvWzSaA9WgWk3cRLjZF4ggxZ8ylXEQmtEP01tvKhhIt/KtNyj6tg7R9G+10mf8TPBX3ELIWFafiLWdOxyLXBnebkUNUJGZ0POyldUMQXrJHT719uNQbZ/ovJEPe6FDO2YcjbXlC3MNsY8YmHt9QJy0ATwLPH+emWeGoXqM/1AKrSnuatkp03hHSr4oou23NGpgPG2pdhwTZKAqXpzMxj3+HA02uVB62FN1GaIHsqZp6qQRsbhAtC2CfT1S2yN8voROs3DX8+Mgkpf7mXXBQeQ2GP5/i6sbU4/FFPrXHZjIa3FhV";

			NameValue[] ret = srv.parseCertificate(certB64);
			System.out.println("tamanho da lista: "+ret.length);
			if(ret.length != 19){
				fail("deveriam retornar 19 mas voltaram "+ret.length);
			}
			for(NameValue next : ret){
				System.out.println(next.getName()+" -> "+next.getValue());
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

//	@Test
	public void testParseCertificateAsMap() {
			try {
//				String certB64 = "MIIFQjCCBCqgAwIBAgIRAM86T9MP6UgerviYQqE96CswDQYJKoZIhvcNAQELBQAwgZsxCzAJBgNVBAYTAkdCMRswGQYDVQQIExJHcmVhdGVyIE1hbmNoZXN0ZXIxEDAOBgNVBAcTB1NhbGZvcmQxGjAYBgNVBAoTEUNPTU9ETyBDQSBMaW1pdGVkMUEwPwYDVQQDEzhDT01PRE8gU0hBLTI1NiBDbGllbnQgQXV0aGVudGljYXRpb24gYW5kIFNlY3VyZSBFbWFpbCBDQTAeFw0xNjA0MjUwMDAwMDBaFw0xNzA0MjUyMzU5NTlaMCYxJDAiBgkqhkiG9w0BCQEWFXNlcmdpby5sZWFsQGdtYWlsLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAOPeujEjVvpLqyactBMhXeeWXSPtANZ9rRxNDB77jhrFwrrEl7UVWl5YiIuUpH1/hYnCjvxsvyxYFeNeYq8lFTk0hbsyrOq7Y19eWjqVlmvMDLUOLJPOYP0KYwtiJBvYAuZRbj04zPxL3QOGKmVBoHpEU4ZoFeibNYk0h7Y8S2+nTBmFUflHAB3VGDZ9FxYrIhDUzr+B56MbVXjYVpRZkHEz2ehgyExZpdoLjqYoeeTQoX99P5AQ7IsQ8dn4C5IlpxSH5b4w6w+PqFb0j6rsO1siESHXkCtzHoQPhtLWRrYZApeE4FELaTIdw2VvCRyGTeGcyCph6eKASgrIgY5YSBMCAwEAAaOCAfMwggHvMB8GA1UdIwQYMBaAFJJha4LhoqCqT+xn8cKj97SAAMHsMB0GA1UdDgQWBBRugGUmugGwJg8NNlUB8dBerdimozAOBgNVHQ8BAf8EBAMCBaAwDAYDVR0TAQH/BAIwADAgBgNVHSUEGTAXBggrBgEFBQcDBAYLKwYBBAGyMQEDBQIwEQYJYIZIAYb4QgEBBAQDAgUgMEYGA1UdIAQ/MD0wOwYMKwYBBAGyMQECAQEBMCswKQYIKwYBBQUHAgEWHWh0dHBzOi8vc2VjdXJlLmNvbW9kby5uZXQvQ1BTMF0GA1UdHwRWMFQwUqBQoE6GTGh0dHA6Ly9jcmwuY29tb2RvY2EuY29tL0NPTU9ET1NIQTI1NkNsaWVudEF1dGhlbnRpY2F0aW9uYW5kU2VjdXJlRW1haWxDQS5jcmwwgZAGCCsGAQUFBwEBBIGDMIGAMFgGCCsGAQUFBzAChkxodHRwOi8vY3J0LmNvbW9kb2NhLmNvbS9DT01PRE9TSEEyNTZDbGllbnRBdXRoZW50aWNhdGlvbmFuZFNlY3VyZUVtYWlsQ0EuY3J0MCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5jb21vZG9jYS5jb20wIAYDVR0RBBkwF4EVc2VyZ2lvLmxlYWxAZ21haWwuY29tMA0GCSqGSIb3DQEBCwUAA4IBAQAMMA7KBsy/WFM6uY2C1B8970HskwDl/2Zs3HUM7PvWzSaA9WgWk3cRLjZF4ggxZ8ylXEQmtEP01tvKhhIt/KtNyj6tg7R9G+10mf8TPBX3ELIWFafiLWdOxyLXBnebkUNUJGZ0POyldUMQXrJHT719uNQbZ/ovJEPe6FDO2YcjbXlC3MNsY8YmHt9QJy0ATwLPH+emWeGoXqM/1AKrSnuatkp03hHSr4oou23NGpgPG2pdhwTZKAqXpzMxj3+HA02uVB62FN1GaIHsqZp6qQRsbhAtC2CfT1S2yN8voROs3DX8+Mgkpf7mXXBQeQ2GP5/i6sbU4/FFPrXHZjIa3FhV";
				String certB64 = ConfigTestParms.getCertB64();
				Map<String, String> ret = srv.parseCertificateAsMap(certB64);
				System.out.println("tamanho da lista: "+ret.keySet().size());
				if(ret.keySet().size() != 19){
					fail("deveriam retornar 19 mas voltaram "+ret.keySet().size());
				}
				for(String next : ret.keySet()){
					System.out.println(next+" -> "+ret.get(next));
				}
				
			} catch (Throwable e) {
				e.printStackTrace();
				fail(e.getLocalizedMessage());
			}
	}

}
