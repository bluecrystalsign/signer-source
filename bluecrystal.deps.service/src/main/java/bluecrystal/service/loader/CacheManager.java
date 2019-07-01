package bluecrystal.service.loader;

import java.security.cert.X509CRL;
import java.util.Date;

import org.bouncycastle.cert.ocsp.OCSPResp;

public interface CacheManager {

	Object getInCache(String url, Date date);

//	boolean checkInCache(String url, Date date);

	void addToCache(String url, X509CRL crl);

	void addToCache(String url, OCSPResp ocspResp);

}