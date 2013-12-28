package com.github.geub.test.actions;

import org.junit.Rule;
import org.junit.Test;

import com.github.geub.sstc.annotations.StrutsAction;
import com.github.geub.sstc.rule.SpringMockStrutsRule;

public class TestLoginAction {

	@Rule
	public SpringMockStrutsRule springMockStrutsRule = new SpringMockStrutsRule();
	
	@Test
	@StrutsAction(requestPathInfo = "/login",forward="success",forwardPath="/main/success.jsp")
	public void testLoginActionSucessFull() {
		springMockStrutsRule.actionPerform();
	}

}
