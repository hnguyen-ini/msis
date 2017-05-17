package com.msis.core.cache;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.msis.core.model.Session;

@Service(value="cacheService")
public class CacheService implements Cacheable {
	static Logger logger = LoggerFactory.getLogger(CacheService.class);
	private static java.util.HashMap<Object, Object> cacheHashMap = new java.util.HashMap<Object, Object>();
	
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
                				logger.info("Running. Found an Expired Object in the Cache  " + value.getToken() +":"+ value.getKey() + ":" + value.getDateofExpiration());
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
	
	
}
