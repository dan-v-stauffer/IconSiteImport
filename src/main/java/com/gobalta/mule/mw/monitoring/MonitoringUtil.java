package com.gobalta.mule.mw.monitoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MonitoringUtil {
	
	private static Configuration config = new Configuration();
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringUtil.class);

    public String process() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Result result = new Result();
		result.setConfiguration(config);
		result.setSummary(getSummary());

		LOGGER.info(objectMapper.writeValueAsString(result));
		return objectMapper.writeValueAsString(result);
    }
    
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<Summary> getSummary() {
    	List<Summary> result = new ArrayList<Summary>();
    	
		List list = readSummaryFile();
    	for (Object item: list) {
    		
			List<String> ri = (List<String>) item;

    		if (ri.size() < 5) continue;
    		
    		Summary s = new Summary();
    		s.setFile(ri.get(0));
    		s.setDate(ri.get(1));
    		s.setSuccess(ri.get(2));
    		s.setDuplicate(ri.get(3));
    		s.setError(ri.get(4));
    		
    		result.add(s);
    	}
    	
    	Collections.reverse(result);
    	return result;
    }

	@SuppressWarnings("rawtypes")
	private List readSummaryFile() {
		String SEPARATOR = ",";
		try {
			Path path = Paths.get(config.processedFileDirectory()
					+ File.separator + "summary.csv");
			Reader source = Files.newBufferedReader(path,
					Charset.forName("UTF-8"));
			try (BufferedReader reader = new BufferedReader(source)) {
				List result = reader.lines().skip(1)
						.map(line -> Arrays.asList(line.split(SEPARATOR)))
						.collect(Collectors.toList());
				return result;
			} catch (IOException e) {
				LOGGER.error("Not able to read summary file", e);
			}
		} catch (Exception e) {
			LOGGER.error("Not able to read summary file", e);
		}
		return null;
	} 
    
	public void setOutputFileDirectory(String val) {
		System.out.println("OutputFileDirectory: " + val);
		config.setOutputFileDirectory(val);
	}
	
	public void setSftpHost(String val) {
		System.out.println("SftpHost: " + val);
		config.setSftpHost(val);
	}
	
	public void setSftpUser(String val) {
		System.out.println("SftpUser: " + val);
		config.setSftpUser(val);
	}
	
	public void setSftpPassword(String val) {
		System.out.println("SftpPassword: " + val);
		config.setSftpPassword(val);
	}
	
	public void setSftpPath(String val) {
		System.out.println("SftpPath: " + val);
		config.setSftpPath(val);
	}
	
	public void setSftpPort(String val) {
		System.out.println("SftpPort: " + val);
		config.setSftpPort(val);
	}
	
	public void setSftpTimeout(String val) {
		System.out.println("SftpTimeout: " + val);
		config.setSftpTimeout(val);
	}
	
	public void setSftpPollingFrequency(String val) {
		System.out.println("SftpPollingFrequency: " + val);
		config.setSftpPollingFrequency(val);
	}
	
	public void setErrorDirectory(String val) {
		System.out.println("ErrorDirectory: " + val);
		config.setErrorDirectory(val);
	}
	
	public void setRestUrl(String val) {
		System.out.println("RestUrl: " + val);
		config.setRestUrl(val);
	}
	
	public void setRestSecurityToken(String val) {
		System.out.println("RestSecurityToken: " + val);
		config.setRestSecurityToken(val);
	}
	
	public void setProcessedFileDirectory(String val) {
		System.out.println("ProcessedFileDirector: " + val);
		config.setProcessedFileDirectory(val);
	}

	public void setMonitoringPort(String val) {
		System.out.println("MonitoringPort: " + val);
		config.setMonitoringPort(val);
	}
	
	public void setMonitoringHost(String val) {
		System.out.println("MonitoringHost: " + val);
		config.setMonitoringHost(val);
	}
	
	public void setCommitHash(String commitHash) {
		System.out.println("commitHash: " + commitHash);
		config.setCommitHash(commitHash);
	}
	
	public void setCommitDate(String commitDate) {
		System.out.println("commitHash: " + commitDate);
		config.setCommitDate(commitDate);
	}
    
}