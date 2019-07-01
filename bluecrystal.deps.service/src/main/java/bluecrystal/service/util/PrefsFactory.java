package bluecrystal.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bluecrystal.service.helper.UtilsRepo;
import bluecrystal.service.interfaces.RepoLoader;
import bluecrystal.service.loader.CacheManager;
import bluecrystal.service.loader.FSRepoLoader;
import bluecrystal.service.loader.HttpLoader;
import bluecrystal.service.loader.LCRLoader;
import bluecrystal.service.loader.Messages;

public class PrefsFactory {
	static final Logger LOG = LoggerFactory.getLogger(UtilsRepo.class);

	static CacheManager localCache = null;
	static HttpLoader httpLoader = null;
	static RepoLoader repoLoader = null;
	static LCRLoader lcrLoader = null;

	public static boolean getUseOCSP() {
		return !"false".equals(Messages.getString("Validator.useOCSP"));
	}

	public static String getCertFolder() {
		return Messages.getString("FSRepoLoader.certFolder");
	}

	public static RepoLoader getRepoLoader() {
		if (repoLoader != null)
			return repoLoader;
		synchronized (PrefsFactory.class) {
			String loaderType = Messages.getString("RepoLoader.loaderType");
			try {
				repoLoader = (RepoLoader) Class.forName(loaderType).newInstance();
				if (repoLoader == null) {
					LOG.error("Could not load Repoloader ");
					repoLoader = loadDefaultFSRepoLoader();
				}
			} catch (Exception e) {
				LOG.error("Could not load Repoloader ", e);
				repoLoader = loadDefaultFSRepoLoader();
			}
			return repoLoader;
		}
	}

	public static CacheManager getCacheManager() {
		if (localCache != null)
			return localCache;
		synchronized (PrefsFactory.class) {
			String cacheType = Messages.getString("LCRLoader.cacheType");
			try {
				localCache = (CacheManager) Class.forName(cacheType).newInstance();
				if (localCache == null) {
					localCache = loadDefaultCacheManager();
				}
			} catch (Exception e) {
				localCache = loadDefaultCacheManager();
			}
			return localCache;
		}
	}

	public static HttpLoader getHttpLoader() {
		if (httpLoader != null)
			return httpLoader;
		synchronized (PrefsFactory.class) {

			String cacheType = Messages.getString("httpLoader");
			try {
				httpLoader = (HttpLoader) Class.forName(cacheType).newInstance();
				if (httpLoader == null) {
					httpLoader = loadDefaultHttpLoader();
				}
			} catch (Exception e) {
				httpLoader = loadDefaultHttpLoader();
			}
			return httpLoader;
		}
	}

	public static LCRLoader getLCRLoader() {
		if (lcrLoader != null)
			return lcrLoader;
		synchronized (PrefsFactory.class) {

			String loaderType = Messages.getString("LCRLoader.loaderType");
			try {
				lcrLoader = (LCRLoader) Class.forName(loaderType).newInstance();
				if (lcrLoader == null) {
					lcrLoader = loadDefaultLCRLoader();
				}
			} catch (Exception e) {
				lcrLoader = loadDefaultLCRLoader();
			}
			return lcrLoader;
		}
	}

	private static FSRepoLoader loadDefaultFSRepoLoader() {
		return new bluecrystal.service.loader.FSRepoLoader();
	}

	private static CacheManager loadDefaultCacheManager() {
		return new bluecrystal.service.loader.MapCacheManager();
	}

	private static HttpLoader loadDefaultHttpLoader() {
		return new bluecrystal.service.loader.ExternalLoaderHttpNio();
	}

	private static LCRLoader loadDefaultLCRLoader() {
		return new bluecrystal.service.loader.LCRLoaderImpl();
	}
}
