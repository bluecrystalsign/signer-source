package bluecrystal.service.service;

import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.bouncycastle.util.encoders.Base64;

import bluecrystal.domain.NameValue;
import bluecrystal.domain.OperationStatus;
import bluecrystal.service.exception.InvalidSigntureException;
import bluecrystal.service.helper.UtilsLocal;
import bluecrystal.service.jwt.Claim;
import bluecrystal.service.jwt.Credential;
import bluecrystal.service.jwt.JwtService;

public class ServiceFacade {
	public static final int NDX_SHA1 = 0;
	public static final int NDX_SHA224 = 1;
	public static final int NDX_SHA256 = 2;
	public static final int NDX_SHA384 = 3;
	public static final int NDX_SHA512 = 4;

	private CryptoService ccServ = null;
	private CertificateService certServ = null;
	private SignVerifyService verify = null;
	private ValidatorSrv validatorServ = null;
	private JwtService jwtServ = null;

	public ServiceFacade() {
		super();
		ccServ = new CryptoServiceImpl();
		certServ = new CertificateService();
		verify = new SignVerifyService();
		validatorServ = new Validator();
		jwtServ = new JwtService();
	}

	public byte[] calcSha256(byte[] content) throws NoSuchAlgorithmException {
		return ccServ.calcSha256(content);
	}
	public String hashSignedAttribADRB21(String origHashB64, Date signingTime, String x509B64) throws Exception {
		// logger.debug("hashSignedAttribSha256: " + "\norigHashB64 (" +
		// origHashB64
		// + ")" + "\nsigningTime(" + signingTime + ")" + "\nx509B64("
		// + x509B64 + ")");
		try {
			byte[] origHash = Base64.decode(origHashB64);
			byte[] x509 = Base64.decode(x509B64);
			X509Certificate cert = certServ.createFromB64(x509);

			byte[] ret = ccServ.hashSignedAttribSha256(origHash, signingTime, cert);

			return new String(Base64.encode(ret));
		} catch (Exception e) {
			// ;logger.error("ERRO: ", e);
			throw e;
		}
	}

	public String hashSignedAttribADRB10(String origHashB64, Date signingTime, String x509B64) throws Exception {

		// logger.debug("hashSignedAttribSha1: " + "\norigHashB64 (" +
		// origHashB64
		// + ")" + "\nsigningTime(" + signingTime + ")" + "\nx509B64("
		// + x509B64 + ")");

		try {
			byte[] origHash = Base64.decode(origHashB64);
			byte[] x509 = Base64.decode(x509B64);
			X509Certificate cert = certServ.createFromB64(x509);

			byte[] ret = ccServ.hashSignedAttribSha1(origHash, signingTime, cert);

			return new String(Base64.encode(ret));
		} catch (Exception e) {
			// logger.error("ERRO: ", e);
			throw e;
		}

	}

	public String composeEnvelopeADRB21(String signB64, String x509B64, String origHashB64, Date signingTime)
			throws Exception {
		// logger.debug("composeBodySha256: " + "\nsignB64 (" + signB64 + ")"
		// + "\nx509B64 (" + x509B64 + ")" + "\norigHashB64 ("
		// + origHashB64 + ")" + "\nsigningTime (" + signingTime + ")");
		try {
			byte[] sign = Base64.decode(signB64);
			byte[] origHash = Base64.decode(origHashB64);
			byte[] x509 = Base64.decode(x509B64);
			X509Certificate cert = certServ.createFromB64(x509);

			byte[] hashSa = ccServ.hashSignedAttribSha256(origHash, signingTime, cert);

			if (!verify.verify(NDX_SHA256, ccServ.calcSha256(hashSa), sign, cert)) {
				// logger.error("Verificação retornou erro, lancando
				// InvalidSigntureException.");
				throw new InvalidSigntureException();
			} else {
				// logger.debug("Assinatura verificada com sucesso!");
			}

			byte[] ret = ccServ.composeBodySha256(sign, cert, origHash, signingTime);

			return new String(Base64.encode(ret));
		} catch (Exception e) {
			// logger.error("ERRO: ", e.getLocalizedMessage());
			// logger.error(LogManager.exceptionToString(e));
			throw e;
		}
	}

	public Date parseDate(String srcDate) throws ParseException{
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date result1 = df1.parse(srcDate);
		return result1;
	}

	public String parseDate(Date srcDate) throws ParseException{
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String result1 = df1.format(srcDate);
		return result1;
	}

//	private int validateSignWithStatus(Boolean algSha256, String ret, String filename) throws Exception {
//		
//		MessageDigest hashSum = null;
//		if(algSha256){
//			hashSum = MessageDigest.getInstance("SHA-256");
//			logger.debug("Validar assinatura SHA-256");
//
//		} else {
//			hashSum = MessageDigest.getInstance("SHA-1");
//			logger.debug("Validar assinatura SHA-1");
//		}
//		hashSum.update(Convert.readFile(filename));
//		byte[] digestResult = hashSum.digest();
//		
////		Base64.Encoder encoder = Base64.getEncoder(); 
//		String digestB64 = new String(Base64.encode(digestResult));
//		return serv.validateSignWithStatus(ret, digestB64, Convert.asXMLGregorianCalendar(new Date()), false);
//	}

	
//	Validate CMS envelope
	public int validateSignWithStatus(String signCms, String origHashb64, Date dtSign,
			boolean verifyCRL) throws Exception {
//		logger.debug("validateSign: " + "\n signCms (" + signCms + ")"
//				+ "\n content (" + origHashb64 + ")" + "\n dtSign (" + dtSign + ")"
//				+ "\n verifyCRL (" + verifyCRL + ")");
		try {
			byte[] sign = Base64.decode(signCms);
			byte[] origHash = Base64.decode(origHashb64);

			int validateSign = ccServ.validateSign(sign, origHash, dtSign, verifyCRL);
			return validateSign;
		} catch (Exception e) {
//			logger.error("ERRO: ", e);
			throw e;
		}
	}

	public String extractSignerCert(String signb64) throws Exception {
//		logger.debug("extractSignCompare: " + "\nsignb64 (" + signb64 + ")");
		try {
			byte[] sign = Base64.decode(signb64);
			X509Certificate certEE = certServ.decodeEE(sign);
			return new String ( Base64.encode(certEE.getEncoded() ));
		} catch (Exception e) {
//			logger.error("ERRO: ", e);
			throw e;
		}
	}

	public String getCertSubject(String cert) throws Exception {
//		logger.debug("getCertSubject: " + "\ncert (" + cert + ")");
		try {

			Map<String, String> certEE = validatorServ.parseCertificateAsMap(cert);

			return certEE.get("subject0");
		} catch (Exception e) {
//			logger.error("ERRO: ", e);
			throw e;
		}

	}

	public OperationStatus isValid(Date refDate, String certb64,
			boolean verifyRevoke) throws Exception
	{
		X509Certificate cert = UtilsLocal.createCert(certb64);
		return certServ.isValid(refDate, cert, verifyRevoke);
	}

	public String preSign(Claim claim, Credential credential) throws Exception {
		return jwtServ.preSign(claim, credential);
	}

	public String sign(Claim claim, Credential credential) throws Exception {
		return jwtServ.sign(claim, credential);
	}
	
	public String postSign(Credential credential, String preSigned, String signed) throws Exception {
		
		return jwtServ.postSign(credential, preSigned, Base64.decode(signed));
	}
	
	public Claim verify(Credential credential, String token) throws Exception {
		
		return jwtServ.verify(token, credential);
	}

	public NameValue[] parseCertificate(String certificate) throws Exception{
		return validatorServ.parseCertificate(certificate);
	}
	
}
