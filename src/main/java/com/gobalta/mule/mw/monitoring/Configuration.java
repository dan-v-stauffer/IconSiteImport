package com.gobalta.mule.mw.monitoring;

@SuppressWarnings("unused")
public class Configuration {
	private String outputFileDirectory = "";
    private String processedFileDirectory = "";
	private String sftpHost = "";
	private String sftpUser = "";
	private String sftpPassword = "";
	private String sftpPath = "";
	private String sftpPort = "";
	private String sftpTimeout = "";
	private String errorDirectory = "";
	private String sftpPollingFrequency = "";
	private String restUrl = "";
	private String restSecurityToken = "";
	private String monitoringPort = "";
	private String monitoringHost = "";
	private String commitHash = "";
	private String commitDate = "";
	
	public void setOutputFileDirectory(String outputFileDirectory) {
		this.outputFileDirectory = outputFileDirectory;
	}
	public void setSftpHost(String sftpHost) {
		this.sftpHost = sftpHost;
	}
	public void setSftpUser(String sftpUser) {
		this.sftpUser = sftpUser;
	}
	public void setSftpPassword(String sftpPassword) {
		this.sftpPassword = sftpPassword;
	}
	public void setSftpPath(String sftpPath) {
		this.sftpPath = sftpPath;
	}
	public void setSftpPort(String sftpPort) {
		this.sftpPort = sftpPort;
	}
	public void setSftpTimeout(String sftpTimeout) {
		this.sftpTimeout = sftpTimeout;
	}
	public void setErrorDirectory(String errorDirectory) {
		this.errorDirectory = errorDirectory;
	}
	public void setSftpPollingFrequency(String sftpPollingFrequency) {
		this.sftpPollingFrequency = sftpPollingFrequency;
	}
	public void setRestUrl(String restUrl) {
		this.restUrl = restUrl;
	}
	public void setRestSecurityToken(String restSecurityToken) {
		this.restSecurityToken = restSecurityToken;
	}
	public void setProcessedFileDirectory(String processedFileDirectory) {
		this.processedFileDirectory = processedFileDirectory;
	}
	public void setMonitoringPort(String val) {
		this.monitoringPort = val;
	}
	public void setMonitoringHost(String val) {
		this.monitoringHost = val;
	}
	public void setCommitHash(String commitHash) {
		this.commitHash = commitHash;
	}
	public void setCommitDate(String commitDate) {
		this.commitDate = commitDate;
	}
	/*
	public String getSftpHost() {
		return sftpHost;
	}
	public String getSftpUser() {
		return sftpUser;
	}
	public String getSftpPassword() {
		return sftpPassword;
	}
	public String getSftpPath() {
		return sftpPath;
	}
	public String getSftpPort() {
		return sftpPort;
	}
	public String getSftpTimeout() {
		return sftpTimeout;
	}
	public String getErrorDirectory() {
		return errorDirectory;
	}
	public String getSftpPollingFrequency() {
		return sftpPollingFrequency;
	}
	public String getRestUrl() {
		return restUrl;
	}
	public String getRestSecurityToken() {
		return restSecurityToken;
	}
	public String getProcessedFileDirectory() {
		return processedFileDirectory;
	}
	public String getOutputFileDirectory() {
		return outputFileDirectory;
	}
	public String getMonitoringPort() {
		return this.monitoringPort;
	}
	public String getMonitoringHost() {
		return this.monitoringHost;
	}
	*/

	public String processedFileDirectory() {
		return processedFileDirectory;
	}
	
	public String getCommitHash() {
		return commitHash;
	}
	
	public String getCommitDate() {
		return commitDate;
	}
}
