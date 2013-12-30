package com.github.geub.sstc.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.apache.struts.action.ActionServlet;

@Retention(RetentionPolicy.RUNTIME)
public @interface StrutsAction {

	String forward() default "";

	String forwardPath() default "";

	String requestPathInfo();

	String[] actionMessages() default "";

	String[] errorMessages() default "";
	
	Class<?> actionServlet() default ActionServlet.class;

}
