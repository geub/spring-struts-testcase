package com.github.geub.test.actions;

import org.junit.Rule;
import org.junit.Test;

import com.github.geub.sstc.annotations.StrutsAction;
import com.github.geub.sstc.rule.SpringMockStrutsRule;

public class TestInjectAction {

	@Rule
	public SpringMockStrutsRule springMockStrutsRule = new SpringMockStrutsRule();

	@Test
	@StrutsAction(requestPathInfo = "/inject", forward = "success", forwardPath = "/inject/bean.jsp")
	public void testInjectComponentToActionSucessFull() {
		this.springMockStrutsRule.doAction();
	}

}
