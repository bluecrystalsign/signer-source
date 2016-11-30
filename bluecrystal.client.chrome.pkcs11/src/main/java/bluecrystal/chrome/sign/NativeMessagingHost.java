package bluecrystal.chrome.sign;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.security.pkcs11.wrapper.PKCS11Exception;
import bluecrystal.deps.pkcs11.util.Base64Coder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NativeMessagingHost {
	// static {
	// System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY,
	// "DEBUG");
	// System.setProperty(org.slf4j.impl.SimpleLogger.LOG_FILE_KEY,
	// "~/Library/Assijus/log.txt");
	// }
	static final Logger LOG = LoggerFactory
			.getLogger(NativeMessagingHost.class);

	static class CurrentCert {
		String alias = null;
		String certificate = null;
		String subject = null;
		String userPIN = null;
		int keySize = 0;
	}

	public static CurrentCert current = new CurrentCert();

	private static Pkcs11Wrapper p11 = null;

	public static void main(String[] args) throws Exception {
		// TODO: esse init deve ser executado pelo GET de um método chamado
		// /init
		p11 = new Pkcs11Wrapper(
				"aetpkss1.dll;eTPKCS11.dll;asepkcs.dll;libaetpkss.dylib;libeTPkcs11.dylib",
				"/usr/local/lib");
		for (;;) {

			byte[] bytes;
			try {
				bytes = read(4);
			} catch (IndexOutOfBoundsException iobe) {
				break;
			}
			int requestLength = getInt(bytes);

			if (requestLength == 0)
				break;

			byte[] request = read(requestLength);
			String sReq = new String(request, StandardCharsets.UTF_8);
			LOG.debug("mensagem recebida:" + sReq);
			String sResp = run(sReq);
			LOG.debug("mensagem enviada: " + sResp);
			byte[] response = sResp.getBytes(StandardCharsets.UTF_8);
			write(response);
		}
	}

	private static int getInt(byte[] bytes) {
		return (bytes[3] << 24) & 0xff000000 | (bytes[2] << 16) & 0x00ff0000
				| (bytes[1] << 8) & 0x0000ff00 | (bytes[0] << 0) & 0x000000ff;
	}

	private static byte[] getBytes(int length) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (length & 0xFF);
		bytes[1] = (byte) ((length >> 8) & 0xFF);
		bytes[2] = (byte) ((length >> 16) & 0xFF);
		bytes[3] = (byte) ((length >> 24) & 0xFF);
		return bytes;
	}

	private static byte[] read(int count) throws IOException {
		byte[] bytes = new byte[count];
		int off = 0;
		int len = count;
		while (len > 0) {
			int read = System.in.read(bytes, off, len);
			off += read;
			len -= read;
		}
		return bytes;
	}

	public static void write(byte[] bytes) throws IOException {
		int l = bytes.length;
		System.out.write(getBytes(l));
		System.out.write(bytes, 0, bytes.length);
	}

	final static Gson gson = new Gson();

	private static String run(String msg) {
		String jsonOut = "";
		try {
			GenericRequest genericrequest = gson.fromJson(msg,
					GenericRequest.class);

			if (genericrequest.url.endsWith("/test"))
				jsonOut = test();
			else if (genericrequest.url.endsWith("/currentcert"))
				jsonOut = currentcert();
			else if (genericrequest.url.endsWith("/cert"))
				jsonOut = cert(genericrequest.data);
			else if (genericrequest.url.endsWith("/token"))
				jsonOut = token(genericrequest.data);
			else if (genericrequest.url.endsWith("/sign"))
				jsonOut = sign(genericrequest.data);
			else
				return "{\"success\":false,\"data\":{\"errormsg\":\"Error 404: file not found\"}}";
			return "{\"success\":true,\"data\":" + jsonOut + "}";
		} catch (Exception ex) {
			// LOG.error("Mensagem não pode ser processada", ex);
			String message = ex.getMessage();
			if (message != null
					&& message.startsWith("O conjunto de chaves não"))
				message = "Não localizamos nenhum Token válido no computador. Por favor, verifique se foi corretamente inserido.";
			return "{\"success\":false,\"status\":500,\"data\":{\"errormsg\":\""
					+ jsonStringSafe(message) + "\"}}";
		}
	}

	private static String test() {
		TestResponse testresponse = new TestResponse();
		testresponse.provider = "Assijus Signer Extension - PKCS#11";
		testresponse.version = "1.2.9.0-P11";
		testresponse.status = "OK";
		return gson.toJson(testresponse);
	}

	private static String currentcert() {
		try {
			CertificateResponse certificateresponse = new CertificateResponse();
			certificateresponse.subject = current.subject;
			certificateresponse.certificate = current.certificate;
			// if (sorn(certificateresponse.subject) != null) {
			// certificateresponse.certificate = getCertificate(
			// "Assinatura Digital",
			// "Escolha o certificado que será utilizado na assinatura.",
			// certificateresponse.subject, "");
			// certificateresponse.subject = current.subject;
			// }

			if (sorn(certificateresponse.subject) == null) {
				certificateresponse.subject = null;
				certificateresponse.errormsg = "Nenhum certificado ativo no momento.";
			}

			return gson.toJson(certificateresponse);
		} catch (Exception ex) {
			clearCurrentCertificate();
			throw ex;
		}
	}

	private static String cert(RequestData req) throws Exception {
		try {
			current.userPIN = req.userPIN;
			if (current.userPIN == null)
				throw new Exception("PIN não informado");

			String subjectRegEx = "ICP-Brasil";

			if (req != null && sorn(req.subject) != null) {
				subjectRegEx = req.subject;
			}

			CertificateResponse certificateresponse = new CertificateResponse();

			String json = listCerts(p11);

			Type listType = new TypeToken<List<AliasAndSubject>>() {
			}.getType();
			List<AliasAndSubject> list = new Gson().fromJson(json, listType);

			List<AliasAndSubject> filteredlist = new ArrayList<>();
			for (AliasAndSubject aas : list) {
				if (aas.subject != null && aas.subject.equals(subjectRegEx)) {
					filteredlist.add(aas);
					break;
				}
			}
			if (filteredlist.size() == 0) {
				for (AliasAndSubject aas : list) {
					if (aas.subject != null
							&& aas.subject.contains(subjectRegEx))
						filteredlist.add(aas);
				}
			}

			if (filteredlist.size() == 0) {
				certificateresponse.errormsg = "Nenhum certificado encontrado.";
			} else if (filteredlist.size() == 1) {
				current.alias = filteredlist.get(0).alias;
				current.subject = filteredlist.get(0).subject;
				current.certificate = p11.getCert(current.alias);
				current.keySize = p11.getKeySize(current.alias);
				certificateresponse.certificate = current.certificate;
				certificateresponse.subject = current.subject;
			} else if (filteredlist.size() > 1) {
				certificateresponse.list = filteredlist;
			}
			return gson.toJson(certificateresponse);
		} catch (Exception ex) {
			clearCurrentCertificate();
			throw ex;
		}

	}

	private static String token(RequestData req) throws Exception {
		try {
			if (current.userPIN == null)
				throw new Exception("PIN não informado");

			if (req.subject != null) {
				String s = getCertificateBySubject(req.subject);
			}

			if (!req.token.startsWith("TOKEN-"))
				throw new Exception("Token should start with TOKEN-.");

			if (req.token.length() > 128 || req.token.length() < 16)
				throw new Exception("Token too long or too shor.");

			byte[] datetime = req.token.getBytes(StandardCharsets.UTF_8);
			String payloadAsString = new String(Base64Coder.encode(datetime));

			TokenResponse tokenresponse = new TokenResponse();
			for (int i = 0;; i++) {
				try {
					LOG.debug("tentanto gerar token.");
					int alg = 99;

					tokenresponse.sign = p11Sign(p11, alg, payloadAsString);
					break;
				} catch (Exception e) {
					if (i > 10)
						throw e;
					if (!"Private keys must be instance of RSAPrivate(Crt)Key or have PKCS#8 encoding"
							.equals(e.getMessage())) {
						throw e;
					}
				}
			}
			tokenresponse.subject = current.subject;
			tokenresponse.token = req.token;

			return gson.toJson(tokenresponse);
		} catch (Exception ex) {
			clearCurrentCertificate();
			throw ex;
		}
	}

	private static String sign(RequestData req) throws Exception {
		try {
			if (current.userPIN == null)
				throw new Exception("PIN não informado");

			if (req.subject == null) {
				String s = getCertificateBySubject(req.subject);
			}

			int keySize = current.keySize;
			SignResponse signresponse = new SignResponse();
			for (int i = 0;; i++) {
				try {
					LOG.debug("tentanto assinar.");
					int alg;
					if ("PKCS7".equals(req.policy))
						alg = 99;
					else if (keySize < 2048)
						alg = 0;
					else
						alg = 2;
					signresponse.sign = p11Sign(p11, alg, req.payload);
					break;
				} catch (Exception e) {
					if (i > 10)
						throw e;
					if (!"Private keys must be instance of RSAPrivate(Crt)Key or have PKCS#8 encoding"
							.equals(e.getMessage())) {
						throw e;
					}
				}
			}

			signresponse.subject = current.subject;

			return gson.toJson(signresponse);
		} catch (Exception ex) {
			clearCurrentCertificate();
			throw ex;
		}

	}

	private static void clearCurrentCertificate() {
		current.alias = null;
		current.certificate = null;
		current.subject = null;
		current.userPIN = null;
		current.keySize = 0;
	}

	private static String getCertificateBySubject(String sub) {

		return null;
	}

	public static String listCerts(Pkcs11Wrapper p11wrap) throws Exception {
		try {
			p11wrap.setUserPIN(current.userPIN);
			p11wrap.loadKeyStore();
			p11wrap.setUserPIN(current.userPIN);
			p11wrap.refreshCerts();
			p11wrap.setStore(0);

			String ret = "";
			String json = p11wrap.loadCertsJson();
			return json;
		} catch (Exception e) {
			LOG.error("can't load que keystore", e);
			if (e instanceof IOException) {
				if (e.getCause() != null
						&& e.getCause() instanceof FailedLoginException
						|| e.getCause() instanceof LoginException) {
					if (e.getCause().getCause() != null
							&& e.getCause().getCause() instanceof PKCS11Exception) {
						throw (PKCS11Exception) e.getCause().getCause();
					}
				}
			}
			throw new Exception("Não foi possível acessar o token", e);
		}
	}

	public static String p11Sign(Pkcs11Wrapper p11, int alg, String payload)
			throws Exception {
		p11.setUserPIN(current.userPIN);
		p11.setCertAlias(current.alias);
		p11.setOrig(payload);
		p11.setAlg(alg);
		p11.setStore(0);
		p11.sign();
		String ret = p11.getResult();
		return ret;
	}

	private static String jsonStringSafe(String s) {
		if (s == null)
			return "null";
		s = s.replace("\r", " ");
		s = s.replace("\n", " ");
		return s;
	}

	private static String sorn(String s) {
		if (s == null)
			return null;
		if (s.trim().length() == 0)
			return null;
		return s;
	}

	private static class AliasAndSubject {
		String alias;
		String subject;
	}

	private static class RequestData {
		String certificate;
		String subject;
		String payload;
		String policy;
		String code;
		String token;
		String userPIN;
	}

	private static class GenericRequest {
		String url;
		RequestData data;
	}

	private static class GenericResponse {
		String errormsg;
	}

	private static class TestResponse {
		String provider;
		String version;
		String status;
		String errormsg;
	}

	private static class CertificateResponse {
		String certificate;
		String subject;
		String errormsg;
		List<AliasAndSubject> list;
	}

	private static class SignResponse {
		String sign;
		String signkey;
		String subject;
		String errormsg;
	}

	private static class TokenResponse {
		String sign;
		String token;
		String subject;
		String errormsg;
	}

}
