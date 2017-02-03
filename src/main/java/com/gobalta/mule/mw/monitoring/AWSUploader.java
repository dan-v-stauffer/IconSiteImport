package com.gobalta.mule.mw.monitoring;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;


public class AWSUploader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AWSUploader.class);
	private String awsProfileName;
	private String awsPrimaryKey;
	private String awsWriteKey;
	private String awsBucket;
	private File awsFile;
	
	private AWSUploader() {
		
	}
	
	public void UploadObjectToS3(File file, String subKey) throws IOException
	{
		awsFile = file;	
		awsWriteKey = awsPrimaryKey + "/"+subKey+"/"+getDateString()+"_"+awsFile.getName();
		uploadObjectToS3();
	}

	public void UploadObjectToS3(File file) throws IOException
	{
		awsFile = file;	
		awsWriteKey = awsPrimaryKey+"/"+awsFile.getName();
		uploadObjectToS3();
	}
	
	private void uploadObjectToS3() throws IOException
	{
		try {
			AWSCredentials credentials = new ProfileCredentialsProvider(awsProfileName).getCredentials();
			AmazonS3 s3Client = new AmazonS3Client(credentials);
			s3Client.putObject(new PutObjectRequest(awsBucket, awsWriteKey, awsFile));
			LOGGER.info("Uploaded "+ awsFile.getName() +" to AWS S3 bucket" + getFullS3UploadPath());
			awsWriteKey = "";
		}
		catch(AmazonServiceException ase) {
			LOGGER.error("Errror uploading file to AWS S3 :"+ase.getMessage());
		}
		catch(AmazonClientException ace) {
			LOGGER.error("Errror uploading file to AWS S3 :"+ace.getMessage());
		}
	}
	
	private String getFullS3UploadPath()
	{
		return awsBucket+(awsWriteKey.length()>0?"/"+awsWriteKey:"");
	}
	
	public String getAwsProfileName() {
		return awsProfileName;
	}
	
	public void setAwsProfileName(String awsProfileName)
	{
		this.awsProfileName = awsProfileName;
	}

	public String getAwsBucket() {
		return awsBucket;
	}
	
	public void setAwsBucket(String awsBucket)
	{
		this.awsBucket = awsBucket;
	}
	
	public String getAwsKey() {
		return awsPrimaryKey;
	}
	
	public void setAwsKey(String awsKey) {
		this.awsPrimaryKey = awsKey;
	}
	
	private String getDateString() {
		return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());	
	}
}
