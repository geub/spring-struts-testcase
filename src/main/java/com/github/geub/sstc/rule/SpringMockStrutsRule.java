package com.github.geub.sstc.rule;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.github.geub.sstc.annotations.StrutsAction;
import com.github.geub.sstc.mock.SpringMockStrutsTestCase;

public class SpringMockStrutsRule extends TestWatcher {

	private SpringMockStrutsTestCase springMockStrutsTestCase = new SpringMockStrutsTestCase();

	@Override
	protected void starting(Description description) {
		try {
			StrutsAction strutsAction = description.getAnnotation(StrutsAction.class);
			this.springMockStrutsTestCase.setUp(strutsAction.requestPathInfo());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void succeeded(Description description) {
	}

	public void doAction() {
		this.springMockStrutsTestCase.actionPerform();
	}

}
