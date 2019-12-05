package bluecrystal.service.loader;

import java.security.cert.X509CRL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.cert.ocsp.OCSPResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bluecrystal.service.exception.OCSPQueryException;
import bluecrystal.service.validator.OcspValidatorImpl;

public class MapCacheManager implements CacheManager {
	static final Logger LOG = LoggerFactory.getLogger(MapCacheManager.class);
	
	public Map<String, Object> cache = new HashMap<>();

	public MapCacheManager() {
//		this.cache = cache;
	}
	/* (non-Javadoc)
	 * @see bluecrystal.service.loader.CacheManager#getInCache(java.lang.String)
	 */
	@Override
	public Object getInCache(String url, Date date) {
		Object hit = cache.get(url);
		if (hit == null)
			return null;
		Date nextUpdate = null;
		if (hit instanceof X509CRL) {
			X509CRL crl = (X509CRL)hit;
			nextUpdate = crl.getNextUpdate(); 
		} else if (hit instanceof OCSPResp) {
			try {
				OCSPResp ocspResp = (OCSPResp)hit;
				nextUpdate = OcspValidatorImpl.xtractNextUpdate(ocspResp);
			} catch (OCSPQueryException e) {
				LOG.error("can't get OCSP next update date", e);
			}
		}
		LOG.info("map-cache: " + url + ", hit=" + (hit != null) + ", next-update=" + nextUpdate + ", date="
				+ date + ", valid=" + nextUpdate.after(date));
		if (nextUpdate != null && nextUpdate.after(date))
			return hit;
		return null;
	}
	/* (non-Javadoc)
	 * @see bluecrystal.service.loader.CacheManager#checkInCache(java.lang.String, java.util.Date)
	 */
//	@Override
//	public boolean checkInCache(String url, Date date) {
//		if( cache.containsKey(url)){
//			X509CRL cachedCRL = cache.get(url);
//			if(cachedCRL.getNextUpdate().after(date)){
//				return true;
//			}
//		}
//		return false;
//	}
	/* (non-Javadoc)
	 * @see bluecrystal.service.loader.CacheManager#addToCache(java.lang.String, java.security.cert.X509CRL)
	 */
	@Override
	public void addToCache(String key, X509CRL crl) {
		cache.put(key, crl);
	}
	@Override
	public void addToCache(String key, OCSPResp ocspResp) {
		cache.put(key, ocspResp);
	}

}