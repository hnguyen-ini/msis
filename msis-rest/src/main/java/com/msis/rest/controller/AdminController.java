package com.msis.rest.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.msis.core.model.User;
import com.msis.core.service.UserService;

@Path("/admin")
public class AdminController implements InitializingBean{
	
	static Logger log = LoggerFactory.getLogger(AdminController.class);
	private static UserService userService;
	
	@GET
	@Path("/gets")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUsers() {
		User user= new User("Elasticsearch", "Rambabu", "Elasticsearch@gmail.com", "password");
        User testUser = userService.save(user);
		
		log.info(new Gson().toJson(testUser));
		return testUser;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(userService, "userService not set");
	}
	
	public static void setUserService(UserService userService) {
		AdminController.userService = userService;
	}

}
