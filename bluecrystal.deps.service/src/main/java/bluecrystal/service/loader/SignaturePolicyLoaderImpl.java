package bluecrystal.service.loader;

import java.util.HashMap;
import java.util.Map;

public class SignaturePolicyLoaderImpl implements SignaturePolicyLoader {

	Map<String, byte[]> policies;
	public SignaturePolicyLoaderImpl() {
		super();
		policies = new HashMap<String, byte[]>();
	}

	
	@Override
	public byte[] loadFromUrl(String url) throws Exception {
		byte[] sp = null;
		if(policies.containsKey(url)){
			return policies.get(url);
		} else {
			sp = ExternalLoaderHttpNio.getfromUrl(url);
			policies.put(url, sp);
		}
		return sp;
		
	}

}
