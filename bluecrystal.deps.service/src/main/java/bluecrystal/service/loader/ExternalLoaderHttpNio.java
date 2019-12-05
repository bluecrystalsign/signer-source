/*
    Blue Crystal: Document Digital Signature Tool
    Copyright (C) 2007-2015  Sergio Leal

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package bluecrystal.service.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalLoaderHttpNio implements HttpLoader {
	static final Logger LOG = LoggerFactory.getLogger(ExternalLoaderHttpNio.class);
	private static final int BUFFER_SIZE = 4 * 1024 * 1024;

	public ExternalLoaderHttpNio() {
		super();
	}

	public byte[] get(String urlName) throws MalformedURLException,
	IOException  {
		int totalRead = 0;
		URL url = new URL(urlName);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
	
	    while (rbc.read(buf) != -1) {
	    }
	    buf.flip();
	    totalRead = buf.limit();
		LOG.debug("baixado: " + totalRead + " bytes de [" + urlName + "]");
		byte[] b = new byte[totalRead];
		buf.get(b, 0, totalRead);
		rbc.close();
		return b;
	}
	

	@Override
	public byte[] post(String url, String contentType, byte[] body) throws MalformedURLException, IOException {
		URL u = new URL(url);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();

		con.setAllowUserInteraction(false);
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setInstanceFollowRedirects(false);
		con.setRequestMethod("POST");

		con.setRequestProperty("Content-Length", Integer.toString(body.length));
		con.setRequestProperty("Content-Type", contentType);

		con.connect();
		OutputStream os = con.getOutputStream();
		os.write(body);
		os.close();

		if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new IOException("Server did not respond with HTTP_OK(200) but with " + con.getResponseCode());
		}

		if ((con.getContentType() == null) || !con.getContentType().equals("application/ocsp-response")) {
			throw new IOException("Response MIME type is not application/ocsp-response");
		}

		// Read response
		InputStream reader = con.getInputStream();

		int resplen = con.getContentLength();
		byte[] ocspResponseEncoded = new byte[resplen];

		int offset = 0;
		int bread;
		while ((resplen > 0) && (bread = reader.read(ocspResponseEncoded, offset, resplen)) != -1) {
			offset += bread;
			resplen -= bread;
		}

		reader.close();
		con.disconnect();
		return ocspResponseEncoded;
	}


}
