package com.gobalta.mule.mw.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class StudySiteWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2527838933932203441L;
	private StudySite studySite = new StudySite();

    @JsonProperty("study_site")
	public StudySite getStudySite() {
		return studySite;
	}

	public void setStudySite(StudySite studySite) {
		this.studySite = studySite;
	}

}
