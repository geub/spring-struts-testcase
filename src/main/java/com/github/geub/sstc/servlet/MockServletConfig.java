package com.github.geub.sstc.servlet;

import javax.servlet.ServletContext;

import servletunit.ServletConfigSimulator;

public class MockServletConfig extends ServletConfigSimulator {

	private ServletContext servletContext;

	public MockServletConfig() {
		super();
		this.servletContext = new MockServletContext();
	}

	@Override
	public ServletContext getServletContext() {
		return this.servletContext;
	}

}
