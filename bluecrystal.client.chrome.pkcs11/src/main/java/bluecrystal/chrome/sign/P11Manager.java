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

package bluecrystal.chrome.sign;

public class P11Manager implements IP11Manager {
	private Pkcs11Wrapper p11 = null;

	@Override
	public void init(String module, String otherPath) throws Exception {
		p11 = new Pkcs11Wrapper(module, otherPath);
	}

	@Override
	public String sign(int store, int alg, String userPIN, String certAlias,
			String orig) throws Exception {

		String ret = "";
		p11.setUserPIN(userPIN);
		p11.setCertAlias(certAlias);
		p11.setOrig(orig);
		p11.setAlg(alg);
		p11.setStore(store);
		p11.sign();
		ret = p11.getResult();
		if (p11.getLastError().length() != 0) {
			throw new Exception(p11.getLastError());
		}
		return ret;
	}

	@Override
	public String listCerts(int store, String userPIN) throws Exception {
		p11.setUserPIN(userPIN);
		p11.loadKeyStore();
		p11.setUserPIN(userPIN);
		p11.refreshCerts();
		p11.setStore(store);

		String ret = "";
		ret = p11.loadCertsJson();
		if (p11.getLastError() != null && p11.getLastError().length() != 0) {
			throw new Exception(p11.getLastError());
		}
		return ret;
	}

	@Override
	public String getCertificate(String alias) {
		String cert = p11.getCert(alias);
		return cert;
	}

	@Override
	public int getKeySize(String alias) {
		return p11.getKeySize(alias);
	}

	@Override
	public String getSubject(String alias) {
		return p11.getSubject(alias);
	}
}