package com.msis.core.cache;

import java.util.Date;

import com.msis.core.model.Session;

public interface Cacheable {
	void setCache(Session session);
	Session getCache(String token);
	//boolean isExpired(Date dateofExpiration);
	void resetExpiration(Session session, int minuteToLive);
}
