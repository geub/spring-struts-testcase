spring-struts-testcase
======================

Spring struts testcase was created to help creating tests utilizing strutstestcase (ref) that use spring. If you have a customized Request processor you can use this framework to test it too.

Testing action
--------------

The following class might be something you want to test it:

    public class InjectAction extends Action {
    
      @Autowired
      private Bean component;
      
      @Override
      public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.component.foo();
        return mapping.findForward("success");
      }
    }

And this is the configuration for struts: 

    <?xml version="1.0" encoding="ISO-8859-1"?>
    <!DOCTYPE struts-config PUBLIC
        "-//Apache Software Foundation//DTD Struts Configuration 1.1//EN"
        "http://jakarta.apache.org/struts/dtds/struts-config_1_1.dtd">
        <struts-config>
          <form-beans>
            <form-bean name="injectionForm" type="com.github.geub.test.forms.InjectionForm"/>
          </form-beans>
  
        <action-mappings>
            <action path="/inject" type="com.github.geub.test.actions.InjectAction" input="success" name="loginForm" scope="request">
              <forward name="success" path="/inject/bean.jsp"/>
            </action>
            <action path="/injectScopeBean" type="com.github.geub.test.actions.InjectScopedBeanAction" input="success" name="injectionForm" scope="request" >
            	<forward name="success" path="/inject/beanScopedBean.jsp"/>
            </action>
        </action-mappings>       
  
        <controller processorClass="org.springframework.web.struts.AutowiringRequestProcessor" />
        
        <plug-in className="org.springframework.web.struts.ContextLoaderPlugIn">
        <set-property property="contextConfigLocation" value="/WEB-INF/applicationContext.xml"/>
        </plug-in>
    </struts-config>

If you wanna test this action you might do it like this:

    public class TestInjectAction {
    
    	@Rule
    	public SpringMockStrutsRule springMockStrutsRule = new SpringMockStrutsRule();
    
    	@Test
    	@StrutsAction(requestPath = "/inject", expectedForward = "success", expectedForwardPath = "/inject/bean.jsp")
    	public void testInjectComponentToActionSucessFull() {
    		
    	}
    
    }
    
This might test it if your configuration is ok, and if you are using a custom request processor if it works as expected.
The way the SpringMockStrutsRule works is that the body of the method can be used to mock any object you want, so if you want a mock a DAO or something else you just use your favorite mock framework

After the test, the framework its gonna test the information passed on the annotation.

You can use to test request and session scoped beans:

    public class InjectScopedBeanAction extends Action {
    
    	@Autowired
    	private SessionScopedBean sessionScopeBean;
    
    	@Autowired
    	private RequestScopedBean requestScopeBean;
    
    	@Override
    	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    		this.sessionScopeBean.doSomething();
    		this.requestScopeBean.doSomething();
    		return mapping.findForward("success");
    	}
    
    }
    
        @Component
    @Scope("request")
    public class RequestScopedBean {
    
    	@Autowired
    	private HttpServletRequest httpServletRequest;
    
    	public void doSomething() {
    		Assert.assertNotNull(this.httpServletRequest);
    	}
    
    }
    
        @Component
    @Scope("session")
    public class SessionScopedBean {
    
    	@Autowired
    	private HttpSession httpSession;
    
    	public void doSomething() {
    		Assert.assertNotNull(this.httpSession);
    	}
    
    }
    
Its gonna work the same way, you dont have to put anything else in the annotation.

