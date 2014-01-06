package org.apache.struts.action;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.digester.Digester;
import org.apache.struts.Globals;
import org.apache.struts.config.ApplicationConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

public class DelegatingTestActionServlet extends ActionServlet {

	private RequestProcessor requestProcessor;

	private ActionServlet actionServlet;

	public DelegatingTestActionServlet(ActionServlet actionServlet) {
		this.actionServlet = actionServlet;
	}

	@Override
	protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		RequestUtils.selectModule(request, getServletContext());
		getRequestProcessor(getModuleConfig(request)).process(request, response);
	}

	public void executeAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		((DelegatingTestRequestProcessor) getRequestProcessor(getModuleConfig(request))).processExecute(request, response);
	}

	@Override
	protected synchronized RequestProcessor getRequestProcessor(ModuleConfig config) throws ServletException {
		String key = Globals.REQUEST_PROCESSOR_KEY + config.getPrefix();
		ServletContext servletContext = getServletContext();
		RequestProcessor requestProcessor = (RequestProcessor) servletContext.getAttribute(key);
		if (requestProcessor == null) {
			createInstance(config);
			servletContext.setAttribute(key, this.requestProcessor);
		}
		return this.requestProcessor;
	}

	private void createInstance(ModuleConfig config) {
		RequestProcessor requestProcessor;
		try {
			requestProcessor = (RequestProcessor) RequestUtils.applicationInstance(config.getControllerConfig().getProcessorClass());
			this.requestProcessor = new DelegatingTestRequestProcessor(requestProcessor);
			this.requestProcessor.init(this.actionServlet, config);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Error creating request processor:", e);
		} catch (ServletException e) {
			throw new IllegalStateException("Error initializing request processor:", e);
		}
	}

	public Action getAction() {
		return ((DelegatingTestRequestProcessor) this.requestProcessor).getAction();
	}

	@Override
	public void addServletMapping(String servletName, String urlPattern) {
		this.actionServlet.addServletMapping(servletName, urlPattern);
	}

	@Override
	public void destroy() {
		this.actionServlet.destroy();
	}

	@Override
	protected void destroyApplications() {
		this.actionServlet.destroyApplications();
	}

	@Override
	protected void destroyConfigDigester() {
		this.actionServlet.destroyConfigDigester();
	}

	@Override
	protected void destroyDataSources() {
		this.actionServlet.destroyDataSources();
	}

	@Override
	protected void destroyInternal() {
		this.actionServlet.destroyInternal();
	}

	@Override
	protected void destroyModules() {
		this.actionServlet.destroyModules();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		this.actionServlet.doGet(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		process(request, response);
	}

	@Override
	public boolean equals(Object obj) {
		return this.actionServlet.equals(obj);
	}

	@Override
	public DataSource findDataSource(String key) {
		return this.actionServlet.findDataSource(key);
	}

	@Override
	public ActionFormBean findFormBean(String name) {
		return this.actionServlet.findFormBean(name);
	}

	@Override
	public ActionForward findForward(String name) {
		return this.actionServlet.findForward(name);
	}

	@Override
	public ActionMapping findMapping(String path) {
		return this.actionServlet.findMapping(path);
	}

	@Override
	public int getDebug() {
		return this.actionServlet.getDebug();
	}

	@Override
	public String getInitParameter(String name) {
		return this.actionServlet.getInitParameter(name);
	}

	@Override
	public Enumeration getInitParameterNames() {
		return this.actionServlet.getInitParameterNames();
	}

	@Override
	protected ApplicationConfig getApplicationConfig(HttpServletRequest request) {
		return this.actionServlet.getApplicationConfig(request);
	}

	@Override
	public MessageResources getInternal() {
		return this.actionServlet.getInternal();
	}

	@Override
	public MessageResources getResources() {
		return this.actionServlet.getResources();
	}

	@Override
	public ServletConfig getServletConfig() {
		return this.actionServlet.getServletConfig();
	}

	@Override
	public ServletContext getServletContext() {
		return this.actionServlet.getServletContext();
	}

	@Override
	public String getServletInfo() {
		return this.actionServlet.getServletInfo();
	}

	@Override
	public String getServletName() {
		return this.actionServlet.getServletName();
	}

	@Override
	public int hashCode() {
		return this.actionServlet.hashCode();
	}

	@Override
	public void init() throws ServletException {
		this.actionServlet.init();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.actionServlet.init(config);
	}

	@Override
	public void log(String message, int level) {
		this.actionServlet.log(message, level);
	}

	@Override
	protected ModuleConfig getModuleConfig(HttpServletRequest request) {
		return this.actionServlet.getModuleConfig(request);
	}

	@Override
	protected ApplicationConfig initApplicationConfig(String prefix, String path) throws ServletException {
		return this.actionServlet.initApplicationConfig(prefix, path);
	}

	@Override
	protected void initApplicationDataSources(ModuleConfig config) throws ServletException {
		this.actionServlet.initApplicationDataSources(config);
	}

	@Override
	protected void initApplicationMessageResources(ModuleConfig config) throws ServletException {
		this.actionServlet.initApplicationMessageResources(config);
	}

	@Override
	protected void initApplicationPlugIns(ModuleConfig config) throws ServletException {
		this.actionServlet.initApplicationPlugIns(config);
	}

	@Override
	protected Digester initConfigDigester() throws ServletException {
		return this.actionServlet.initConfigDigester();
	}

	@Override
	protected void initDataSources() throws ServletException {
		this.actionServlet.initDataSources();
	}

	@Override
	protected void initInternal() throws ServletException {
		this.actionServlet.initInternal();
	}

	@Override
	protected ModuleConfig initModuleConfig(String prefix, String paths) throws ServletException {
		return this.actionServlet.initModuleConfig(prefix, paths);
	}

	@Override
	protected void initModuleDataSources(ModuleConfig config) throws ServletException {
		this.actionServlet.initModuleDataSources(config);
	}

	@Override
	protected void initModuleMessageResources(ModuleConfig config) throws ServletException {
		this.actionServlet.initModuleMessageResources(config);
	}

	@Override
	protected void initModulePlugIns(ModuleConfig config) throws ServletException {
		this.actionServlet.initModulePlugIns(config);
	}

	@Override
	protected void initOther() throws ServletException {
		this.actionServlet.initOther();
	}

	@Override
	protected void initServlet() throws ServletException {
		this.actionServlet.initServlet();
	}

	@Override
	public void log(String message, Throwable t) {
		this.actionServlet.log(message, t);
	}

	@Override
	public void log(String msg) {
		this.actionServlet.log(msg);
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		this.actionServlet.service(req, res);
	}

	@Override
	public String toString() {
		return this.actionServlet.toString();
	}

	public ActionServlet getActionServlet() {
		return this.actionServlet;
	}

	public void setActionServlet(ActionServlet actionServlet) {
		this.actionServlet = actionServlet;
	}

	public void setRequestProcessor(RequestProcessor requestProcessor) {
		this.requestProcessor = requestProcessor;
	}

}
