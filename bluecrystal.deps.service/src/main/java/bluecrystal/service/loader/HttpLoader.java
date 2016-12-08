package bluecrystal.service.loader;

import java.io.IOException;
import java.net.MalformedURLException;

public interface HttpLoader {
	public byte[] getfromUrl(String urlName) throws MalformedURLException,
	IOException;
}