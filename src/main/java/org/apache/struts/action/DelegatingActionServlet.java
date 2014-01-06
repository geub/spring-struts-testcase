package org.apache.struts.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;

public class DelegatingActionServlet extends ActionServlet {

	private RequestProcessor requestProcessor;

	private ActionServlet actionServlet;

	public DelegatingActionServlet(ActionServlet actionServlet) {
		this.actionServlet = actionServlet;
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		RequestUtils.selectModule(request, getServletContext());
		getRequestProcessor(getModuleConfig(request)).process(request, response);
	}

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
			throw new IllegalStateException("Error creating request processor:", e);
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Action getAction() {
		return ((DelegatingTestRequestProcessor) this.requestProcessor).getAction();
	}
}
