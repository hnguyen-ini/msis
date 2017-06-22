package com.msis.core.cache;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;
import com.msis.core.config.CoreConfig;
import com.msis.core.model.Session;

@Service(value="cacheService")
public class CacheService implements Cacheable {
	static Logger logger = LoggerFactory.getLogger(CacheService.class);
	private static java.util.HashMap<Object, Object> cacheHashMap = new java.util.HashMap<Object, Object>();
	
	@Autowired
	private CoreConfig config;
	
	static {
        try {
            Thread threadCleanerUpper = new Thread(new Runnable() {
              int milliSecondSleepTime = 5000;
              public void run() {
                try {
                	while (true) {
                		logger.info("Scanning For Expired Objects...");
                		java.util.Set keySet = cacheHashMap.keySet();
                		java.util.Iterator keys = keySet.iterator();
                		while(keys.hasNext()) {
                			Object key = keys.next();
                			Session value = (Session)cacheHashMap.get(key);
                			if (value != null && isExpired(value.getDateofExpiration())) {
                				cacheHashMap.remove(key);
                				logger.info("Found and Removed an Expired Object:  " + value.getToken() + ":" + value.getDateofExpiration());
                				break;
                			}
                		}
                		Thread.sleep(this.milliSecondSleepTime);
                	}
                } catch (Exception e) {
                	logger.warn(e.getMessage());
                }
                return;
              } 
            });
            
            threadCleanerUpper.setPriority(Thread.MIN_PRIORITY);
            threadCleanerUpper.start();
        } catch(Exception e) {
              logger.info("CacheManager.Static Block: " + e);
        }
	}
	
	private static boolean isExpired(Date dateofExpiration) {
		if (dateofExpiration != null) {
			if (dateofExpiration.before(new java.util.Date())) {
				return true;
			} else {
				return false;
			}
        } else // This means it lives forever!
          return false;
	}

	@Override
	public void setCache(Session session) {
		logger.info("Set to cache: " + session.getToken());
		cacheHashMap.put(session.getToken(), session);
	}

	@Override
	public Session getCache(String token) {
		Session session = (Session) cacheHashMap.get(token);
		if (session == null)
			return null;
		if (isExpired(session.getDateofExpiration())) {
			cacheHashMap.remove(session);
			return null;
		}
		return session;
	}

	@Override
	public void resetExpiration(Session session, int minuteToLive) {
		if (minuteToLive != 0) {
			Date dateofExpiration = new java.util.Date();
	        java.util.Calendar cal = java.util.Calendar.getInstance();
	        cal.setTime(dateofExpiration);
	        cal.add(cal.MINUTE, minuteToLive);
	        dateofExpiration = cal.getTime();
	        session.setDateofExpiration(dateofExpiration);
		} else
			session.setDateofExpiration(null);
	}
	
	@Override
	public Session checkAccessToken(String accessToken) throws ServiceException {
		try {
			if (accessToken == null || accessToken.isEmpty()) {
				logger.warn("Missing accessToken");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing accessToken");
			}
			Session session = getCache(accessToken);
			if (session == null) {
				throw new ServiceException(ServiceStatus.REQUEST_TIME_OUT, "Your token had been expired!");
			}
			resetExpiration(session, config.sessionExpired());
			return session;
		} catch(ServiceException e) {
			throw e;
		} catch (Exception e) {
			logger.warn("Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
}
