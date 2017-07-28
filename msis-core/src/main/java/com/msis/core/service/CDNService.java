package com.msis.core.service;

import java.io.File;
import java.io.InputStream;

import com.msis.common.service.ServiceException;

public interface CDNService {
	long saveContent(InputStream is, String uri) throws ServiceException;
	InputStream readContent(String uri) throws ServiceException;
	void deleteContent(String uri) throws ServiceException;
	void cleanDirectory(String directory) throws ServiceException;
}
