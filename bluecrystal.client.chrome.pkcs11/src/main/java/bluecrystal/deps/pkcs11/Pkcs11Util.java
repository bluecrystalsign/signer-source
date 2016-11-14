package bluecrystal.deps.pkcs11;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Pkcs11Util {
	public static final String[] DIGITAL_SIGNATURE_ALGORITHM_NAME = {
			"SHA1withRSA", "SHA224withRSA", "SHA256withRSA", "SHA384withRSA",
			"SHA512withRSA" };


	protected String signFileSignPol(String lastFilePath2, String userPIN2, int alg, String orig)
			throws Exception, KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, InvalidKeyException, SignatureException {
				PrivateKey privateKey = PkiHelper.loadPrivFromP12(
						lastFilePath2, userPIN2);
				X509Certificate certificate = PkiHelper.loadCertFromP12(
						lastFilePath2, userPIN2);
				System.out.println("Certificate: ");
				System.out.println(certificate.getSubjectDN().getName());
				System.out.println(certificate.getNotBefore() + " -> "+certificate.getNotAfter());
				
			
				return performSign(privateKey, certificate,alg, orig);
			}

	public String performSign(PrivateKey privateKey, X509Certificate certificate, int alg, String orig)
			throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException {
				Signature sig = Signature
						.getInstance(DIGITAL_SIGNATURE_ALGORITHM_NAME[alg]);
				sig.initSign(privateKey);
			
				BASE64Decoder b64dec = new BASE64Decoder();
				BASE64Encoder b64enc = new BASE64Encoder();
				byte[] decodeOrig = b64dec.decodeBuffer(orig);
				sig.update(decodeOrig);
				byte[] dataSignature = sig.sign();
			
				String result = b64enc.encode(dataSignature);
				System.out.print("Assinatura: ");
				System.out.println(result);
			
				// Verify signature
				Signature verificacion = Signature
						.getInstance(DIGITAL_SIGNATURE_ALGORITHM_NAME[alg]);
				verificacion.initVerify(certificate);
				verificacion.update(decodeOrig);
				if (verificacion.verify(dataSignature)) {
					System.out.println("Signature verification Succeeded!");
				} else {
					System.out.println("Signature verification FAILED!");
				}
				return result;
			}

}
