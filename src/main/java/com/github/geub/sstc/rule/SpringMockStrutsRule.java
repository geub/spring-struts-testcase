package com.github.geub.sstc.rule;

import org.apache.struts.action.ActionServlet;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.github.geub.sstc.annotations.StrutsAction;
import com.github.geub.sstc.mock.SpringMockStrutsTestCase;

public class SpringMockStrutsRule extends TestWatcher {

	private ActionServlet actionServlet;

	public SpringMockStrutsRule(ActionServlet customActionServlet) {
		this.actionServlet = customActionServlet;
	}

	public SpringMockStrutsRule() {
		this(new ActionServlet());
	}

	private SpringMockStrutsTestCase springMockStrutsTestCase = new SpringMockStrutsTestCase();

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

	protected StrutsAction getStrutsActionAnnotation(Description description) {
		return description.getAnnotation(StrutsAction.class);
	}

	public void doAction() {
		this.springMockStrutsTestCase.actionPerform();
	}

}
