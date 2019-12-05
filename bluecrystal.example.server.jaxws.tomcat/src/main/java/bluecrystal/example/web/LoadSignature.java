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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;
import bluecrystal.example.web.domain.SignRef;
import bluecrystal.example.web.util.Convert;
import bluecrystal.service.v1.icpbr.IcpbrService;
import bluecrystal.service.v1.icpbr.IcpbrService_Service;

import com.google.gson.Gson;

/**
 * Servlet implementation class LoadSignature
 */

// "GET /example/LoadSignature?
// cert=MIIFQjCCBCqgAwIBAgIRAM86T9MP6UgerviYQqE96CswDQYJKoZIhvcNAQELBQAwgZsxCzAJBgNVBAYTAkdCMRswGQYDVQQIExJHcmVhdGVyIE1hbmNoZXN0ZXIxEDAOBgNVBAcTB1NhbGZvcmQxGjAYBgNVBAoTEUNPTU9ETyBDQSBMaW1pdGVkMUEwPwYDVQQDEzhDT01PRE8gU0hBLTI1NiBDbGllbnQgQXV0aGVudGljYXRpb24gYW5kIFNlY3VyZSBFbWFpbCBDQTAeFw0xNjA0MjUwMDAwMDBaFw0xNzA0MjUyMzU5NTlaMCYxJDAiBgkqhkiG9w0BCQEWFXNlcmdpby5sZWFsQGdtYWlsLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAOPeujEjVvpLqyactBMhXeeWXSPtANZ9rRxNDB77jhrFwrrEl7UVWl5YiIuUpH1%2FhYnCjvxsvyxYFeNeYq8lFTk0hbsyrOq7Y19eWjqVlmvMDLUOLJPOYP0KYwtiJBvYAuZRbj04zPxL3QOGKmVBoHpEU4ZoFeibNYk0h7Y8S2%2BnTBmFUflHAB3VGDZ9FxYrIhDUzr%2BB56MbVXjYVpRZkHEz2ehgyExZpdoLjqYoeeTQoX99P5AQ7IsQ8dn4C5IlpxSH5b4w6w%2BPqFb0j6rsO1siESHXkCtzHoQPhtLWRrYZApeE4FELaTIdw2VvCRyGTeGcyCph6eKASgrIgY5YSBMCAwEAAaOCAfMwggHvMB8GA1UdIwQYMBaAFJJha4LhoqCqT%2Bxn8cKj97SAAMHsMB0GA1UdDgQWBBRugGUmugGwJg8NNlUB8dBerdimozAOBgNVHQ8BAf8EBAMCBaAwDAYDVR0TAQH%2FBAIwADAgBgNVHSUEGTAXBggrBgEFBQcDBAYLKwYBBAGyMQEDBQIwEQYJYIZIAYb4QgEBBAQDAgUgMEYGA1UdIAQ%2FMD0wOwYMKwYBBAGyMQECAQEBMCswKQYIKwYBBQUHAgEWHWh0dHBzOi8vc2VjdXJlLmNvbW9kby5uZXQvQ1BTMF0GA1UdHwRWMFQwUqBQoE6GTGh0dHA6Ly9jcmwuY29tb2RvY2EuY29tL0NPTU9ET1NIQTI1NkNsaWVudEF1dGhlbnRpY2F0aW9uYW5kU2VjdXJlRW1haWxDQS5jcmwwgZAGCCsGAQUFBwEBBIGDMIGAMFgGCCsGAQUFBzAChkxodHRwOi8vY3J0LmNvbW9kb2NhLmNvbS9DT01PRE9TSEEyNTZDbGllbnRBdXRoZW50aWNhdGlvbmFuZFNlY3VyZUVtYWlsQ0EuY3J0MCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5jb21vZG9jYS5jb20wIAYDVR0RBBkwF4EVc2VyZ2lvLmxlYWxAZ21haWwuY29tMA0GCSqGSIb3DQEBCwUAA4IBAQAMMA7KBsy%2FWFM6uY2C1B8970HskwDl%2F2Zs3HUM7PvWzSaA9WgWk3cRLjZF4ggxZ8ylXEQmtEP01tvKhhIt%2FKtNyj6tg7R9G%2B10mf8TPBX3ELIWFafiLWdOxyLXBnebkUNUJGZ0POyldUMQXrJHT719uNQbZ%2FovJEPe6FDO2YcjbXlC3MNsY8YmHt9QJy0ATwLPH%2BemWeGoXqM%2F1AKrSnuatkp03hHSr4oou23NGpgPG2pdhwTZKAqXpzMxj3%2BHA02uVB62FN1GaIHsqZp6qQRsbhAtC2CfT1S2yN8voROs3DX8%2BMgkpf7mXXBQeQ2GP5%2Fi6sbU4%2FFFPrXHZjIa3FhV
// alg=sha256 HTTP/1.1" 200 809



@WebServlet("/LoadSignature")
public class LoadSignature extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IcpbrService serv;
	final static Logger logger = LoggerFactory.getLogger(LoadSignature.class);

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadSignature() {
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
		String certb64 = (String) request.getParameter("cert");
		String alg = (String) request.getParameter("alg");
		
		logger.info("LoadSignature *****");
		logger.info("certb64 :"+certb64);
		logger.info("alg :"+alg);


		boolean isError = false;
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
		
		MessageDigest hashSum = null;
		if(alg == null || alg.compareToIgnoreCase("sha256")==0){
			hashSum = MessageDigest.getInstance("SHA-256");
		} else {
			hashSum = MessageDigest.getInstance("SHA-1");
		}
		String destPathname = (String)request.getSession().getAttribute("destPathname");
		hashSum.update(Convert.readFile(destPathname));
		byte[] digestResult = hashSum.digest();
		Date now = new Date();
		
//		String origHashB64, Date signingTime,
//		String x509B64
		BASE64Encoder b64enc = new BASE64Encoder();
		String hashSa = null;
		if(alg == null || alg.compareToIgnoreCase("sha256")==0){
			hashSa = serv.hashSignedAttribADRB21(b64enc.encode(digestResult), 
					Convert.asXMLGregorianCalendar(now), certb64);
		} else {
			hashSa = serv.hashSignedAttribADRB10(b64enc.encode(digestResult), 
					Convert.asXMLGregorianCalendar(now), certb64);
		}
		SignRef signRef = new SignRef(b64enc.encode(digestResult), now.getTime(), hashSa);
		
		Gson gson = new Gson();
		String signRefJson = gson.toJson(signRef);
		System.out.println("retorno: "+ signRefJson);
		
		response.setContentType("application/json");
		// Get the printwriter object from response to write the required json object to the output stream      
		PrintWriter out = response.getWriter();
		// Assuming your json object is **jsonObject**, perform the following, it will return your json object  
		out.print(signRefJson);
		out.flush();
		
	}

	

}
