package org.apache.struts.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;

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
		return this.requestProcessor.processMultipart(request);
	}

	@Override
	protected String processPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return this.requestProcessor.processPath(request, response);
	}

	@Override
	protected void processLocale(HttpServletRequest request, HttpServletResponse response) {
		this.requestProcessor.processLocale(request, response);
	}

	@Override
	protected void processContent(HttpServletRequest request, HttpServletResponse response) {
		this.requestProcessor.processContent(request, response);
	}

	@Override
	protected void processNoCache(HttpServletRequest request, HttpServletResponse response) {
		this.requestProcessor.processNoCache(request, response);
	}

	@Override
	protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
		return this.requestProcessor.processPreprocess(request, response);
	}

	@Override
	protected ActionMapping processMapping(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
		return this.requestProcessor.processMapping(request, response, path);
	}

	@Override
	protected boolean processRoles(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
		return this.requestProcessor.processRoles(request, response, mapping);
	}

	@Override
	protected ActionForm processActionForm(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) {
		return this.requestProcessor.processActionForm(request, response, mapping);
	}

	@Override
	protected void processPopulate(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws ServletException {
		this.requestProcessor.processPopulate(request, response, form, mapping);
	}

	@Override
	protected boolean processValidate(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws IOException, ServletException {
		return this.requestProcessor.processValidate(request, response, form, mapping);
	}

	@Override
	protected boolean processForward(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
		return this.requestProcessor.processForward(request, response, mapping);
	}

	@Override
	protected boolean processInclude(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException, ServletException {
		return this.requestProcessor.processInclude(request, response, mapping);
	}

	@Override
	protected Action processActionCreate(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException {
		return this.requestProcessor.processActionCreate(request, response, mapping);
	}

	@Override
	protected void doForward(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		this.requestProcessor.doForward(uri, request, response);
	}

	@Override
	protected void doInclude(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		this.requestProcessor.doInclude(uri, request, response);
	}

	@Override
	protected ServletContext getServletContext() {
		return this.requestProcessor.getServletContext();
	}

	@Override
	protected ActionForward processActionPerform(HttpServletRequest request, HttpServletResponse response, Action action, ActionForm form, ActionMapping mapping) throws IOException, ServletException {
		return this.requestProcessor.processActionPerform(request, response, action, form, mapping);
	}

	@Override
	protected MessageResources getInternal() {
		return this.requestProcessor.getInternal();
	}

	@Override
	protected void internalModuleRelativeForward(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		this.requestProcessor.internalModuleRelativeForward(uri, request, response);
	}

	@Override
	protected void internalModuleRelativeInclude(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		this.requestProcessor.internalModuleRelativeInclude(uri, request, response);
	}

	@Override
	protected void log(String message) {
		this.requestProcessor.log(message);
	}

	@Override
	protected void log(String message, Throwable exception) {
		this.requestProcessor.log(message, exception);
	}

	@Override
	protected ActionForward processException(HttpServletRequest request, HttpServletResponse response, Exception exception, ActionForm form, ActionMapping mapping) throws IOException,
		ServletException {
		return this.requestProcessor.processException(request, response, exception, form, mapping);
	}

	@Override
	protected void processActionForward(HttpServletRequest request, HttpServletResponse response, ActionForward forward) throws IOException, ServletException {
		this.requestProcessor.processActionForward(request, response, forward);
	}

	@Override
	protected void processForwardConfig(HttpServletRequest request, HttpServletResponse response, ForwardConfig forward) throws IOException, ServletException {
		this.requestProcessor.processForwardConfig(request, response, forward);
	}

	@Override
	public String toString() {
		return this.requestProcessor.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return this.requestProcessor.equals(obj);
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

}
