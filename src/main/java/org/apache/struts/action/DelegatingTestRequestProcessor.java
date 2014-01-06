package org.apache.struts.action;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.springframework.util.ReflectionUtils;

public class DelegatingTestRequestProcessor extends RequestProcessor {

	private RequestProcessor requestProcessor;

	private Action action;
	private ActionMapping actionMapping;
	private ActionForm actionForm;

	public DelegatingTestRequestProcessor(RequestProcessor requestProcessor) {
		this.requestProcessor = requestProcessor;
	}

	@Override
	public void destroy() {
		this.requestProcessor.destroy();
	}

	@Override
	public void init(ActionServlet servlet, ModuleConfig moduleConfig) throws ServletException {
		this.requestProcessor.init(servlet, moduleConfig);
	}

	@Override
	/**
	 * It only does until the creation of the action so Its possible to change any attribute before executing the action. For example Mocking.
	 */
	public void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		processMultipart(request);
		String processPath = processPath(request, response);
		if (processPath == null) {
			return;
		}
		processLocale(request, response);
		processContent(request, response);
		processNoCache(request, response);
		if (!processPreprocess(request, response)) {
			return;
		}
		ActionMapping mapping = processMapping(request, response, processPath);
		if (mapping == null) {
			return;
		}
		if (!processRoles(request, response, mapping)) {
			return;
		}
		ActionForm form = processActionForm(request, response, mapping);
		processPopulate(request, response, form, mapping);
		if (!processValidate(request, response, form, mapping)) {
			return;
		}
		if (!processForward(request, response, mapping)) {
			return;
		}
		if (!processInclude(request, response, mapping)) {
			return;
		}
		Action action = processActionCreate(request, response, mapping);
		if (action == null) {
			return;
		}
		this.action = action;
		this.actionForm = form;
		this.actionMapping = mapping;
	}

	/**
	 * Execute the action forwarding
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void processExecute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		ActionForward forward = processActionPerform(request, response, this.action, this.actionForm, this.actionMapping);
		processForwardConfig(request, response, forward);
	}

	@Override
	protected HttpServletRequest processMultipart(HttpServletRequest request) {
		return (HttpServletRequest) delegateProcess("processMultipart", request);
	}

	@Override
	protected String processPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return (String) delegateProcess("processPath", request, response);
	}

	@Override
	protected void processLocale(HttpServletRequest request, HttpServletResponse response) {
		delegateProcess("processLocale", request, response);
	}

	@Override
	protected void processContent(HttpServletRequest request, HttpServletResponse response) {
		delegateProcess("processContent", request, response);
	}

	@Override
	protected void processNoCache(HttpServletRequest request, HttpServletResponse response) {
		delegateProcess("processNoCache", request, response);
	}

	@Override
	protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
		return (Boolean) delegateProcess("processPreProcess", request, response);
	}

	@Override
	protected ActionMapping processMapping(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
		return (ActionMapping) delegateProcess("processMapping", request, response, path);
	}

	@Override
	protected boolean processRoles(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
		return (Boolean) delegateProcess("processRoles", request, response);
	}

	@Override
	protected ActionForm processActionForm(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) {
		return (ActionForm) delegateProcess("processActionForm", request, response, mapping);
	}

	@Override
	protected void processPopulate(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws ServletException {
		delegateProcess("processPopulate", request, response, form, mapping);
	}

	@Override
	protected boolean processValidate(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws IOException, ServletException {
		return (Boolean) delegateProcess("processValidate", request, response, form, mapping);
	}

	@Override
	protected boolean processForward(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
		return (Boolean) delegateProcess("processForward", request, response, mapping);
	}

	@Override
	protected boolean processInclude(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
		return (Boolean) delegateProcess("processInclude", request, response, mapping);
	}

	@Override
	protected Action processActionCreate(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException {
		return (Action) delegateProcess("processActionCreate", request, response, mapping);
	}

	@Override
	protected void doForward(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		delegateProcess("doForward", uri, request, response);
	}

	@Override
	protected void doInclude(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		delegateProcess("doInclude", uri, request, response);
	}

	@Override
	protected ServletContext getServletContext() {
		return (ServletContext) delegateProcess("getServletContext");
	}

	@Override
	protected ActionForward processActionPerform(HttpServletRequest request, HttpServletResponse response, Action action, ActionForm form, ActionMapping mapping) throws IOException, ServletException {
		return (ActionForward) delegateProcess("processActionPerform", request, response, action, form, mapping);
	}

	@Override
	protected MessageResources getInternal() {
		return (MessageResources) delegateProcess("getInternal");
	}

	@Override
	protected void internalModuleRelativeForward(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		delegateProcess("internalModuleRelativeForward", uri, request, response);
	}

	@Override
	protected void internalModuleRelativeInclude(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		delegateProcess("internalModuleRelativeInclude", uri, request, response);
	}

	@Override
	protected void log(String message) {
		delegateProcess("log", message);
	}

	@Override
	protected void log(String message, Throwable exception) {
		delegateProcess("log", message, exception);
	}

	@Override
	protected ActionForward processException(HttpServletRequest request, HttpServletResponse response, Exception exception, ActionForm form, ActionMapping mapping) throws IOException,
		ServletException {
		return (ActionForward) delegateProcess("processException", request, response, exception, form, mapping);
	}

	@Override
	protected void processActionForward(HttpServletRequest request, HttpServletResponse response, ActionForward forward) throws IOException, ServletException {
		delegateProcess("processActionForward", request, response, forward);
	}

	@Override
	protected void processForwardConfig(HttpServletRequest request, HttpServletResponse response, ForwardConfig forward) throws IOException, ServletException {
		delegateProcess("processForwardConfig", request, response, forward);
	}

	@Override
	public String toString() {
		return this.requestProcessor.toString();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return delegateProcess("clone");
	}

	@Override
	public boolean equals(Object obj) {
		return this.requestProcessor.equals(obj);
	}

	@Override
	protected void finalize() throws Throwable {
		delegateProcess("finalize");
	}

	@Override
	public int getDebug() {
		return this.requestProcessor.getDebug();
	}

	@Override
	public int hashCode() {
		return this.requestProcessor.hashCode();
	}

	public Action getAction() {
		return this.action;
	}

	public ActionForm getActionForm() {
		return this.actionForm;
	}

	public ActionMapping getActionMapping() {
		return this.actionMapping;
	}

	private Object delegateProcess(String methodName, Object... args) {
		try {
			List<Class<?>> argsClass = new ArrayList<Class<?>>();
			for (Object argClass : args) {
				argsClass.add(argClass.getClass());
			}
			Method method = this.requestProcessor.getClass().getMethod(methodName, argsClass.toArray(new Class[argsClass.size()]));
			return ReflectionUtils.invokeMethod(method, this.requestProcessor, args);
		} catch (Exception e) {
			throw new IllegalStateException("Was not possible to call the method on the request processor", e);
		}
	}

}
