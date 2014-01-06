package com.github.geub.sstc.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface StrutsAction {

	String expectedForward() default "";

	String expectedForwardPath() default "";

	String requestPath();

	String[] expectedActionMessages() default "";

	String[] expectedErrorMessages() default "";

}
