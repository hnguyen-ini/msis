package com.msis.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.msis.core.service.CDNService;
import com.msis.core.service.UserService;
import com.msis.core.service.impl.CDNServiceS3;
import com.msis.core.service.impl.UserServiceImpl;

@Configuration
public class BeanConfig {
	static Logger log = LoggerFactory.getLogger(BeanConfig.class);
	
	@Bean
	public UserService userService() {
		log.info("Init UserService Bean");
		return new UserServiceImpl();
	}
	
	@Bean
	public CDNService cdnService() {
		log.info("init CDNService Bean");
		return new CDNServiceS3();
	}
}
