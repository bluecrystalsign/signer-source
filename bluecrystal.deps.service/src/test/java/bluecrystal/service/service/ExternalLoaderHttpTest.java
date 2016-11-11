package bluecrystal.service.service;

import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Base64;

import org.junit.Test;

import bluecrystal.service.loader.ExternalLoaderHttp;
import bluecrystal.service.loader.ExternalLoaderHttpNio;

public class ExternalLoaderHttpTest {

//	@Test
//	public void testGetfromUrl() {
//		try {
//			byte[] ret = ExternalLoaderHttp.getfromUrl("https://p5.icpedu.rnp.br/crl");
//			System.out.println("baixado: "+ret.length+" bytes");
//			
//			
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//	}
	

	@Test
	public void testGetfromUrlNio3() {
		try {
			
			
			byte[] ret = ExternalLoaderHttpNio.getfromUrl("https://p5.icpedu.rnp.br/crl");
			System.out.println("resultado: "+ Base64.getEncoder().encodeToString(ret));
			
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

}
