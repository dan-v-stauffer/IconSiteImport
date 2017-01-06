package com.gobalta.mule.mw.exception;

public class GoBaltoMWException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 436902233132017924L;
	
	private String gbCode;
	
	public GoBaltoMWException(String gbCode, String message){
		this(gbCode, message , new Exception(message));
	}
	
	public GoBaltoMWException(String message, Exception e){
		super(message,e);
	}
	
	public GoBaltoMWException(String gbCode, String message, Exception e){
		super(message,e);
		this.gbCode = gbCode;
	}

	public String getGbCode() {
		return gbCode;
	}
	
}
