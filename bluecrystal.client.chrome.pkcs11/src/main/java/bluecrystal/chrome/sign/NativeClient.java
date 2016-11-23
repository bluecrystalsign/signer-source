package bluecrystal.chrome.sign;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import bluecrystal.deps.pkcs11.util.Base64Coder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NativeClient {

	private static SignApplet signer = new SignApplet();

	private static class CurrentCert {
		String alias = null;
		String certificate = null;
		String subject = null;
		String userPIN = null;
		int keySize = 0;
	}

	private static CurrentCert current = new CurrentCert();

	public static void main(String[] args) throws Exception {
		for (;;) {
			byte[] bytes = read(4);
			int requestLength = bytes[0] + bytes[1] * (256 ^ 1) + bytes[2]
					* (256 ^ 2) + bytes[3] * (256 ^ 3);

			if (requestLength == 0)
				break;

			byte[] request = read(requestLength);
			String s = new String(request, StandardCharsets.UTF_8);
			byte[] response = run(s).getBytes(StandardCharsets.UTF_8);
			write(response);
		}
	}

	private static byte[] read(int count) throws IOException {
		byte[] bytes = new byte[count];
		int off = 0;
		int len = 4;
		while (len > 0) {
			int read = System.in.read(bytes, off, len);
			off += read;
			len -= read;
		}
		return bytes;
	}

	public static void write(byte[] bytes) {
		int l = bytes.length;
		for (int i = 0; i < 4; i++) {
			int j = l % 256;
			l = l / 256;
			System.out.write(j);
		}
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
			String message = ex.getMessage();
			if (message.startsWith("O conjunto de chaves não"))
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
			String subjectRegEx = "ICP-Brasil";

			if (req != null && sorn(req.subject) != null) {
				subjectRegEx = req.subject;
			}

			CertificateResponse certificateresponse = new CertificateResponse();

			String json = signer.listCerts(0, current.userPIN);
			Type listType = new TypeToken<List<AliasAndSubject>>() {
			}.getType();
			List<AliasAndSubject> list = new Gson().fromJson(json, listType);
			if (list.size() > 0) {
				current.alias = list.get(0).alias;
				current.subject = list.get(0).subject;
				current.certificate = signer.getCertificate(current.alias);
				current.keySize = signer.getKeySize(current.alias);
			}

			// certificateresponse.certificate = getCertificate(
			// "Assinatura Digital",
			// "Escolha o certificado que será utilizado na assinatura.",
			// subjectRegEx, "");

			certificateresponse.subject = current.subject;

			if (sorn(certificateresponse.certificate) == null) {
				certificateresponse.errormsg = "Nenhum certificado encontrado.";
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
			tokenresponse.sign = signer.sign(0, 99, current.userPIN,
					current.alias, payloadAsString);

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
			if ("PKCS7".equals(req.policy))
				signresponse.sign = signer.sign(0, 99, current.userPIN,
						current.alias, req.payload);
			else if (keySize < 2048)
				signresponse.sign = signer.sign(0, 0, current.userPIN,
						current.alias, req.payload);
			else
				signresponse.sign = signer.sign(0, 2, current.userPIN,
						current.alias, req.payload);

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

	private static String jsonStringSafe(String s) {
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
