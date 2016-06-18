package bluecrystal.service.service;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.util.encoders.Base64;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;



public class CryptoServiceImplTest {
	private static CryptoService ccServ = null;

	@BeforeClass
	public static void oneTimeSetUp() {
		// one-time initialization code
		System.out.println("@BeforeClass - oneTimeSetUp");
		try {
			ccServ = new CryptoServiceImpl();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void oneTimeTearDown() {
		// one-time cleanup code
		System.out.println("@AfterClass - oneTimeTearDown");
		ccServ = null;
	}

	@Test
	public void testDoIt() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testCryptoServiceImpl() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testHashSignedAttribSha1() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testHashSignedAttribSha256() {
		try {
			
			String origHashB64 = ConfigTestParms.getOrigHashB64();
			Date signingTime = ConfigTestParms.getSigningTime();
			String x509B64 = ConfigTestParms.getCertB64();

			println("hashSignedAttribSha256: " + "\norigHashB64 (" + origHashB64
					+ ")" + "\nsigningTime(" + signingTime + ")" + "\nx509B64("
					+ x509B64 + ")");

			byte[] origHash = Base64.decode(origHashB64);
			byte[] x509 = Base64.decode(x509B64);

			X509Certificate cert = createFromB64(x509);
			
			byte[] ret = ccServ.hashSignedAttribSha256(origHash, signingTime,
					cert);
			
			String retAsStr = new String (Base64.encode(ret));
			println( "Resultado: "+ retAsStr );
//			String expected = "MYIB+DAcBgkqhkiG9w0BCQUxDxcNMTYwNjAzMTc1ODA4WjCBlAYLKoZIhvcNAQkQAg8xgYQwgYEGCGBMAQcBAQIBMC8wCwYJYIZIAWUDBAIBBCDdV8mKQxO8E5jOZUPTgCRYlXz3Fq4ylOxNjCYlEpHmwTBEMEIGCyqGSIb3DQEJEAUBFjNodHRwOi8vcG9saXRpY2FzLmljcGJyYXNpbC5nb3YuYnIvUEFfQURfUkJfdjJfMS5kZXIwgfUGCyqGSIb3DQEJEAIvMYHlMIHiMIHfMIHcBCAAEw3UUca+5nzBLIoc3vfiG8F30e4xPj3zZDXDyFDJ1zCBtzCBoaSBnjCBmzELMAkGA1UEBhMCR0IxGzAZBgNVBAgTEkdyZWF0ZXIgTWFuY2hlc3RlcjEQMA4GA1UEBxMHU2FsZm9yZDEaMBgGA1UEChMRQ09NT0RPIENBIExpbWl0ZWQxQTA/BgNVBAMTOENPTU9ETyBTSEEtMjU2IENsaWVudCBBdXRoZW50aWNhdGlvbiBhbmQgU2VjdXJlIEVtYWlsIENBAhEAzzpP0w/pSB6u+JhCoT3oKzAvBgkqhkiG9w0BCQQxIgQgDePcPXQKgM/dnjrXMLb/2FeLguBpfCtPGDx9ldJg338wGAYJKoZIhvcNAQkDMQsGCSqGSIb3DQEHAQ==";
			String expected = ConfigTestParms.getHashSignedAttribSha256();
			if(retAsStr.compareTo(expected)!=0){
				fail("resultado não é o esperado");
			} else {
				println("Resultado esperado OK");
			}

			
			
		} catch (Throwable e) {
			// TODO: handle exception
		}

	
	}

	@Test
	public void testExtractSignature() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testExtractHashId() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testExtractSignCompare() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testExtractSignCompare2() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testExtractDTOSignPolicyOid() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testValidateSignByContent() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testValidateSign() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testValidateSignatureByPolicySignPolicyRefSignCompare() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testExtractVerifyRefence() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testComposeBodySha1() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testComposeBodySha256ByteArrayX509CertificateByteArrayDate() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testComposeBodySha256ListOfAppSignedInfo() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testCalcSha1() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testCalcSha256() {
		//  fail("Not yet implemented");
	}

	@Test
	public void testValidateSignatureByPolicyByteArrayByteArray() {
		//  fail("Not yet implemented");
	}
	private void println(String string) {
		System.out.println(string);
		
	}
	public X509Certificate createFromB64(byte[] certB64)
			throws CertificateException {
		ByteArrayInputStream is = new ByteArrayInputStream(certB64);
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		Certificate c = cf.generateCertificate(is);
		return (X509Certificate) c;
	}

}
