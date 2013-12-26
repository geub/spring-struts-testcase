package com.github.geub.test.actions;

import org.junit.Test;

import servletunit.struts.MockStrutsTestCase;

public class TestLoginAction extends MockStrutsTestCase {

	@Test
	public void testLoginActionSucessFull() {
		setRequestPathInfo("/login");
		actionPerform();
		verifyForward("success");
		verifyForwardPath("/main/success.jsp");
	}

}
