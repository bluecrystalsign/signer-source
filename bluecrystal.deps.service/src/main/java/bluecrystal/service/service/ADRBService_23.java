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

package bluecrystal.service.service;

import bluecrystal.service.helper.UtilsLocal;

public class ADRBService_23 extends BaseService {

	public ADRBService_23() {
		super();
		minKeyLen = 2048;
		signingCertFallback = false;
		addChain = false;
		signedAttr = true;
//		version = 3; // CEF
		version = 1;
		policyHash = UtilsLocal.convHexToByte(SIG_POLICY_HASH_23);
		policyId = SIG_POLICY_BES_ID_23;
		policyUri = SIG_POLICY_URI_23;
	}
}
