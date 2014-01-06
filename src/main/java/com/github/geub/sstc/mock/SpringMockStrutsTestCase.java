package com.github.geub.sstc.mock;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.AssertionFailedError;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.struts.ContextLoaderPlugIn;

import servletunit.HttpServletRequestSimulator;
import servletunit.ServletContextSimulator;
import servletunit.struts.ExceptionDuringTestError;
import servletunit.struts.MockStrutsTestCase;

import com.github.geub.sstc.plugin.TestXmlWebApplicationContext;
import com.github.geub.sstc.servlet.MockServletConfig;
import com.github.geub.sstc.servlet.RequestManager;

public class SpringMockStrutsTestCase extends MockStrutsTestCase {

	private RequestManager requestManager = initiateRequestManager();
	private WebApplicationContext webApplicationContext;

	public void setUp(String requestPath, ActionServlet actionServlet) throws Exception {
		super.setUp();
		this.config = new MockServletConfig();
		ServletContext servletContext = this.config.getServletContext();
		this.request = new HttpServletRequestSimulator(servletContext);
		this.context = (ServletContextSimulator) servletContext;
		setActionServlet(actionServlet);
		setRequestPathInfo(requestPath);
	}

	@Override
	public ActionServlet getActionServlet() {
		ActionServlet actionServlet = super.getActionServlet();
		this.requestManager.startRequest(this.request);
		this.webApplicationContext = initializeSpringContext(actionServlet);
		return actionServlet;
	}

	public void startRequest() {
		this.requestManager.startRequest(this.request);
	}

	public void endRequest() {
		this.requestManager.endRequest();
	}

	/**
	 * Delegate for the actual verifiers on the original MockStrutsTestCase
	 */
	public void verifyForward(String forward, String forwardPath) {
		verifyForward(forward);
		verifyForwardPath(forwardPath);
	}

	protected WebApplicationContext initializeSpringContext(ActionServlet actionServlet) {
		ModuleConfig moduleConfig = getModuleConfig();
		String attrName = getAttrName(moduleConfig);
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(this.context, attrName);
		if (webApplicationContext != null) {
			return webApplicationContext;
		}
		try {
			ContextLoaderPlugIn contextLoaderPlugIn = getContextLoader();
			contextLoaderPlugIn.setContextClass(TestXmlWebApplicationContext.class);
			ConfigLocationParser configLocationParser = getConfigLocationParser();
			contextLoaderPlugIn.setContextConfigLocation(configLocationParser.getContextConfigLocations());
			contextLoaderPlugIn.init(actionServlet, moduleConfig);
		} catch (ServletException e) {
			throw new IllegalStateException("Was not possible to start the spring context", e);
		}
		return WebApplicationContextUtils.getWebApplicationContext(this.context, attrName);
	}

	protected ConfigLocationParser getConfigLocationParser() {
		return new ConfigLocationParser();
	}

	/**
	 * Hook for changing the key for
	 */
	protected String getAttrName(ModuleConfig moduleConfig) {
		return ContextLoaderPlugIn.SERVLET_CONTEXT_PREFIX + moduleConfig.getPrefix();
	}

	/**
	 * Hook for changing de contextLoader
	 */
	protected ContextLoaderPlugIn getContextLoader() {
		return new ContextLoaderPlugIn();
	}

	@Override
	public void actionPerform() {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering");
		}
		if (!this.requestPathSet) {
			throw new IllegalStateException("You must call setRequestPathInfo() prior to calling actionPerform().");
		}
		if (!this.isInitialized) {
			throw new AssertionFailedError(
				"You are overriding the setUp() method without calling super.setUp().  You must call the superclass setUp() method in your TestCase subclass to ensure proper initialization.");
		}
		HttpServletRequest request = this.request;
		HttpServletResponse response = this.response;
		if (this.requestWrapper != null) {
			request = this.requestWrapper;
		}
		if (this.responseWrapper != null) {
			response = this.responseWrapper;
		}
		try {
			ActionServlet actionServlet = this.getActionServlet();
			beforeDoPost(this.webApplicationContext);
			actionServlet.doPost(request, response);
		} catch (NullPointerException npe) {
			String message = "A NullPointerException was thrown.  This may indicate an error in your ActionForm, or "
				+ "it may indicate that the Struts ActionServlet was unable to find struts config file.  "
				+ "TestCase is running from " + System.getProperty("user.dir") + " directory.  "
				+ "Context directory ";
			if (this.context.getContextDirectory() == null) {
				message += "has not been set.  Try calling setContextDirectory() with a relative or absolute path";
			} else {
				message = message + "is " + this.context.getContextDirectory().getAbsolutePath();
			}
			message = message + ".  struts config file must be found under the context directory, "
				+ "the directory the test case is running from, or in the classpath.";
			throw new ExceptionDuringTestError(message, npe);
		} catch (Exception e) {
			throw new ExceptionDuringTestError("An uncaught exception was thrown during actionExecute()", e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting");
		}
	}

	protected void beforeDoPost(WebApplicationContext webApplicationContext) {

	}

	/**
	 * Copied from the ActionServlet class, was a protected method, and the ActionServlet class will not be overridden cause the user may want to override itself.
	 */
	private ModuleConfig getModuleConfig() {
		Object config = this.request.getAttribute(Globals.MODULE_KEY);
		if (config == null) {
			config = this.context.getAttribute(Globals.MODULE_KEY);
		}
		return (ModuleConfig) config;
	}

	protected RequestManager initiateRequestManager() {
		return new RequestManager();
	}

	public ApplicationContext getApplicationContext() {
		return this.webApplicationContext;
	}

}
