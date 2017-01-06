package com.gobalta.mule.mw.monitoring;

public class Summary {
    private String file = "";
    private String date = "";
    private int total = 0;
	private String success = "";
	private String duplicate = "";
	private String error = "";
	
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
		total = total + new Integer(success);
	}
	public String getDuplicate() {
		return duplicate;
	}
	public void setDuplicate(String duplicate) {
		this.duplicate = duplicate;
		total = total + new Integer(duplicate);
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
		total = total + new Integer(error);
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
