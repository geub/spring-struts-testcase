package com.github.geub.sstc.mock;

public class ConfigLocationParser {

	private String DEFAULT_SPRING_CONTEXT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";

	public String getContextConfigLocations() {
		return this.DEFAULT_SPRING_CONTEXT_CONFIG_LOCATION;
	}
}
