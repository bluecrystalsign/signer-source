package bluecrystal.server.rest.controller;

import java.io.InputStream;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bluecrystal.server.rest.domain.EnvelopeRequest;
import bluecrystal.server.rest.domain.EnvelopeResponse;
import bluecrystal.server.rest.domain.HashRequest;
import bluecrystal.server.rest.domain.HashResponse;
import bluecrystal.server.rest.domain.ValidateRequest;
import bluecrystal.server.rest.domain.ValidateResponse;
import bluecrystal.server.rest.util.BlucUtil;
import sun.misc.BASE64Decoder;

@SuppressWarnings("restriction")
@Path("v1")
public class BlucRest {
	static final Logger LOG = LoggerFactory.getLogger(BlucRest.class);
	private BlucUtil util;

	public BlucRest() {
		this.util = new BlucUtil();
	}

	@POST
	@Path("/hash")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response hash(HashRequest req) throws Exception {
		HashResponse resp = new HashResponse();

		try {
			BASE64Decoder b64dec = new BASE64Decoder();
			if (!("AD-RB".equals(req.getPolicy()) || "PKCS#7".equals(req
					.getPolicy())))
				throw new Exception(
						"Parameter 'policy' should be either 'AD-RB' or 'PKCS#7'");

			boolean policy = "AD-RB".equals(req.getPolicy());
			byte[] certificate = b64dec.decodeBuffer(req.getCertificate());
			byte[] sha1 = b64dec.decodeBuffer(req.getSha1());
			byte[] sha256 = b64dec.decodeBuffer(req.getSha256());
			Date dtSign = javax.xml.bind.DatatypeConverter.parseDateTime(
					req.getTime()).getTime();
			boolean verifyCRL = "true".equals(req.getCrl());

			util.produzPacoteAssinavel(certificate, sha1, sha256, policy,
					dtSign, resp);

			return Response.status(200).entity(resp).build();
		} catch (Exception e) {
			resp.setError(e.toString());
			return Response.status(500).entity(resp).build();
		}
	}

	@POST
	@Path("/envelope")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response envelope(EnvelopeRequest req) throws Exception {
		EnvelopeResponse resp = new EnvelopeResponse();

		try {
			BASE64Decoder b64dec = new BASE64Decoder();
			if (!("AD-RB".equals(req.getPolicy()) || "PKCS#7".equals(req
					.getPolicy())))
				throw new Exception(
						"Parameter 'policy' should be either 'AD-RB' or 'PKCS#7'");

			boolean policy = "AD-RB".equals(req.getPolicy());
			byte[] certificate = b64dec.decodeBuffer(req.getCertificate());
			byte[] sha1 = b64dec.decodeBuffer(req.getSha1());
			byte[] sha256 = b64dec.decodeBuffer(req.getSha256());
			byte[] signature = b64dec.decodeBuffer(req.getSignature());
			Date dtSign = javax.xml.bind.DatatypeConverter.parseDateTime(
					req.getTime()).getTime();
			boolean verifyCRL = "true".equals(req.getCrl());

			util.validarECompletarPacoteAssinavel(certificate, sha1, sha256,
					signature, policy, dtSign, resp);

			return Response.status(200).entity(resp).build();
		} catch (Exception e) {
			resp.setError(e.toString());
			return Response.status(500).entity(resp).build();
		}
	}

	@POST
	@Path("/validate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validate(ValidateRequest req) throws Exception {
		ValidateResponse resp = new ValidateResponse();

		try {
			BASE64Decoder b64dec = new BASE64Decoder();
			byte[] sign = b64dec.decodeBuffer(req.getEnvelope());
			byte[] sha1 = b64dec.decodeBuffer(req.getSha1());
			byte[] sha256 = b64dec.decodeBuffer(req.getSha256());
			Date dtSign = javax.xml.bind.DatatypeConverter.parseDateTime(
					req.getTime()).getTime();
			boolean verifyCRL = "true".equals(req.getCrl());

			int errorCode = util.validateSign(sign, sha1, sha256, dtSign, verifyCRL, resp);
			resp.setErrorCode(errorCode);

			return Response.status(200).entity(resp).build();
		} catch (Exception e) {
			resp.setError(e.toString());
			return Response.status(500).entity(resp).build();
		}
	}

	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public Response test(InputStream incomingData) {
		String result = "Blue Crystal REST OK!";

		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}

	private void LogDebug(String str) {
		// LOG.debug(str);
		System.out.println(new Date() + " - " + str);
	}

}