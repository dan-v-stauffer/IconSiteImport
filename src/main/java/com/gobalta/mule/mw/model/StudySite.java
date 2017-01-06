package com.gobalta.mule.mw.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudySite implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8542595340809326730L;
	private String siteNumber;
	private String studyName;
	private String institution;
	private String street;
	private String state;
	private String city;
	private String postalCode;
	private String countryCode;
	private String comments;
	private PrincipalInvestigator principalInvestigator = new PrincipalInvestigator();
	
	@JsonProperty("study_country_code")
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	@JsonProperty("site_number")
	public String getSiteNumber() {
		return siteNumber; 
	}
	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}
	
	@JsonProperty("study_name")
	public String getStudyName() {
		return studyName;
	}
	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}
	
	@JsonProperty("institution")
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	
	@JsonProperty("principal_investigator")
	public PrincipalInvestigator getPrincipalInvestigator() {
		return principalInvestigator;
	}
	public void setPrincipalInvestigator(PrincipalInvestigator principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}
	
	@JsonProperty("comments")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	@JsonProperty("street_1")
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	@JsonProperty("state_or_region")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@JsonProperty("city")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	@JsonProperty("postal_code")
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	
	
}
