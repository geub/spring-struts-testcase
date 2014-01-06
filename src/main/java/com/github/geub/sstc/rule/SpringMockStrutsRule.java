package com.github.geub.sstc.rule;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionServlet;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import servletunit.HttpServletRequestSimulator;

import com.github.geub.sstc.annotations.StrutsAction;
import com.github.geub.sstc.mock.SpringMockStrutsTestCase;

public class SpringMockStrutsRule extends TestWatcher {

	private ActionServlet actionServlet;
	private SpringMockStrutsTestCase springMockStrutsTestCase = new SpringMockStrutsTestCase();

	public SpringMockStrutsRule(ActionServlet customActionServlet) {
		this.actionServlet = customActionServlet;
	}

	public SpringMockStrutsRule() {
		this(new ActionServlet());
	}

	@Override
	protected void starting(Description description) {
		try {
			StrutsAction strutsAction = getStrutsActionAnnotation(description);
			this.springMockStrutsTestCase.setUp(strutsAction.requestPathInfo(), this.actionServlet);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void succeeded(Description description) {
		StrutsAction strutsActionAnnotation = getStrutsActionAnnotation(description);
		this.springMockStrutsTestCase.verifyForward(strutsActionAnnotation.forward(), strutsActionAnnotation.forwardPath());
	}

	@Override
	protected void finished(Description description) {
		this.springMockStrutsTestCase.endRequest();
	}

	protected StrutsAction getStrutsActionAnnotation(Description description) {
		return description.getAnnotation(StrutsAction.class);
	}

	public HttpServletRequest getRequest() {
		return this.springMockStrutsTestCase.getRequest();
	}

	public void setCookies(Cookie... cookies) {
		getRequestSimulator().setCookies(cookies);
	}

	public void addCookie(String key, String value) {
		getRequestSimulator().addCookie(new Cookie(key, value));
	}

	private HttpServletRequestSimulator getRequestSimulator() {
		return (HttpServletRequestSimulator) getRequest();
	}

	public void doAction() {
		this.springMockStrutsTestCase.actionPerform();
	}

}
