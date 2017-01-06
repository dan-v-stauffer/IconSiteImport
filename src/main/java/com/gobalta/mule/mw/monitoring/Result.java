package com.gobalta.mule.mw.monitoring;

import java.util.List;

public class Result {
	private Configuration configuration;
	private List<Summary> summary;
	
	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	public List<Summary> getSummary() {
		return summary;
	}
	public void setSummary(List<Summary> summary) {
		this.summary = summary;
	}
	
	
}
