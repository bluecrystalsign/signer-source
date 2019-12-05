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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import bluecrystal.service.interfaces.RepoLoader;
import bluecrystal.service.util.PrefsFactory;

public class FSZipRepoLoader implements RepoLoader {
	
	private String certFolder = PrefsFactory.getCertFolder(); // $NON-NLS-1$
	private byte[] content;

	@Override
	public InputStream load(String key) throws Exception {

		if(content == null){
			System.out.println("loading....");
			String pathName = certFolder+".zip";
			FileInputStream fis = null;
			fis = new FileInputStream(pathName);
			content = new byte[fis.available()];
			fis.read(content);
			fis.close();
		} 
		
		InputStream myIs = new ByteArrayInputStream(content);
		
		
		ZipInputStream zis = new ZipInputStream(myIs);

		ZipEntry ze = zis.getNextEntry();

		long totalLen = 0l;
		ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
		while (ze != null) {
			if (!ze.isDirectory() 
					&& ze.getName().startsWith(key)
				) {
				totalLen += ze.getSize();
				int len = 0;
				byte[] buffer = new byte[1024];

				while ((len = zis.read(buffer)) > 0) {
					outBuffer.write(buffer, 0, len);
				}
			}
			zis.closeEntry();
			outBuffer.write("\n".getBytes(), 0, 1);
			ze = zis.getNextEntry();
		}
		
		byte[] b = outBuffer.toByteArray();
		System.out.println("Total Size ( " + totalLen + " ) ");
		System.out.println("Total Size ( " + b.length + " ) ");
		zis.close();
		return new ByteArrayInputStream(b);
	}

	@Override
	public boolean isDir(String object) throws Exception {
		return false;
	}

	@Override
	public String getFullPath(String object) {
		return certFolder+".zip";
	}

	@Override
	public boolean exists(String object) throws Exception {
		File f = new File(getFullPath(object));
		return f.exists();
	}

	@Override
	public String[] list(String object) throws Exception {
		return null;
	}

	@Override
	public InputStream loadFromContent(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String Put(InputStream input, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String PutInSupport(InputStream input, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String PutInContent(InputStream input, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String checkContentByHash(String sha256) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String PutIn(InputStream input, String key, String bucket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String PutDirect(InputStream input, String key, String bucket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createAuthUrl(String object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
