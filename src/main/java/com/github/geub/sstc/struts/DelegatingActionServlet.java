package com.github.geub.sstc.struts;

import javax.servlet.ServletException;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;

public class DelegatingActionServlet extends ActionServlet {

	private RequestProcessor requestProcessor;

	@Override
	protected synchronized RequestProcessor getRequestProcessor(ModuleConfig config) throws ServletException {
		String key = Globals.REQUEST_PROCESSOR_KEY + config.getPrefix();
		RequestProcessor requestProcessor = (RequestProcessor) getServletContext().getAttribute(key);
		if (requestProcessor == null) {
			createInstance(config);
		}
		return this.requestProcessor;
	}

	private void createInstance(ModuleConfig config) {
		RequestProcessor requestProcessor;
		try {
			requestProcessor = (RequestProcessor) RequestUtils.applicationInstance(config.getControllerConfig().getProcessorClass());
			this.requestProcessor = new DelegatingTestRequestProcessor(requestProcessor);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public Action getAction() {
		return ((DelegatingTestRequestProcessor) this.requestProcessor).getAction();
	}
}
