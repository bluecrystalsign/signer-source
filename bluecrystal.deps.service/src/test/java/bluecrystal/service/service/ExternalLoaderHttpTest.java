package bluecrystal.service.service;

import static org.junit.Assert.fail;

import org.junit.Test;

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
//			System.out.println("resultado: "+ Base64.getEncoder().encodeToString(ret));
			
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

}
