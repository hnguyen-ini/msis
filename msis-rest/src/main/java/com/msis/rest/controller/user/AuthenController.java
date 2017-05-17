package com.msis.rest.controller.user;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.msis.common.parser.JsonHelper;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceResponse;
import com.msis.common.service.ServiceStatus;
import com.msis.core.cache.CacheService;
import com.msis.core.cache.Cacheable;
import com.msis.core.model.Session;
import com.msis.core.model.User;
import com.msis.core.service.UserService;

@Path("/authen")
public class AuthenController implements InitializingBean {
	static Logger log = LoggerFactory.getLogger(AuthenController.class);
	private static UserService userService;
	private static CacheService cacheService;
	
	@POST
	@Path("/reg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(User user) {
		ServiceResponse<User> response = new ServiceResponse<User>();
		try {
			log.info("Register " + JsonHelper.toString(user));
			user = userService.regUser(user);
		} catch (ServiceException e) {
			log.warn("-> Register Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Register OK");
		response.setStatus(ServiceStatus.OK);
		response.setResult(user);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@PUT
	@Path("/signin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signin(User user) {
		ServiceResponse<User> response = new ServiceResponse<User>();
		try {
			log.info("Sign-in " + JsonHelper.toString(user));
			user = userService.verify(user.getEmail(), user.getPassword());
		} catch (ServiceException e) {
			log.warn("-> Sign-in Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Sign-in OK");
		response.setStatus(ServiceStatus.OK);
		response.setResult(user);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	public static void setUserService(UserService userService) {
		AuthenController.userService = userService;
	}
	
	public static void setCacheService(CacheService cacheService) {
		AuthenController.cacheService = cacheService;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(userService, "userService can't be null");
		Assert.notNull(cacheService, "userService can't be null");
		
		String s = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		Session co = new Session("1234", s, 1);
		cacheService.setCache(co);
		Session obj = cacheService.getCache("1234");
		if (obj == null)
			System.out.println("CacheManagerTestProgram.Main:  FAILURE!  Object not Found.");
		else
		    System.out.println("CacheManagerTestProgram.Main:  SUCCESS! " + obj.getKey());
	}

}
