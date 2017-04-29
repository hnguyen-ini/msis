package com.msis.rest.controller.user;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceResponse;
import com.msis.common.service.ServiceStatus;
import com.msis.core.model.User;
import com.msis.core.service.UserService;

@Path("/authen")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenController implements InitializingBean {
	static Logger log = LoggerFactory.getLogger(AuthenController.class);
	private static UserService userService;
	
	@POST
	@Path("/reg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(User user) {
		log.info("Register " + new Gson().toJson(user));
		ServiceResponse<User> response = new ServiceResponse<User>();
		try {
			userService.regUser(user);
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
	
	@GET
	@Path("/signin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User signin(User user) {
		return user;
	}
	
	public static void setUserService(UserService userService) {
		AuthenController.userService = userService;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
				
	}

}
