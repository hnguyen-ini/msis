package com.msis.core.service;

import java.io.InputStream;
import java.util.List;

import com.msis.common.service.ServiceException;
import com.msis.core.model.Content;

public interface ContentService {
	Content upload(InputStream is, String fileName, String pid, String recordId, String accessToken) throws ServiceException;
	void delete(String fileName, String pid, String recordId, String accessToken) throws ServiceException;
	String download(String fileName, String pid, String recordId, String accessToken) throws ServiceException;
	List<Content> getByRecord(String recordId, String accessToken) throws ServiceException;
	
}
