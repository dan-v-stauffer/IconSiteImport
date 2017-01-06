package com.gobalta.mule.mw.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StudySitesWrapper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1974966447137137854L;

	private StudySites studySites = new StudySites();
	
	public StudySites getStudySites() {
		return studySites;
	}

	public void setStudySites(StudySites studySites) {
		this.studySites = studySites;
	}
}
