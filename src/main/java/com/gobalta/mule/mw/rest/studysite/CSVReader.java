package com.gobalta.mule.mw.rest.studysite;

import java.io.*;
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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gobalta.mule.mw.exception.GoBaltoMWException;
import com.gobalta.mule.mw.model.StudySite;
import com.gobalta.mule.mw.model.StudySites;
import com.gobalta.mule.mw.model.StudySiteWrapper;
import com.gobalta.mule.mw.model.StudySitesWrapper;
import com.gobalta.mule.mw.monitoring.DataFiles;
import com.gobalta.mule.mw.monitoring.AWSUploader;
import com.gobalta.mule.mw.transformers.csv.MapsToCSVTransformer;


public class CSVReader {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVReader.class);
	private StudySiteRestService studySiteService;
	private MapsToCSVTransformer maptoCSVTransformer;
	private AWSUploader awsUploader;
	private String errorDirectory;
	private String processedFileDirectory;
	private String dataFileDirectory;
	private StudySites importedData;
	private Boolean writeFile = false;
	private String status = "selected";
	
	private Boolean useDynamicDelay;
	private int minDynamicDelay;
	private int maxDynamicDelay;
	private int staticPostDelay;
	
	public void processFile(Map<String, List<Map<String, String>>> maps , String fileName) throws Exception {
       LOGGER.info("Processing CSV file line by line , total rows:"+(maps!=null ? maps.size() :0));
       writeFile = false;
       
	   List<Map<String,String>> errors = new ArrayList<>();
	   Map<String, Integer> summary = new HashMap<String, Integer>();
	   summary.put("success", 0);
	   summary.put("duplicate", 0);
	   summary.put("error", 0);
	   
	   //for now, only process 'selected' sites
	   List<Map<String,String>> newSites = maps.get("selected");
	   
	   if(newSites != null) {
		   int delay = getDelay(newSites.size());
		   
	       for(Map<String,String> map : newSites){
			    LOGGER.info("Processing site :"+map);
			    StudySiteWrapper siteWrapper = new StudySiteWrapper();
			    StudySite ss = siteWrapper.getStudySite();
			    
			    //ICON requirement - only load sites where status == 'selected'
				if(!trim(map.get("STUDY_SITE_STATUS")).equalsIgnoreCase("selected")) {
					continue;
				}
				else
				{
					ss.setStatus("in_activation");
				}
				ss.setStudyName(trim(map.get("PROTOCOL")));
				ss.setCountryCode(trim(map.get("PI_COUNTRY")));
				ss.setSiteNumber(trim(map.get("SITE")));
				ss.setInstitution(trim(map.get("ORG_NAME")));
				ss.setCroStudyName(trim(map.get("STUDY_NUMBER")));
				ss.setSponsorStudyName(trim(map.get("PROTOCOL")));
				ss.setSiteId(trim(map.get("SITE_ID")));
				ss.setIrbEcType("local");
				ss.getPrincipalInvestigator().setFirstName(trim(map.get("PI_FNAME")));
				ss.getPrincipalInvestigator().setLastName(trim(map.get("PI_LNAME")));
				ss.getPrincipalInvestigator().setEmail(trim(map.get("PI_EMAIL")));
				
				if(isChangedRecord(ss.getSiteId(), ss)) {	
					try{
						if(!writeFile) writeFile = true;
						studySiteService.postData(siteWrapper,map,fileName);
						summary.put("success", summary.get("success") + 1);
						
						//delay of 6-10 secs to prevent overloading API
						Thread.sleep(delay);
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
		   }
	       
	       if(writeFile) writeDataFile();
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
	    	String summaryFileName = processedFileDirectory+"/"+"summary.csv";
			Files.write(Paths.get(summaryFileName), data.getBytes(), StandardOpenOption.APPEND);
			this.awsUploader.UploadObjectToS3(new File(summaryFileName), "summary");
		}catch(IOException io){
			LOGGER.error("Not able to write to Summary file",io);
			throw new GoBaltoMWException("Not able to write to Summary file",io);
		}catch(Exception te){
			LOGGER.error("Not able to write to Summary file",te);
			throw new GoBaltoMWException("Not able to write to Summary file",te);
		}
	}
	
	private int getDelay(int recordCount) {
		
		if(useDynamicDelay)
			return staticPostDelay;
		
		//introduce a delay between 6-10 seconds to prevent overloading of API
		if(recordCount <= 0)
			return 0;
		
		int delay = (8 * 3600)/recordCount;
		
		return Math.max(Math.min(maxDynamicDelay, delay), minDynamicDelay)*1000;
	}
	
	private Boolean isChangedRecord(String siteId, StudySite value) {
		try {
			if(importedData == null) {
				try{
					readDataFile();
				}
				catch(FileNotFoundException nf)
				{
					importedData.put(value);
					writeDataFile();
					return true;
				}
			}
			
			StudySite  ss = importedData.get("siteId", siteId);
			if(ss != null) {
				Boolean isChanged = (!ss.equals(value));
				if(isChanged) {
					importedData.delete(value);
					importedData.put(value);
				}
				return isChanged;
			}
			else
			{
				importedData.put(value);
				return true;
			}
			
		}
		catch(NullPointerException np) {
			LOGGER.error("SiteId cannot be null", np );
			return true;
		}
		catch(ClassCastException cc) {
			LOGGER.error("SiteId must be a string.", cc);
			return true;
		}
		catch(Exception e) {
			LOGGER.error("Error reading from data map.", e);
			return true;
		}
		
	}
	
	private void readDataFile() throws FileNotFoundException
	{
		try {
			importedData = new StudySites();
			
			File file = DataFiles.getLatestDataFile(dataFileDirectory);

			if(file != null)
			{
				//importedData = new ObjectMapper().readValue(file, new TypeReference<HashMap<String,StudySite[]>>(){});
				
				ObjectMapper objectMapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PUBLIC_ONLY);
				
				importedData = objectMapper.readValue(file, new TypeReference<StudySites>(){});
				
				
				//importedData = new ObjectMapper().readValue(file, new TypeReference<Collection<StudySite>>(){});
			}
		}
		catch(IOException io) {
			LOGGER.error("IO Error while trying to read to Data file",io);
		}
	}
	
	private void writeDataFile() {
		try {
			String fileName = "/"+"icon_"+status+"_studysites.json";
			ObjectMapper objectMapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PUBLIC_ONLY);
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			File file = new File(Paths.get(dataFileDirectory).toString()+fileName);
			
			StudySitesWrapper studySites = new StudySitesWrapper();
			StudySites ss = studySites.getStudySites();
			
			ss.setStudySites(importedData.getStudySites());
			
			objectMapper.writeValue(file, studySites.getStudySites());

			this.awsUploader.UploadObjectToS3(file, "new");
		}
		catch(JsonGenerationException jg) {
			
		}
		catch(JsonMappingException jm) {
			
		}
		catch(IOException io) {
			LOGGER.error("IO Error while writing to Data file",io);
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
			this.awsUploader.UploadObjectToS3(file, "error");
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
	
	public void setAwsUploader(AWSUploader awsUploader) {
		this.awsUploader = awsUploader;
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
	
	public String getDataFileDirectory() {
		return dataFileDirectory;
	}
	
	public void setDataFileDirectory(String dataFileDirectory) {
		this.dataFileDirectory = dataFileDirectory;
	}
	
	public Boolean getUseDynamicDelay() {
		return useDynamicDelay;
	}
	
	public void setUseDynamicDelay(String useDynamicDelay) {
		this.useDynamicDelay = Boolean.parseBoolean(useDynamicDelay);
	}
	
	public int getMaxDynamicDelay() {
		return maxDynamicDelay;
	}
	
	public void setMaxDynamicDelay(String maxDynamicDelay) {
		this.maxDynamicDelay = Integer.parseInt(maxDynamicDelay);
	}
	
	public int getMinDynamicDelay() {
		return minDynamicDelay;
	}
	
	public void setMinDynamicDelay(String minDynamicDelay) {
		this.minDynamicDelay = Integer.parseInt(minDynamicDelay);
	}
	
	public int getStaticPostDelay() {
		return staticPostDelay;
	}
	
	public void setStaticPostDelay(String staticPostDelay) {
		this.staticPostDelay = Integer.parseInt(staticPostDelay);
	}
}
