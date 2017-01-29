package com.gobalta.mule.mw.rest.studysite;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobalta.mule.mw.exception.GoBaltoMWException;
import com.gobalta.mule.mw.model.StudySiteWrapper;

public class StudySiteRestService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StudySiteRestService.class);
	private String serviceURL;
	private Client restClient;
	private String securityToken;

	
	public void postData(StudySiteWrapper studySite , Map<String,String> rawCSV , String fileName) throws Exception{
		LOGGER.info("Service URL for the rest call {}",serviceURL);
		WebTarget target = restClient.target(serviceURL).path("api/v1/study_sites");
		ObjectMapper objectMapper = new ObjectMapper();
		LOGGER.info(objectMapper.writeValueAsString(studySite));
		try{
			Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
					.header("x-gobalto-api-token-v2", securityToken)
					.post(Entity.entity(objectMapper.writeValueAsString(studySite), MediaType.APPLICATION_JSON_TYPE));
			if(response.getStatus()==200){
				LOGGER.info("Response successful : {}"+response.readEntity(String.class));
			}else {
				String errorMessage = response.readEntity(String.class);
				ErrorMessage errorObj = new ErrorMessage();
				if (errorMessage != null) {
					try {
						errorObj = objectMapper.readValue(errorMessage, ErrorMessage.class);
					} catch (Exception e) {
						LOGGER.error("Error while converting response to error object", e);
						// ignore this to let the processing go forward
					}
				} 
				LOGGER.error("Error while making the rest request , received httpStatus as {} and message as : {} and error is {}",response.getStatus(), errorMessage, errorObj);

				throw new GoBaltoMWException(errorObj.getGbCode(), "Error while making the rest request , received httpStatus as "+response.getStatus() + " with error code " + errorObj.getGbCode());
			}
		}catch(GoBaltoMWException e){
		   LOGGER.error("Just Log the error",e);	
		   throw e;
		}catch(Exception e){
			throw new GoBaltoMWException("Error while making the rest request",e);
		}
	}
	

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public void setRestClient(Client restClient) {
		this.restClient = restClient;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}


	
	
	
}
