package test.bluecrystal.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import bluecrystal.domain.StatusConst;

public class StatusConstTest {

	@Test
	public void testGetMessageByStatus() {
		try {
			String message = StatusConst.getMessageByStatus(StatusConst.AACOMPROMISE);
			System.out.println(message);			
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
			
		}
	}

}
