package bluecrystal.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bluecrystal.service.helper.UtilsRepo;
import bluecrystal.service.interfaces.RepoLoader;
import bluecrystal.service.loader.CacheManager;
import bluecrystal.service.loader.FSRepoLoader;
import bluecrystal.service.loader.HttpLoader;
import bluecrystal.service.loader.Messages;

public class PrefsFactory {
	static final Logger LOG = LoggerFactory.getLogger(UtilsRepo.class);

	public static String getCertFolder() {
		return Messages.getString("FSRepoLoader.certFolder");
	}
	
	public static RepoLoader getRepoLoader() {
		RepoLoader repoLoader;
		String loaderType = Messages.getString("RepoLoader.loaderType");
		try {
			repoLoader = (RepoLoader) Class
			        .forName(loaderType)
			        .newInstance();
			if(repoLoader==null){
				LOG.error("Could not load Repoloader ");
				repoLoader = loadDefaultFSRepoLoader();
			}
		} catch (Exception e) {
			LOG.error("Could not load Repoloader ", e);
			repoLoader = loadDefaultFSRepoLoader();
		}
		return repoLoader;
	}
	
	public static CacheManager getCacheManager(){
		CacheManager localCache = null;
		String cacheType = Messages.getString("LCRLoader.cacheType");
		try {
			localCache = (CacheManager) Class
			        .forName(cacheType)
			        .newInstance();
			if(localCache == null){
				localCache = loadDefaultCacheManager();
			}
		} catch (Exception e) {
			localCache = loadDefaultCacheManager();
		}
		return localCache;
	}

	public static HttpLoader getHttpLoader(){
		HttpLoader httpLoader = null;
		String cacheType = Messages.getString("httpLoader");
		try {
			httpLoader = (HttpLoader) Class
			        .forName(cacheType)
			        .newInstance();
			if(httpLoader == null){
				httpLoader = loadDefaultHttpLoader();
			}
		} catch (Exception e) {
			httpLoader = loadDefaultHttpLoader();
		}
		return httpLoader;
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
}
