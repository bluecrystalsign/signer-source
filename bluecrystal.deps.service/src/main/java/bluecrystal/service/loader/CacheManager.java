package bluecrystal.service.loader;

import java.security.cert.X509CRL;
import java.util.Date;

public interface CacheManager {

	X509CRL getInCache(String url, Date date);

//	boolean checkInCache(String url, Date date);

	void addToCache(String url, X509CRL crl);

}