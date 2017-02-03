package com.gobalta.mule.mw.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StudySites implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6733430842814015042L;
	private Collection<StudySite> study_sites = new ArrayList<StudySite>();
	private ZonedDateTime processed = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
	private DateTimeFormatter format = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
	
	private static class StudySitePredicate implements Predicate {
		private Object expected;
		private String propertyName;
		
		public StudySitePredicate(String propertyName, Object expected) {
			super();
			this.propertyName = propertyName;
			this.expected = expected;
		}
		
		public boolean evaluate(Object object) {
			try {
				return expected.equals(PropertyUtils.getProperty(object, propertyName));
				
			} catch(Exception e) {
				return false;
			}
			
		}
	}
	
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
	
	public void setProcessedTime(String time) {
		
		processed = ZonedDateTime.parse(time);
	}
	
	public void put(StudySite studySite) {
		
		study_sites.add(studySite);
	}
	
	public StudySite get(String propertyName, String propertyValue) {
		//returns the first element (no guarantee if sorted) of filtered collection that has propertyName = propertyValue
		//note: use this method only if propertyValue is expected to be unique
		StudySite retval = null;
		try {
		retval = (StudySite) CollectionUtils.select(study_sites, new StudySitePredicate(propertyName, propertyValue)).iterator().next();
		} catch(NoSuchElementException nse) {
			retval = null;
		}
		return retval;
	}
	
	public void delete(StudySite studySite) {
		study_sites.remove(studySite);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<StudySite> getAll(String propertyName, String propertyValue) {
		Collection<StudySite> filtered = (Collection<StudySite>) CollectionUtils.select(study_sites, new StudySitePredicate(propertyName, propertyValue));
		
		if(filtered.isEmpty())
			return null;
		
		return filtered;
	}
}
