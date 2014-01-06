package com.github.geub.sstc.plugin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.context.request.SessionScope;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.github.geub.sstc.servlet.RequestManager;

public class TestXmlWebApplicationContext extends XmlWebApplicationContext {

	private static final String SCOPE_CONFIGURER = "scopeConfigurer";

	@Override
	protected void onRefresh() {
		super.onRefresh();
		Map beansOfType = getBeansOfType(CustomScopeConfigurer.class);
		if (!beansOfType.isEmpty()) {
			return;
		}
		addCustomScopeConfigurerBean();
	}

	private void addCustomScopeConfigurerBean() {
		AutowireCapableBeanFactory autowireCapableBeanFactory = getAutowireCapableBeanFactory();
		CustomScopeConfigurer customScopeConfigurer = (CustomScopeConfigurer) autowireCapableBeanFactory.createBean(CustomScopeConfigurer.class);
		HashMap map = new HashMap();
		map.put(RequestManager.SESSION, SessionScope.class);
		map.put(RequestManager.REQUEST, RequestScope.class);
		customScopeConfigurer.setScopes(map);
		autowireCapableBeanFactory.initializeBean(customScopeConfigurer, SCOPE_CONFIGURER);
	}

}
