package com.github.geub.sstc.servlet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestManager {

	public static final String REQUEST = "request";
	public static final String SESSION = "session";

	public void startRequest(HttpServletRequest httpServletRequest) {
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));
	}

	public void endRequest() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (servletRequestAttributes == null) {
			return;
		}
		servletRequestAttributes.requestCompleted();
		RequestContextHolder.resetRequestAttributes();
	}

}
