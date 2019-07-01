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
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalLoaderHttp implements HttpLoader  {
	static final Logger LOG = LoggerFactory.getLogger(ExternalLoaderHttp.class);
	private static final int BUFFER_SIZE = 64 * 1024;

	public ExternalLoaderHttp() {
		super();
	}

	/* (non-Javadoc)
	 * @see bluecrystal.service.loader.ExternalLoaderHttp#getfromUrl(java.lang.String)
	 */
	public byte[] get(String url) throws MalformedURLException,
			IOException {
		URLConnection conn = createConn(url);
		byte[] ret = null;
		try {
			ret = getBinResponse(conn);
		} catch (Throwable e) {
			LOG.error("Could not load through HTTP(S) "+ url, e);
			throw new RuntimeException(e);
		}

		return ret;
	}

	private static byte[] getBinResponse(URLConnection conn) throws Exception {
		List buffer = new ArrayList();
		byte[] ret = null;
		InputStream is = conn.getInputStream();
		int size = 0, readLen = 0;
		readLen = is.available() < BUFFER_SIZE ? is.available() : BUFFER_SIZE;
		ret = new byte[readLen];
		int gotFromIs = is.read(ret);
		int cnt = 0;

		while (gotFromIs != -1) {
			if (gotFromIs > 0) {
				buffer.add(ret);
				if (readLen > 0 || gotFromIs > 0 ) {
					LOG.debug("# [" + cnt +"]" + size + " => " + readLen + " : "+gotFromIs);
				} else {
					LOG.debug("*");
				}
					
				size += ret.length;
			}
			readLen = is.available() < BUFFER_SIZE ? is.available()
					: BUFFER_SIZE;
			for (int i = 0; i < 5; i++) {
				if (readLen != 0) {
					ret = new byte[readLen];
					break;
				} 
				Thread.sleep(500);
				readLen = is.available() < BUFFER_SIZE ? is.available()
						: BUFFER_SIZE;
			}

			gotFromIs = is.read(ret);
			cnt += 1;
	}
		
		
		
		if (is.read() != -1) {
		}

		int index = 0;
		Iterator it = buffer.iterator();
		ret = new byte[size];
		while (it.hasNext()) {
			byte[] nextBuff = (byte[]) it.next();
			LOG.debug("cp (" + ret.length + ") : " + index + " => "
					+ nextBuff.length);
			System.arraycopy(nextBuff, 0, ret, index, nextBuff.length);
			index += nextBuff.length;
		}

		return ret;
	}

	private static URLConnection createConn(String serverUrl)
			throws MalformedURLException, IOException {
		URL url = new URL(serverUrl);
		URLConnection conn = url.openConnection();
		return conn;
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
