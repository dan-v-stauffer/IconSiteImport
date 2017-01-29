package com.gobalta.mule.mw.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PrincipalInvestigator implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6179563556882554023L;
	private String firstName;
	private String lastName;
	private String title = "pi";
	private String email;
	
	@JsonProperty("first_name")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@JsonProperty("last_name")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof PrincipalInvestigator)) {
			return false;
		}
		PrincipalInvestigator p = (PrincipalInvestigator)o;
		
		return Objects.equals(this.firstName, p.getFirstName()) &&
				Objects.equals(this.lastName, p.getLastName()) &&
				Objects.equals(this.title, p.getTitle()) &&
				Objects.equals(this.email, p.getEmail());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, title, email);
	}
	
}
