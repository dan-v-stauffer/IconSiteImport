package com.gobalta.mule.mw.rest.studysite;

public class ErrorMessage {

	private String gbCode;
	private String message;
	private String details;
	
	public String getGbCode() {
		return gbCode;
	}
	public void setGbCode(String gbCode) {
		this.gbCode = gbCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
	public String toString() {
		return "{\"gbCode\":\"" + gbCode + "\"," +
				"\"message\":\"" + message + "\"," +
				"\"details\":\"" + details + "\"}";
	}
	
}
