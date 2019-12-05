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

package bluecrystal.service.validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.cert.ocsp.RevokedStatus;
import org.bouncycastle.cert.ocsp.SingleResp;
import org.bouncycastle.operator.OperatorCreationException;
//import org.bouncycastle.ocsp.BasicOCSPResp;
//import org.bouncycastle.ocsp.OCSPException;
//import org.bouncycastle.ocsp.OCSPReq;
//import org.bouncycastle.ocsp.OCSPResp;
//import org.bouncycastle.cert.ocsp.OCSPRespStatus;
//import org.bouncycastle.ocsp.RevokedStatus;
//import org.bouncycastle.ocsp.SingleResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bluecrystal.bcdeps.helper.DerEncoder;
//import bluecrystal.bcdeps.helper.DerEncoder;
import bluecrystal.domain.OperationStatus;
import bluecrystal.domain.StatusConst;
import bluecrystal.service.exception.OCSPQueryException;
import bluecrystal.service.exception.RevokedException;
//import bluecrystal.service.exception.OCSPQueryException;
//import bluecrystal.service.exception.RevokedException;
//import bluecrystal.service.exception.UndefStateException;
import bluecrystal.service.exception.UndefStateException;
import bluecrystal.service.loader.CacheManager;
import bluecrystal.service.util.PrefsFactory;

public class OcspValidatorImpl implements OcspValidator {
	static final Logger LOG = LoggerFactory.getLogger(OcspValidatorImpl.class);

	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	static final long MIN_VALID=60*ONE_MINUTE_IN_MILLIS;//millisecs
	
	
	public OcspValidatorImpl() {
		super();
	}	
	
	public OperationStatus verifyOCSP(X509Certificate nextCert,
			X509Certificate nextIssuer, Date date) throws IOException,
			CertificateException, CRLException, UndefStateException,
			RevokedException, OperatorCreationException {
		try {
			OCSPReq req = GenOcspReq(nextCert, nextIssuer);

			List<String> OCSPUrls = extractOCSPUrl(nextCert);

			OCSPResp ocspResponse = null;
			for (String ocspUrl : OCSPUrls) {
				try {
					String urlToCache = ocspUrl + "?reqHash=" + nextCert.hashCode();
					CacheManager cache = PrefsFactory.getCacheManager();
					ocspResponse = (OCSPResp) cache.getInCache(urlToCache, date);
					if (ocspResponse == null) {
						ocspResponse = xchangeOcsp(ocspUrl, req);
						cache.addToCache(urlToCache, ocspResponse);
					}
					break;
				} catch (Exception e) {
					LOG.error("Error exchanging OCSP",e);
				}
			}
			if (ocspResponse != null) {
				Date valid = xtractNextUpdate(ocspResponse);
				if (valid != null) {
					return new OperationStatus(StatusConst.GOOD, valid);
				} else {
					Date goodUntil = new Date();
					goodUntil = new Date(goodUntil.getTime() + MIN_VALID);
					return new OperationStatus(StatusConst.GOOD, goodUntil);
				}
			}

		} catch (OCSPException e) {
			LOG.error("Error executing OCSP Operation",e);
		} catch (OCSPQueryException e) {
			LOG.error("Error executing OCSP Operation",e);
		}
		return new OperationStatus(StatusConst.UNKNOWN, null);
	}

	public static Date xtractNextUpdate(OCSPResp ocspResponse) throws OCSPQueryException {
		int status = ocspResponse.getStatus();
		switch (status) {
//		case OCSPRespStatus.SUCCESSFUL:
//			break;
//		case OCSPResp.INTERNAL_ERROR:
//		case OCSPRespStatus.MALFORMED_REQUEST:
//		case OCSPRespStatus.SIGREQUIRED:
//		case OCSPRespStatus.TRY_LATER:
//		case OCSPRespStatus.UNAUTHORIZED:

		case OCSPResp.SUCCESSFUL:
			break;
		case OCSPResp.INTERNAL_ERROR:
		case OCSPResp.MALFORMED_REQUEST:
		case OCSPResp.SIG_REQUIRED:
		case OCSPResp.TRY_LATER:
		case OCSPResp.UNAUTHORIZED:

		
			throw new OCSPQueryException(
					"OCSP Error: " //$NON-NLS-1$
					+ Integer.toString(status));
		default:
			throw new OCSPQueryException("***"); //$NON-NLS-1$
		}

		try {
			BasicOCSPResp bresp = (BasicOCSPResp) ocspResponse
					.getResponseObject();

			if (bresp == null) {
				throw new OCSPQueryException("***"); //$NON-NLS-1$
			}
//			X509Certificate[] ocspcerts = bresp.getCerts(Messages
//					.getString("ValidateSignAndCertBase.71")); //$NON-NLS-1$

			// Verify all except trusted anchor
			// for (i = 0; i < ocspcerts.length - 1; i++) {
			// ocspcerts[i].verify(ocspcerts[i + 1].getPublicKey());
			// }
			// if (rootcert != null) {
			// ocspcerts[i].verify(rootcert.getPublicKey());
			// }

			SingleResp[] certstat = bresp.getResponses();
			for (SingleResp singleResp : certstat) {
				// if (singleResp.getCertStatus() == null) {
				// return true;
				// }
				if (singleResp.getCertStatus() instanceof RevokedStatus) {
					throw new RevokedException();
				}
				LOG.debug("this-update=" + singleResp.getThisUpdate().getTime());
				LOG.debug("next-update=" + singleResp.getNextUpdate().getTime());
				return singleResp.getNextUpdate();
			}

		} catch (Exception e) {
			throw new OCSPQueryException(e);
		}

		return null;
	}

	private OCSPResp xchangeOcsp(String ocspUrl, OCSPReq req)
			throws MalformedURLException, IOException, OCSPQueryException {
		byte[] ocspResponseEncoded = PrefsFactory.getHttpLoader().post(ocspUrl, "application/ocsp-request", req.getEncoded());
		return new OCSPResp(ocspResponseEncoded);
	}
	
	
	private OCSPReq GenOcspReq(X509Certificate nextCert,
			X509Certificate nextIssuer) throws OCSPException, CertificateEncodingException, OperatorCreationException, IOException {

	return DerEncoder.GenOcspReq(nextCert, nextIssuer);
	}

	private List<String> extractOCSPUrl(X509Certificate nextCert)
			throws CRLException {
		return DerEncoder.extractOCSPUrl(nextCert);
	}
}
