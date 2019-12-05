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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import bluecrystal.service.v1.icpbr.IcpbrService;
import bluecrystal.service.v1.icpbr.IcpbrService_Service;
import bluecrystal.service.v1.icpbr.NameValue;

// test data - 
// "GET /example/ParseCert?
// cert=MIIFQjCCBCqgAwIBAgIRAM86T9MP6UgerviYQqE96CswDQYJKoZIhvcNAQELBQAwgZsxCzAJBgNVBAYTAkdCMRswGQYDVQQIExJHcmVhdGVyIE1hbmNoZXN0ZXIxEDAOBgNVBAcTB1NhbGZvcmQxGjAYBgNVBAoTEUNPTU9ETyBDQSBMaW1pdGVkMUEwPwYDVQQDEzhDT01PRE8gU0hBLTI1NiBDbGllbnQgQXV0aGVudGljYXRpb24gYW5kIFNlY3VyZSBFbWFpbCBDQTAeFw0xNjA0MjUwMDAwMDBaFw0xNzA0MjUyMzU5NTlaMCYxJDAiBgkqhkiG9w0BCQEWFXNlcmdpby5sZWFsQGdtYWlsLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAOPeujEjVvpLqyactBMhXeeWXSPtANZ9rRxNDB77jhrFwrrEl7UVWl5YiIuUpH1%2FhYnCjvxsvyxYFeNeYq8lFTk0hbsyrOq7Y19eWjqVlmvMDLUOLJPOYP0KYwtiJBvYAuZRbj04zPxL3QOGKmVBoHpEU4ZoFeibNYk0h7Y8S2%2BnTBmFUflHAB3VGDZ9FxYrIhDUzr%2BB56MbVXjYVpRZkHEz2ehgyExZpdoLjqYoeeTQoX99P5AQ7IsQ8dn4C5IlpxSH5b4w6w%2BPqFb0j6rsO1siESHXkCtzHoQPhtLWRrYZApeE4FELaTIdw2VvCRyGTeGcyCph6eKASgrIgY5YSBMCAwEAAaOCAfMwggHvMB8GA1UdIwQYMBaAFJJha4LhoqCqT%2Bxn8cKj97SAAMHsMB0GA1UdDgQWBBRugGUmugGwJg8NNlUB8dBerdimozAOBgNVHQ8BAf8EBAMCBaAwDAYDVR0TAQH%2FBAIwADAgBgNVHSUEGTAXBggrBgEFBQcDBAYLKwYBBAGyMQEDBQIwEQYJYIZIAYb4QgEBBAQDAgUgMEYGA1UdIAQ%2FMD0wOwYMKwYBBAGyMQECAQEBMCswKQYIKwYBBQUHAgEWHWh0dHBzOi8vc2VjdXJlLmNvbW9kby5uZXQvQ1BTMF0GA1UdHwRWMFQwUqBQoE6GTGh0dHA6Ly9jcmwuY29tb2RvY2EuY29tL0NPTU9ET1NIQTI1NkNsaWVudEF1dGhlbnRpY2F0aW9uYW5kU2VjdXJlRW1haWxDQS5jcmwwgZAGCCsGAQUFBwEBBIGDMIGAMFgGCCsGAQUFBzAChkxodHRwOi8vY3J0LmNvbW9kb2NhLmNvbS9DT01PRE9TSEEyNTZDbGllbnRBdXRoZW50aWNhdGlvbmFuZFNlY3VyZUVtYWlsQ0EuY3J0MCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5jb21vZG9jYS5jb20wIAYDVR0RBBkwF4EVc2VyZ2lvLmxlYWxAZ21haWwuY29tMA0GCSqGSIb3DQEBCwUAA4IBAQAMMA7KBsy%2FWFM6uY2C1B8970HskwDl%2F2Zs3HUM7PvWzSaA9WgWk3cRLjZF4ggxZ8ylXEQmtEP01tvKhhIt%2FKtNyj6tg7R9G%2B10mf8TPBX3ELIWFafiLWdOxyLXBnebkUNUJGZ0POyldUMQXrJHT719uNQbZ%2FovJEPe6FDO2YcjbXlC3MNsY8YmHt9QJy0ATwLPH%2BemWeGoXqM%2F1AKrSnuatkp03hHSr4oou23NGpgPG2pdhwTZKAqXpzMxj3%2BHA02uVB62FN1GaIHsqZp6qQRsbhAtC2CfT1S2yN8voROs3DX8%2BMgkpf7mXXBQeQ2GP5%2Fi6sbU4%2FFFPrXHZjIa3FhV
// HTTP/1.1" 200 1277


/**
 * Servlet implementation class LoadSignature
 */
@WebServlet("/ParseCert")
public class ParseCert extends HttpServlet {
	final static Logger logger = LoggerFactory.getLogger(ParseCert.class);
	private static final long serialVersionUID = 1L;
	private IcpbrService serv;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ParseCert() {
        super();
        serv = (new IcpbrService_Service()).getIcpbrPort();
//        System.out.println("***ParseCert - LoggerContext");
//        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//        // print logback's internal status
//        StatusPrinter.print(lc);
        
        logger.debug("Carregando Servlet ParseCert");
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
		
		logger.debug("Parse Cert *****");
		logger.debug("certb64 :"+certb64);
		
		if(certb64 == null){
			logger.error("certb64 é nulo!");
			return;
			
		}
		
		List<NameValue> parsed = serv.parseCertificate(certb64);
		for(NameValue next : parsed){
			logger.trace(next.getName() + " -> "+ next.getValue());
		}
		Gson gson = new Gson();
		String parsedJson = gson.toJson(parsed);
		
		PrintWriter out = response.getWriter();
		out.print(parsedJson);
		out.flush();

	}

	

}
