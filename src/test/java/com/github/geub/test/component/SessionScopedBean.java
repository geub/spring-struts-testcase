package com.github.geub.test.component;

import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class SessionScopedBean {

	@Autowired
	private HttpSession httpSession;

	public void doSomething() {
		Assert.assertNotNull(this.httpSession);
	}

}
