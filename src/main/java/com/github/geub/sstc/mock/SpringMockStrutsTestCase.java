package com.github.geub.sstc.mock;

import org.apache.struts.action.ActionServlet;
import servletunit.struts.MockStrutsTestCase;

import com.github.geub.sstc.annotations.StrutsAction;

public class SpringMockStrutsTestCase extends MockStrutsTestCase {

	private StrutsAction strutsAction;
	
	public SpringMockStrutsTestCase(StrutsAction strutsAction) {
		this.strutsAction = strutsAction;
	}
	
	public void configureTest() throws Exception {
		super.setUp();
		if (strutsAction != null) {
			changeActionServlet();
			setRequestPathInfo(strutsAction.requestPathInfo());
		}
	}

	private void changeActionServlet() throws InstantiationException, IllegalAccessException {
		Class<?> customActionServletClass = strutsAction.actionServlet();
		if (customActionServletClass == null) {
			return;
		}
		Object customActionServlet = customActionServletClass.newInstance();
		if (!(customActionServlet instanceof ActionServlet)) {
			throw new IllegalArgumentException("Custom action servlet must override struts ActionServlet");
		}
		super.setActionServlet((ActionServlet) customActionServlet);
	}
	
}
