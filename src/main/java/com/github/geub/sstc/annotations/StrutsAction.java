package com.github.geub.sstc.annotations;

public @interface StrutsAction {

	String getForward();

	String getForwardPaths();

	String getRequestPathInfo();

	String getActionMessages();

	String getErrorMessages();

}
