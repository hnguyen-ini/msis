package com.msis.rest.controller.admin;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceResponse;
import com.msis.common.service.ServiceStatus;
import com.msis.core.model.Patient;
import com.msis.core.model.User;
import com.msis.core.service.UserService;

@Path("/admin")
public class AdminController implements InitializingBean{
	
	static Logger log = LoggerFactory.getLogger(AdminController.class);
	private static UserService userService;
	
	@GET
	@Path("/gets")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		ServiceResponse<List<User>> response = new ServiceResponse<List<User>>();
		try {
			List<User> users = userService.findAll();
			if (users.size() == 0) {
				log.warn("-> Not found user");
				response.setStatus(new ServiceStatus(ServiceStatus.NOT_FOUND, "Not found user"));
				return Response.status(ServiceStatus.NOT_FOUND.getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
			}
			response.setResult(users);
		} catch (Exception e) {
			log.warn("-> Register Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(ServiceStatus.SERVICE_ERROR, e.getMessage()));
			return Response.status(ServiceStatus.SERVICE_ERROR.getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Get OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@DELETE
	@Path("/delete/{email}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("email") String email) {
		ServiceResponse<User> response = new ServiceResponse<User>();
		try {
			log.info("Delete user " + email);
			userService.deleteByEmail(email);
		} catch (ServiceException e) {
			log.warn("-> Delete Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> delete OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(userService, "userService not set");
	}
	
	public static void setUserService(UserService userService) {
		AdminController.userService = userService;
	}

}
