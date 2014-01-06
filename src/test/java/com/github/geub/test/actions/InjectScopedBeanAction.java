package com.github.geub.test.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.geub.test.component.RequestScopedBean;
import com.github.geub.test.component.SessionScopedBean;

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
