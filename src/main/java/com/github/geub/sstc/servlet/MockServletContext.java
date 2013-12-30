package com.github.geub.sstc.servlet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;

import servletunit.ServletContextSimulator;

public class MockServletContext extends ServletContextSimulator {

	private ResourceLoader resourceLoader;

	public MockServletContext() {
		this.resourceLoader = new DefaultResourceLoader();
	}

	@Override
	public Set<String> getResourcePaths(String path) {
		String actualPath = (path.endsWith("/") ? path : path + "/");
		Resource resource = this.resourceLoader.getResource(getResourceLocation(actualPath));
		try {
			File file = resource.getFile();
			String[] fileList = file.list();
			if (ObjectUtils.isEmpty(fileList)) {
				return null;
			}
			Set<String> resourcePaths = new LinkedHashSet<String>(fileList.length);
			for (int i = 0; i < fileList.length; i++) {
				String resultPath = actualPath + fileList[i];
				if (resource.createRelative(fileList[i]).getFile().isDirectory()) {
					resultPath += "/";
				}
				resourcePaths.add(resultPath);
			}
			return resourcePaths;
		} catch (IOException ex) {
			return null;
		}
	}

	@Override
	public String getRealPath(String path) {
		Resource resource = this.resourceLoader.getResource(getResourceLocation(path));
		try {
			return resource.getFile().getAbsolutePath();
		} catch (IOException ex) {
			return null;
		}
	}

	private String getResourceLocation(String actualPath) {
		return actualPath;
	}

}
