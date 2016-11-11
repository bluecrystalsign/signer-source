/*
    Blue Crystal: Document Digital Signature Tool
    Copyright (C) 2007-2015  Sergio Leal

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package bluecrystal.example.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import bluecrystal.domain.StatusConst;
import bluecrystal.example.web.domain.SignedEnvelope;
import bluecrystal.example.web.util.Convert;
import bluecrystal.service.v1.icpbr.Exception_Exception;
import bluecrystal.service.v1.icpbr.IcpbrService;
import bluecrystal.service.v1.icpbr.IcpbrService_Service;

/**
 * Servlet implementation class CreateEnvelope
 */
@WebServlet("/CreateEnvelope")
public class CreateEnvelope extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IcpbrService serv;
	final static Logger logger = LoggerFactory.getLogger(CreateEnvelope.class);

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateEnvelope() {
        super();
        serv = (new IcpbrService_Service()).getIcpbrPort();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			handleRequest(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			handleRequest(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String hash_valueb64 = (String) request.getParameter("hash_value");
		String timeValue = (String) request.getParameter("time_value");
		String saValueb64 = (String) request.getParameter("sa_value");
		String signedValueb64 = (String) request.getParameter("signed_value");
		String certb64 = (String) request.getParameter("cert");
		String alg = (String) request.getParameter("alg");
		
		boolean isError = false;
		if(hash_valueb64 == null){
			logger.error("hash_valueb64 é nulo!");
			isError = true;
		}
		if(timeValue == null){
			logger.error("timeValue é nulo!");
			isError = true;
		}
		
		if(saValueb64 == null){
			logger.error("saValueb64 é nulo!");
			isError = true;
		}
		
		if(signedValueb64 == null){
			logger.error("signedValueb64 é nulo!");
			isError = true;
		}
		
		if(certb64 == null){
			logger.error("certb64 é nulo!");
			isError = true;
		}
		
		if(alg == null){
			logger.error("alg é nulo!");
			isError = true;
		}
		
		if(isError){
			return;
		}

		
		
		logger.debug("CreateEnvelope *****");
		logger.debug("hash_valueb64 :"+hash_valueb64);
		logger.debug("timeValue :"+timeValue);
		logger.debug("saValueb64 :"+saValueb64);
		logger.debug("signedValueb64 :"+signedValueb64);
		logger.debug("certb64 :"+certb64);
		logger.debug("alg :"+alg);
		
		String ret = "";
		
		Boolean algSha256 = false;
		if(alg == null || alg.compareToIgnoreCase("sha256")==0){
			algSha256 = true;

//			List<Signature> signatute = new ArrayList<Signature>();
//			Signature sign = new Signature();
//			sign.setOrigHashB64(hash_valueb64);
//			sign.setSignB64(signedValueb64);
//			sign.setSigningTime(parseDate(timeValue));
//			ret = serv.composeCoSignEnvelopeADRB21(signatute );

			ret = serv.composeEnvelopeADRB21(signedValueb64, certb64, hash_valueb64, parseDate(timeValue));
		} else {
			ret = serv.composeEnvelopeADRB10(signedValueb64, certb64, hash_valueb64, parseDate(timeValue));
			
		}

		
		
//		boolean isOk = verifySignature(algSha256, ret, (String)request.getSession().getAttribute("destPathname"));
		int signStatus = validateSignWithStatus(algSha256, ret, (String)request.getSession().getAttribute("destPathname"));
		
		
		String certB64 = parseCertFromSignature(ret);
		String certSubject = getCertSubject(certb64);
		Gson gson = new Gson();
		String VerifiedSignJson = gson.toJson(new SignedEnvelope(ret, signStatus,StatusConst.getMessageByStatus(signStatus), certB64, certSubject));

		isError = false;
		if(ret == null){
			logger.error("ret é nulo!");
		}

		if(certB64 == null){
			logger.error("certB64 é nulo!");
		}
		if(certSubject == null){
			logger.error("certSubject é nulo!");
		}
		
		if(signStatus != StatusConst.GOOD || signStatus != StatusConst.UNKNOWN){
			logger.warn("Assinatura  NÂO é valida!");
			logger.warn("status ="+signStatus);
			logger.warn("descr = "+ StatusConst.getMessageByStatus(signStatus));
			logger.warn("certB64 = "+certB64);
			logger.warn("certSubject = "+certSubject);
			logger.warn("ret = "+ret);
		}
		
		logger.debug("retorno: "+ VerifiedSignJson);
		
		PrintWriter out = response.getWriter();
		out.print(VerifiedSignJson);
		out.flush();
	}

	private String parseCertFromSignature(String ret) throws Exception_Exception  {
		
		return serv.extractSignerCert(ret);
	}

	private String getCertSubject(String certb64) throws Exception_Exception {
		return serv.getCertSubject(certb64);
	}

	private boolean verifySignature(Boolean algSha256, String ret, String filename) throws Exception {
		
		MessageDigest hashSum = null;
		if(algSha256){
			hashSum = MessageDigest.getInstance("SHA-256");
			logger.debug("Validar assinatura SHA-256");

		} else {
			hashSum = MessageDigest.getInstance("SHA-1");
			logger.debug("Validar assinatura SHA-1");
		}
		hashSum.update(Convert.readFile(filename));
		byte[] digestResult = hashSum.digest();
		
//		Base64.Encoder encoder = Base64.getEncoder(); 
		String digestB64 = new String(Base64.encode(digestResult));
		return serv.validateSign(ret, digestB64, Convert.asXMLGregorianCalendar(new Date()), false);
	}

	private int validateSignWithStatus(Boolean algSha256, String ret, String filename) throws Exception {
		
		MessageDigest hashSum = null;
		if(algSha256){
			hashSum = MessageDigest.getInstance("SHA-256");
			logger.debug("Validar assinatura SHA-256");

		} else {
			hashSum = MessageDigest.getInstance("SHA-1");
			logger.debug("Validar assinatura SHA-1");
		}
		hashSum.update(Convert.readFile(filename));
		byte[] digestResult = hashSum.digest();
		
//		Base64.Encoder encoder = Base64.getEncoder(); 
		String digestB64 = new String(Base64.encode(digestResult));
		return serv.validateSignWithStatus(ret, digestB64, Convert.asXMLGregorianCalendar(new Date()), false);
	}

	
	private XMLGregorianCalendar parseDate(String timeValue) throws DatatypeConfigurationException {
		Date signDate = new Date();
		signDate.setTime(Long.parseLong(timeValue));
		
		
		return Convert.asXMLGregorianCalendar(signDate);
	}
	
	


}
