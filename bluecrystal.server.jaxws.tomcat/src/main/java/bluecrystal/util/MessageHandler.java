package bluecrystal.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler implements SOAPHandler<SOAPMessageContext> {
	final static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {

		Boolean isRequest = (Boolean) context
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (!isRequest) {

			try {
				SOAPMessage soapMsg = context.getMessage();

				// tracking
				//soapMsg.writeTo(System.out);
				logger.debug(soapMsg.toString());
				

//			} catch (SOAPException e) {
//				logger.error(exceptionToString(e));
//				System.err.println(e);
			} catch (Throwable e) {
//				System.err.println(e);
				logger.error(exceptionToString(e));

			}

		}

		// continue other handler chain
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

//	private void generateSOAPErrMessage(SOAPMessage msg, String reason) {
//		try {
//			SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
//			SOAPFault soapFault = soapBody.addFault();
//			soapFault.setFaultString(reason);
//			throw new SOAPFaultException(soapFault);
//		} catch (SOAPException e) {
//		}
//	}
	
	private String exceptionToString(Throwable e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString(); // stack trace as a string
		
	}
}
