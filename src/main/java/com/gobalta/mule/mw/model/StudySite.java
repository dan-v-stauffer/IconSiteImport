package com.gobalta.mule.mw.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudySite implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8542595340809326730L;
	private String siteNumber;
	private String studyName;
	private String institution;
	private String status;
	private String irb_ec_type;
	private String street;
	private String state;
	private String city;
	private String postalCode;
	private String countryCode;
	private String comments;
	private String croStudyName;
	private String sponsorStudyName;
	private String siteId;
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
	
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonProperty("irb_ec_type")
	public String getIrbEcType() {
		return irb_ec_type;
	}
	
	public void setIrbEcType(String irb_ec_type) {
		this.irb_ec_type = irb_ec_type;
	}
	
	@JsonProperty("cro_study_name") 
	public String getCroStudyName() {
		return croStudyName;
	}
	
	public void setCroStudyName(String croStudyName) {
		this.croStudyName = croStudyName;
	}
	
	@JsonProperty("sponsor_study_name")
	public String getSponsorStudyName() {
		return sponsorStudyName;
	}
	
	public void setSponsorStudyName(String sponsorStudyName) {
		this.sponsorStudyName = sponsorStudyName;
	}
	
	@JsonProperty("site_id")
	public String getSiteId() {
		return siteId;
	}
	
	public void setSiteId(String siteId) {
		this.siteId = siteId;
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
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof StudySite)) {
			return false;
		}
		StudySite s = (StudySite)o;
		
		return Objects.equals(this.siteNumber, s.getSiteNumber()) &&
				Objects.equals(this.studyName, s.getStudyName()) &&
				Objects.equals(this.institution, s.getInstitution()) &&
				Objects.equals(this.street, s.getStreet()) &&
				Objects.equals(this.state, s.getState()) &&
				Objects.equals(this.postalCode, s.getPostalCode()) &&
				Objects.equals(this.countryCode, s.getCountryCode()) &&
				Objects.equals(this.comments, s.getComments()) &&
				this.principalInvestigator.equals(s.getPrincipalInvestigator());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(siteNumber, studyName, institution, street, state, city, 
				postalCode, countryCode, comments, principalInvestigator);
	}
	
}
