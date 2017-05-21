package com.msis.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;

@Configuration
@PropertySource({"classpath:core-app.properties"})
public class CoreConfig {
	static Logger log = LoggerFactory.getLogger(CoreConfig.class);
	@Autowired
    protected Environment env;
	
	private enum env_enum {
		dev, test, prod;
	}
    
    public String esHost() throws ServiceException{
        try {
	    	String esHost;
	    	switch(env_enum.valueOf(env.getActiveProfiles()[0])) {
	            case dev:
	                esHost = env.getProperty("es.host.dev");
	                break;
	            case test:
	                esHost = env.getProperty("es.host.test");
	                break;
	            case prod:
	                esHost = env.getProperty("es.host.prod");
	                break;
	            default:
	                return null;
	        }
	    	log.info("Elasticsearch host name: " + esHost);
	    	return esHost;
        } catch (Exception e) {
        	throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: Host Failed, " + e.getMessage());
        }
    }

    public int esPort() throws ServiceException{
        try {
	    	int port = 9300;
	    	switch(env_enum.valueOf(env.getActiveProfiles()[0])) {
	            case dev:
	                port = Integer.parseInt(env.getProperty("es.port.dev"));
	                break;
	            case test:
	                port = Integer.parseInt(env.getProperty("es.port.test"));
	                break;
	            case prod:
	                port = Integer.parseInt(env.getProperty("es.port.prod"));
	                break;
	            default:
	                return 9300;
	        }
	    	log.info("Elasticsearch port: " + port);
	    	return port;
        } catch (Exception e) {
        	throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: Port Failed, " + e.getMessage());
        }
    }

    public String esCluster() throws ServiceException {
    	try {
	    	String cluster;
	        switch(env_enum.valueOf(env.getActiveProfiles()[0])) {
	            case dev:
	                cluster = env.getProperty("es.cluster.dev");
	                break;
	            case test:
	                cluster = env.getProperty("es.cluster.test");
	                break;
	            case prod:
	                cluster = env.getProperty("es.cluster.prod");
	                break;
	            default:
	                return null;
	        }
	        log.info("Elasticsearch cluster name: " + cluster);
	        return cluster;
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: Cluster Failed, " + e.getMessage());
    	}
    }
    
    public String privateKey() throws ServiceException {
    	try {
    		return env.getProperty("privateKey");
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: PrivateKey Failed, " + e.getMessage());
    	}
    }
    
    public String publicKey() throws ServiceException {
    	try {
    		return env.getProperty("publicKey");
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: PublicKey Failed, " + e.getMessage());
    	}
    }
    
    public String salt() throws ServiceException {
    	try {
    		return env.getProperty("salt");
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: Salt Failed, " + e.getMessage());
    	}
    }
    
    public String iv() throws ServiceException {
    	try {
    		return env.getProperty("iv");
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: IV Failed, " + e.getMessage());
    	}
    }
    
    public String awsRegion() throws ServiceException {
    	try {
    		return env.getProperty("aws.region");
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: AWSRegion Failed, " + e.getMessage());
    	}
    }
    
    public String awsAccessKey() throws ServiceException {
    	try {
    		return env.getProperty("aws.accessKey");
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: AWSAccessKey Failed, " + e.getMessage());
    	}
    }
    
    public String awsSecretKey() throws ServiceException {
    	try {
    		return env.getProperty("aws.secretKey");
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: AWSSecretKey Failed, " + e.getMessage());
    	}
    }
    
    public String awsRootBucket() throws ServiceException {
    	try {
    		return env.getProperty("aws.rootBucket");
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: AWSRootBucket Failed, " + e.getMessage());
    	}
    }
    
    public String awsBase() throws ServiceException {
    	try {
    		return env.getProperty("aws.base");
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, "Configuration: AWSBase Failed, " + e.getMessage());
    	}
    }
}
