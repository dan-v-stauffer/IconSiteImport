package com.gobalta.mule.mw.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudySites implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6733430842814015042L;
	private Collection<StudySite> study_sites;
	private ZonedDateTime processed = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
	private DateTimeFormatter format = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
	
	@JsonProperty("study_sites")
	public Collection<StudySite> getStudySites() {
		return study_sites;
	}
	
	public void setStudySites(Collection<StudySite> studySites) {
		this.study_sites = studySites;
	}
	
	@JsonProperty("processed")
	public String getProcessedTime() {
		return processed.format(format);
	}
}
