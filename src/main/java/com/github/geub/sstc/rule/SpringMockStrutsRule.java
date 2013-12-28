package com.github.geub.sstc.rule;

import org.apache.struts.config.ModuleConfigFactory;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.github.geub.sstc.annotations.StrutsAction;
import com.github.geub.sstc.mock.SpringMockStrutsTestCase;

public class SpringMockStrutsRule extends TestWatcher {

	private SpringMockStrutsTestCase springMockStrutsTestCase;
	
	@Override
	protected void starting(Description description) {
		try {
			StrutsAction strutsAction = description.getAnnotation(StrutsAction.class);
			this.springMockStrutsTestCase = new SpringMockStrutsTestCase(strutsAction);
			this.springMockStrutsTestCase.configureTest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void succeeded(Description description) {
		super.succeeded(description);
	}

	public void actionPerform() {
		springMockStrutsTestCase.actionPerform();
	}
	
}
