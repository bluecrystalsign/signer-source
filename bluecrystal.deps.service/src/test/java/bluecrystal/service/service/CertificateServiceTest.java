package bluecrystal.service.service;

import static org.junit.Assert.fail;

import java.security.cert.X509Certificate;

import org.bouncycastle.util.encoders.Base64;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class CertificateServiceTest {
	private static final String NAO_TROUXE_O_SUBJECT_DN_ESPERADO = "parsing do certificado não trouxe o Subject DN esperado";
	private static CertificateService certServ;

	@BeforeClass
	public static void oneTimeSetUp() {
		// one-time initialization code
		System.out.println("@BeforeClass - oneTimeSetUp");
		try {
			certServ = new CertificateService();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void oneTimeTearDown() {
		// one-time cleanup code
		System.out.println("@AfterClass - oneTimeTearDown");
		certServ = null;
	}

	
	@Test
	public void testCertificateService() {
		// fail("Not yet implemented");
	}

//	@Test
	public void testCreateFromB64() {
		try {
			String x509B64 = ConfigTestParms.getCertB64();
			byte[] x509 = Base64.decode(x509B64);
			X509Certificate cert = certServ.createFromB64(x509);
			println(cert.getSubjectDN().toString());
			if(cert.getSubjectDN().toString().compareTo(ConfigTestParms.getSubjectDN())!=0){
				fail(NAO_TROUXE_O_SUBJECT_DN_ESPERADO);				
			} 

		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	private void println(String string) {
		System.out.println(string);
		
	}

	@Test
	public void testGetIntermCaList() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetTrustAnchorList() {
		// fail("Not yet implemented");
	}

	@Test
	public void testIsValidForSign() {
		// fail("Not yet implemented");
	}

	@Test
	public void testParseChainAsMap() {
		// fail("Not yet implemented");
	}

	@Test
	public void testIsValidDateX509Certificate() {
		// fail("Not yet implemented");
	}

	@Test
	public void testIsValidDateX509CertificateBoolean() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildPath() {
		// fail("Not yet implemented");
	}

	@Test
	public void testDecode() {
		// fail("Not yet implemented");
	}

	@Test
	public void testDecodeEE() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBuildPathUsingAIA() {
		// fail("Not yet implemented");
	}

	@Test
	public void testIsRoot() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetAIA() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetAIAComplete() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetCrlDistributionPointsX509Certificate() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetCrlDistributionPointsByteArray() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetCertPolicies() {
		// fail("Not yet implemented");
	}

	@Test
	public void testObject() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetClass() {
		// fail("Not yet implemented");
	}

	@Test
	public void testHashCode() {
		// fail("Not yet implemented");
	}

	@Test
	public void testEquals() {
		// fail("Not yet implemented");
	}

	@Test
	public void testClone() {
		// fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		// fail("Not yet implemented");
	}

	@Test
	public void testNotify() {
		// fail("Not yet implemented");
	}

	@Test
	public void testNotifyAll() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWaitLong() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWaitLongInt() {
		// fail("Not yet implemented");
	}

	@Test
	public void testWait() {
		// fail("Not yet implemented");
	}

	@Test
	public void testFinalize() {
		// fail("Not yet implemented");
	}

}
