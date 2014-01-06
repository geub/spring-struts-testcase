package com.github.geub.test.component;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class RequestScopedBean {

	@Autowired
	private HttpServletRequest httpServletRequest;

	public void doSomething() {
		Assert.assertNotNull(this.httpServletRequest);
	}

}
