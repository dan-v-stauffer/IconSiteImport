package com.gobalta.mule.mw.rest.studysite;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gobalta.mule.mw.exception.GoBaltoMWException;
import com.gobalta.mule.mw.model.StudySite;
import com.gobalta.mule.mw.model.StudySiteWrapper;
import com.gobalta.mule.mw.transformers.csv.MapsToCSVTransformer;

public class CSVReader {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVReader.class);
	private StudySiteRestService studySiteService;
	private MapsToCSVTransformer maptoCSVTransformer;
	private String errorDirectory;
	private String processedFileDirectory;

	
	public void processFile(List<Map<String, String>> maps , String fileName) throws Exception {
       LOGGER.info("Processing CSV file line by line , total rows:"+(maps!=null ? maps.size() :0));
	   List<Map<String,String>> errors = new ArrayList<>();
	   Map<String, Integer> summary = new HashMap<String, Integer>();
	   summary.put("success", 0);
	   summary.put("duplicate", 0);
	   summary.put("error", 0);
       for(Map<String,String> map : maps){
		    LOGGER.info("Each Line :"+maps);
		    StudySiteWrapper siteWrapper = new StudySiteWrapper();
		    StudySite ss = siteWrapper.getStudySite();
			ss.setStudyName(trim(map.get("STUDY_CODE_ALIAS")));
			ss.setCountryCode(trim(map.get("COUNTRY_CODE")));
			ss.setSiteNumber(trim(map.get("STUDY_SITE_ID")));
			ss.setInstitution(trim(map.get("SITE_NAME")));
			ss.setCity(trim(map.get("POSTAL_ADDRESS_CITY_NAME")));
			ss.setState(trim(map.get("POSTAL_ADDRESS_STATE_NAME")));
			ss.setPostalCode(trim(map.get("POSTAL_ADDRESS_POSTAL_CODE")));
			ss.setStreet(trim(map.get("POSTAL_ADDRESS_LINES")));
			ss.getPrincipalInvestigator().setFirstName(trim(map.get("FORENAME")));
			ss.getPrincipalInvestigator().setLastName(trim(map.get("SURNAME")));
			ss.getPrincipalInvestigator().setEmail(trim(map.get("EMAIL_ADDRESS")));
			try{
				studySiteService.postData(siteWrapper,map,fileName);
				summary.put("success", summary.get("success") + 1);
			}catch(GoBaltoMWException e){
				if ("gb-1022".equals(e.getGbCode())) {
					summary.put("duplicate", summary.get("duplicate") + 1);
				} else {
					summary.put("error", summary.get("error") + 1);
				}
				map.put("ERROR", e.getMessage());
				errors.add(map);
			}
	   }
       if(errors.size()>0){
    	   writeToErrorFile(errors,fileName);
       }
       writeToSummaryFile(summary, fileName);
	}
    
	private void writeToSummaryFile(Map<String,Integer> summary, String fileName) throws Exception{
		try{
	    	String date = new SimpleDateFormat("yyyy MM dd hh:mm:ss").format(new Date());	
	    	String data = fileName + "," +
	    			date + "," +
	    			summary.get("success") + "," +
	    			summary.get("duplicate") + "," +
	    			summary.get("error") + "\n";
	    	
			Files.write(Paths.get(processedFileDirectory+"/"+"summary.csv"), data.getBytes(), StandardOpenOption.APPEND);
		}catch(IOException io){
			LOGGER.error("Not able to write to Summary file",io);
			throw new GoBaltoMWException("Not able to write to Summary file",io);
		}catch(Exception te){
			LOGGER.error("Not able to write to Summary file",te);
			throw new GoBaltoMWException("Not able to write to Summary file",te);
		}
	}
	
	private void writeToErrorFile(List<Map<String,String>> csv,String fileName) throws Exception{
		try{
			String rawCSV = (String) maptoCSVTransformer.transform(csv);
			File file = new File(errorDirectory+"/"+fileName);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(rawCSV);
			bw.close();
		}catch(IOException io){
			LOGGER.error("Not able to write to Error file",io);
			throw new GoBaltoMWException("Not able to write to Error file",io);
		}catch(Exception te){
			LOGGER.error("Not able to write to Error file",te);
			throw new GoBaltoMWException("Not able to write to Error file",te);
		}
	}
	
	private String trim(String value){
		return value != null ? value.trim(): null;
	}

	public void setStudySiteService(StudySiteRestService studySiteService) {
		this.studySiteService = studySiteService;
	}
	
	public void setMaptoCSVTransformer(MapsToCSVTransformer maptoCSVTransformer) {
		this.maptoCSVTransformer = maptoCSVTransformer;
	}
	
	public void setErrorDirectory(String errorDirectory) {
		this.errorDirectory = errorDirectory;
	}

	public String getProcessedFileDirectory() {
		return processedFileDirectory;
	}

	public void setProcessedFileDirectory(String processedFileDirectory) {
		this.processedFileDirectory = processedFileDirectory;
	}
	
}
