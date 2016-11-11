package bluecrystal.service.loader;

import java.security.cert.X509CRL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MapCacheManager implements CacheManager {
	public Map<String, X509CRL> cache = new HashMap<String, X509CRL>();

	public MapCacheManager() {
//		this.cache = cache;
	}
	/* (non-Javadoc)
	 * @see bluecrystal.service.loader.CacheManager#getInCache(java.lang.String)
	 */
	@Override
	public X509CRL getInCache(String url, Date date) {
		X509CRL crl = cache.get(url);
		if(crl != null && crl.getNextUpdate().after(date)){
			return crl;
		}
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

}