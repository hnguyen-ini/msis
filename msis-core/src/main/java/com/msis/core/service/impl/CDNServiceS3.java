package com.msis.core.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;
import com.msis.core.config.CoreConfig;
import com.msis.core.service.CDNService;

@Service(value="cdnService")
public class CDNServiceS3 implements CDNService{
	static Logger log = LoggerFactory.getLogger(CDNServiceS3.class);
	private String rootBucketName;
    private String cdnBaseUri;
    private AmazonS3 s3client;
    
    private CoreConfig coreConfig;
	@Autowired
	public void setCoreConfig(CoreConfig coreConfig) {
		this.coreConfig = coreConfig;
	}
	
	@PostConstruct
	public void init() throws ServiceException {
		try {
			this.rootBucketName = coreConfig.awsRootBucket();
	    	this.cdnBaseUri = coreConfig.awsBase();
	    	
	    	BasicAWSCredentials creds = new BasicAWSCredentials(coreConfig.awsAccessKey(), coreConfig.awsSecretKey()); 
	    	s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withRegion(Regions.valueOf(coreConfig.awsRegion())).build();
	    	
	        if (!cdnBaseUri.trim().endsWith("/")) {
	            cdnBaseUri = cdnBaseUri.trim()+"/";
	        }
	        log.info("S3Client int successfully: " + cdnBaseUri + "/" + rootBucketName);
		} catch (Exception e) {
			log.warn("S3Client int failed: " + cdnBaseUri + "/" + rootBucketName);
			throw new ServiceException(ServiceStatus.SERVER_CONNECTION, "AWS Connection Failed");
		}
	}
	
	@Override
	public long saveContent(InputStream is, String uri) throws ServiceException {
		ObjectMetadata metadata = new ObjectMetadata();
        try {
            byte[] contentBytes = IOUtils.toByteArray(is);
            long contentLength = Long.valueOf(contentBytes.length);
            metadata.setContentLength(contentLength);
            metadata.setContentType("application/json");
            uri = fixUri(uri);
            
            log.info("Save content at " + uri + "/" + contentLength + "bytes");
            
            is = new ByteArrayInputStream(contentBytes);
        	s3client.putObject(rootBucketName, uri, is, metadata);
        	return contentLength;
        } catch (Exception e) {
        	log.warn("Save content failed, " + e.getMessage());
            throw new ServiceException(ServiceStatus.NO_CONTENT, "Saving Content Failed, " + e.getMessage());
        } finally {
        	if (is != null) {
        		try {
					is.close();
				} catch (IOException e) {}
        	}
        }
	}

	@Override
	public InputStream readContent(String uri) throws ServiceException {
		InputStream is = null;
    	S3Object object = null;
    	try {
    		if (uri == null || uri.isEmpty())
				return null;
            uri = fixUri(uri);
        	object = s3client.getObject(new GetObjectRequest(rootBucketName, uri));
        	is = (InputStream)object.getObjectContent();
            log.info("Read content at " + uri);
        	return is;
            
        } catch (Exception e) {
        	log.warn("Read content failed, " + e.getMessage());
            throw new ServiceException(ServiceStatus.NO_CONTENT, "Reading Content Failed, " + e.getMessage());
        }
	}
	
	@Override
	public void deleteContent(String uri) throws ServiceException {
		try {
			if (uri == null || uri.isEmpty())
				return;
			uri = fixUri(uri);
			s3client.deleteObject(rootBucketName, uri);
		} catch (Exception e) {
			log.warn("Delete content failed, " + e.getMessage());
			throw new ServiceException(ServiceStatus.NO_CONTENT, "Deleting Content Failed, " + e.getMessage());
		}
	}

	private String fixUri(String uri) {
        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }
        return uri;
    }
	
}
