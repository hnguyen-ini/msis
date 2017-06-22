package com.msis.core.cache;

import com.msis.common.service.ServiceException;
import com.msis.core.model.Session;

public interface Cacheable {
	void setCache(Session session);
	Session getCache(String token);
	void resetExpiration(Session session, int minuteToLive);
	
	Session checkAccessToken(String accessToken) throws ServiceException;
}
