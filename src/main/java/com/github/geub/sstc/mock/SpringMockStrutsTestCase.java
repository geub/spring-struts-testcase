package com.github.geub.sstc.mock;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.struts.ContextLoaderPlugIn;

import servletunit.HttpServletRequestSimulator;
import servletunit.ServletContextSimulator;
import servletunit.struts.MockStrutsTestCase;

import com.github.geub.sstc.servlet.MockServletConfig;

public class SpringMockStrutsTestCase extends MockStrutsTestCase {

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
		initializeSpringContext(actionServlet);
		return actionServlet;
	}

	private void initializeSpringContext(ActionServlet actionServlet) {
		ModuleConfig moduleConfig = getModuleConfig();
		String attrName = getAttrName(moduleConfig);
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(actionServlet.getServletContext(), attrName);
		if (webApplicationContext != null) {
			return;
		}
		try {
			ContextLoaderPlugIn contextLoaderPlugIn = getContextLoader();
			ConfigLocationParser configLocationParser = new ConfigLocationParser();
			contextLoaderPlugIn.setContextConfigLocation(configLocationParser.getContextConfigLocations());
			contextLoaderPlugIn.init(actionServlet, moduleConfig);
		} catch (ServletException e) {
			throw new IllegalStateException("Was not possible to start the spring context", e);
		}
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

	/**
	 * Delegate for the actual verifiers on the original MockStrutsTestCase
	 */
	public void verifyForward(String forward, String forwardPath) {
		verifyForward(forward);
		verifyForwardPath(forwardPath);
	}

}
