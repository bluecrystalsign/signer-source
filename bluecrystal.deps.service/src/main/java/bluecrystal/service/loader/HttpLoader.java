package bluecrystal.service.loader;

import java.io.IOException;
import java.net.MalformedURLException;

public interface HttpLoader {
	public byte[] get(String url) throws MalformedURLException, IOException;

	public byte[] post(String url, String contentType, byte[] body) throws MalformedURLException, IOException;
}