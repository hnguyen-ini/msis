package com.msis.core.service;

import java.io.InputStream;

import com.msis.common.service.ServiceException;
import com.msis.core.model.Content;

public interface ContentService {
	Content upload(InputStream is, String fileName, String pid, String recordId, String accessToken) throws ServiceException;
}
