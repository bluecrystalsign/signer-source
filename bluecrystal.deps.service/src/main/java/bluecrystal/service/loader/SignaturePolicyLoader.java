package bluecrystal.service.loader;

public interface SignaturePolicyLoader {
	
	byte[] loadFromUrl(String url) throws Exception;

}
