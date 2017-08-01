package com.msis.core.service.impl;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import com.amazonaws.util.IOUtils;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;
import com.msis.common.utils.ListUtils;
import com.msis.common.utils.UriUtils;
import com.msis.core.cache.CacheService;
import com.msis.core.model.Content;
import com.msis.core.repository.ContentRepository;
import com.msis.core.service.CDNService;
import com.msis.core.service.ContentService;


@Service(value="contentService")
public class ContentServiceImpl implements ContentService {
	static Logger log = LoggerFactory.getLogger(ContentServiceImpl.class);
	
	@Autowired
	private CDNService cdnService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ContentRepository contentRepo;
	
	@Override
	public Content upload(InputStream is, String fileName, String pid, String recordId, String accessToken) throws ServiceException {
		try {
			if (pid == null || pid.isEmpty() || recordId == null || recordId.isEmpty() || fileName == null || fileName.isEmpty()) {
				log.warn("Missing pid or recordId or fileName");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing patientId or recordId or fileName");
			}
			cacheService.checkAccessToken(accessToken);
			
			String uri = UriUtils.buildTestUri(pid, recordId, fileName);
			Long size = cdnService.saveContent(is, uri);
			
			List<Content> contents = contentRepo.findByRecordIdAndName(recordId, fileName);
			if (contents != null)
				contentRepo.delete(contents);
			Content content = new Content();
			content.setContentUri(uri);
			content.setCreateAt(System.currentTimeMillis());
			content.setName(fileName);
			content.setPid(pid);
			content.setRecordId(recordId);
			content.setSize(size);
			contentRepo.save(content);
			return content;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}

	@Override
	public void delete(String fileName, String pid, String recordId, String accessToken) throws ServiceException {
		try {
			if (pid == null || pid.isEmpty() || recordId == null || recordId.isEmpty() || fileName == null || fileName.isEmpty()) {
				log.warn("Missing pid or recordId or fileName");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing patientId or recordId or fileName");
			}
			cacheService.checkAccessToken(accessToken);
			String uri = UriUtils.buildTestUri(pid, recordId, fileName);
			cdnService.deleteContent(uri);
			
			List<Content> contents = contentRepo.findByRecordIdAndName(recordId, fileName);
			if (contents != null)
				contentRepo.delete(contents);
			
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			log.warn("Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}

	@Override
	public List<Content> getByRecord(String recordId, String accessToken) throws ServiceException {
		try {
			if (recordId == null || recordId.isEmpty()) {
				log.warn("Missing recordId");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing recordId");
			}
			cacheService.checkAccessToken(accessToken);
			
			return ListUtils.okList(contentRepo.findByRecordId(recordId));
			
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			log.warn("Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}

	@Override
	public String download(String fileName, String pid, String recordId, String accessToken) throws ServiceException {
		try {
			if (pid == null || pid.isEmpty() || recordId == null || recordId.isEmpty() || fileName == null || fileName.isEmpty()) {
				log.warn("Missing pid or recordId or fileName");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing patientId or recordId or fileName");
			}
			cacheService.checkAccessToken(accessToken);
			String uri = UriUtils.buildTestUri(pid, recordId, fileName);
			InputStream is = cdnService.readContent(uri);
			return new String(Base64.encode(IOUtils.toByteArray(is)));
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}


}
