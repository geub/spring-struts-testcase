package com.github.geub.sstc.mock;

import javax.servlet.ServletException;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.struts.ContextLoaderPlugIn;

import servletunit.struts.MockStrutsTestCase;

public class SpringMockStrutsTestCase extends MockStrutsTestCase {

	public void setUp(String requestPath) throws Exception {
		super.setUp();
		setRequestPathInfo(requestPath);
	}

	@Override
	public ActionServlet getActionServlet() {
		ActionServlet actionServlet = super.getActionServlet();
		initializeSpringContextIfNecessary(actionServlet);
		return actionServlet;
	}

	private void initializeSpringContextIfNecessary(ActionServlet actionServlet) {
		ModuleConfig moduleConfig = getModuleConfig(actionServlet);
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
	private ModuleConfig getModuleConfig(ActionServlet actionServlet) {
		Object config = this.request.getAttribute(Globals.MODULE_KEY);
		if (config == null) {
			config = actionServlet.getServletContext().getAttribute(Globals.MODULE_KEY);
		}
		return (ModuleConfig) config;
	}

}
