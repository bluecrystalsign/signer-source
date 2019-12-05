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

package bluecrystal.example.web.domain;

import bluecrystal.domain.StatusConst;

public class SignedEnvelope {
	private String signedContent;
	private int signStatus;
	private String statusMessage;
	private String certB64;
	private String certSubject;
	public String getSignedContent() {
		return signedContent;
	}
	public void setSignedContent(String signedContent) {
		this.signedContent = signedContent;
	}
	public boolean isOk() {
		return signStatus == StatusConst.GOOD;
	}
	public int getSignStatus() {
		return signStatus;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}

	public String getCertB64() {
		return certB64;
	}
	public void setCertB64(String certB64) {
		this.certB64 = certB64;
	}
	public String getCertSubject() {
		return certSubject;
	}
	public void setCertSubject(String certSubject) {
		this.certSubject = certSubject;
	}
	public SignedEnvelope(String signedContent, int signStatus, String statusMessage, 
			String certB64,
			String certSubject) {
		super();
		this.signedContent = signedContent;
		this.signStatus = signStatus;
		this.statusMessage = statusMessage;
		this.certB64 = certB64;
		this.certSubject = certSubject;
	}
}
