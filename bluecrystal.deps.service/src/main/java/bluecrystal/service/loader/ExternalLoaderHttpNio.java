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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalLoaderHttpNio {
	static final Logger LOG = LoggerFactory.getLogger(ExternalLoaderHttpNio.class);
	private static final int BUFFER_SIZE = 4 * 1024 * 1024;

	public ExternalLoaderHttpNio() {
		super();
	}

	public static  byte[] getfromUrl(String urlName) throws MalformedURLException,
	IOException  {
		URL url = new URL(urlName);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		int bytesRead = rbc.read(buf);
		LOG.debug("baixado: " + bytesRead + " bytes de [" + urlName + "]");
		buf.rewind();
		byte[] b = new byte[bytesRead];
		buf.get(b, 0, bytesRead);

		rbc.close();
		return b;
	}

}
