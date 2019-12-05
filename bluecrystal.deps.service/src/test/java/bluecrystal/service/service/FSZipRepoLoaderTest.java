package bluecrystal.service.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;

import org.junit.Test;

import bluecrystal.service.loader.FSZipRepoLoader;

public class FSZipRepoLoaderTest {
	String interm = "interm";
	String root = "root";

	@Test
	public void testLoad() {
		try {
			FSZipRepoLoader rl = new FSZipRepoLoader();
			loadByFolder(rl,interm);
			loadByFolder(rl, root);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void loadByFolder(FSZipRepoLoader rl, String src) throws Exception, IOException, CertificateException {
		StringBuffer sb = new StringBuffer();
		InputStream is = rl.load(src);
		
//		int len = is.available();
//		byte[] b = new byte[len];
//		int readLen = is.read(b);
//		String str = new String(b, 0, readLen);
//		sb.append(str);
//		len -= readLen;
//		while (len > 0) {
//			readLen = is.read(b);
//			sb.append(str);
//			len -= readLen;
//		}
//		System.out.println(sb.toString());
		
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		Collection<? extends Certificate> c = cf.generateCertificates(is);
		System.out.println(c.size());
	}

	// @Test
	// public void testLoadFromContent() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testPut() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testPutInSupport() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testPutInContent() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testCheckContentByHash() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testPutIn() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testPutDirect() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testCreateAuthUrl() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testIsDir() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetFullPath() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testExists() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testList() {
	// fail("Not yet implemented");
	// }
	//
}
