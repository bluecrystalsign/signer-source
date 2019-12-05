package bluecrystal.service.v1.icpbr;

import static org.junit.Assert.fail;

import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.util.encoders.Base64;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import bluecrystal.service.service.CertificateService;
import bluecrystal.service.service.CryptoService;
import bluecrystal.service.service.CryptoServiceImpl;
import bluecrystal.service.service.SignVerifyService;
import bluecrystal.service.service.Validator;
import bluecrystal.service.service.ValidatorSrv;


public class IcpbrServiceImplTest {

	private static CryptoService ccServ = null;
	private static SignVerifyService verify = null;
	private static CertificateService certServ = null;
	private static ValidatorSrv validatorServ = null;

//	@BeforeClass
	public static void oneTimeSetUp() {
		// one-time initialization code
		System.out.println("@BeforeClass - oneTimeSetUp");
		try {
			ccServ = new CryptoServiceImpl();
			verify = new SignVerifyService();
			certServ = new CertificateService();
			validatorServ = new Validator();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	@AfterClass
	public static void oneTimeTearDown() {
		// one-time cleanup code
		System.out.println("@AfterClass - oneTimeTearDown");
		ccServ = null;
		verify = null;
		certServ = null;
		validatorServ = null;
	}

	
	@Test
	public void testIcpbrServiceImpl() {
		// fail("Not yet implemented");
	}

	@Test
	public void testHashSignedAttribADRB10() {
		// fail("Not yet implemented");
	}

//	@Test
	public void testHashSignedAttribADRB21() {

		try {
			String origHashB64 = ConfigTestParms.getOrigHashB64();
//			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM kk:mm:ss Z yyyy");
//			Date signingTime = sdf.parse("Fri Jun 03 14:58:08 BRT 2016");
			Date signingTime = ConfigTestParms.getSigningTime();
			String x509B64 = ConfigTestParms.getCertB64();

			println("hashSignedAttribSha256: " + "\norigHashB64 (" + origHashB64
					+ ")" + "\nsigningTime(" + signingTime + ")" + "\nx509B64("
					+ x509B64 + ")");

			byte[] origHash = Base64.decode(origHashB64);
			byte[] x509 = Base64.decode(x509B64);
			X509Certificate cert = certServ.createFromB64(x509);

			byte[] ret = ccServ.hashSignedAttribSha256(origHash, signingTime,
					cert);

			String retAsStr = new String( Base64.encode(ret) );
			println( "Resultado: "+ retAsStr );
			String expected = ConfigTestParms.getHashSignedAttribADRB21();
			
			if(retAsStr.compareTo(expected)!=0){
				fail("resultado não é o esperado");
			} else {
				println("Resultado esperado OK");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}

	}


	@Test
	public void testExtractSignature() {
		// fail("Not yet implemented");
	}

	@Test
	public void testComposeEnvelopeADRB10() {
		// fail("Not yet implemented");
	}

	@Test
	public void testComposeEnvelopeADRB21() {
		// fail("Not yet implemented");
	}

	@Test
	public void testComposeCoSignEnvelopeADRB21() {
		// fail("Not yet implemented");
	}

	@Test
	public void testExtractSignCompare() {
		// fail("Not yet implemented");
	}

	@Test
	public void testValidateSignatureByPolicy() {
		// fail("Not yet implemented");
	}

	@Test
	public void testExtractSignerCert() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetCertSubject() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetCertSubjectCn() {
		// fail("Not yet implemented");
	}

	@Test
	public void testValidateSign() {
		// fail("Not yet implemented");
	}

	@Test
	public void testValidateSignWithStatus() {
		// fail("Not yet implemented");
	}

	@Test
	public void testParseCertificate() {
		// fail("Not yet implemented");
	}

	private void println(String string) {
		System.out.println(string);
		
	}

}
