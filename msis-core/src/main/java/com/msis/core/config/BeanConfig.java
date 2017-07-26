package com.msis.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.msis.core.service.CDNService;
import com.msis.core.service.impl.CDNServiceFile;

@Configuration
public class BeanConfig {
	static Logger log = LoggerFactory.getLogger(BeanConfig.class);
	
	@Bean
	public CDNService cdnService() {
		log.info("init CDNService Bean");
		return new CDNServiceFile();
	}
	
}
