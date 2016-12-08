package bluecrystal.service.service;

import static org.junit.Assert.fail;

import java.util.Base64;

import org.junit.Test;

import bluecrystal.service.loader.HttpLoader;
import bluecrystal.service.util.PrefsFactory;

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
	public void testGetfromUrlNio() {
		try {
			
			
			HttpLoader httpLoader = PrefsFactory.getHttpLoader();
			byte[] ret = httpLoader.getfromUrl("https://p5.icpedu.rnp.br/crl");
			System.out.println("resultado: "+ Base64.getEncoder().encodeToString(ret));
			
		} catch (Throwable e) {
			fail(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testGetfromUrlNio2() {
		try {
			
			
			byte[] ret = PrefsFactory.getHttpLoader().getfromUrl("http://lcr.caixa.gov.br/accaixajusv2.crl");
			System.out.println("resultado: ("+ret.length+")"+ Base64.getEncoder().encodeToString(ret));
			
		} catch (Throwable e) {
			fail(e.getLocalizedMessage());
		}
	}

}
