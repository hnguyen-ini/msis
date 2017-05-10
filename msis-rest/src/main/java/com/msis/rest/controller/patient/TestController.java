package com.msis.rest.controller.patient;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.msis.common.parser.JsonHelper;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceResponse;
import com.msis.common.service.ServiceStatus;
import com.msis.core.model.Test;
import com.msis.core.service.RecordService;

@Path("/test")
public class TestController implements InitializingBean{
	static Logger log = LoggerFactory.getLogger(TestController.class);
	private static RecordService recordService;
	
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTest(Test test) {
		ServiceResponse<Test> response = new ServiceResponse<Test>();
		try {
			log.info("Create Test " + JsonHelper.toString(test));
			test = recordService.createTest(test);
			response.setResult(test);
		} catch (ServiceException e) {
			log.warn("-> Create Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Create OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateTest(Test test) {
		ServiceResponse<Test> response = new ServiceResponse<Test>();
		try {
			log.info("Update Test " + JsonHelper.toString(test));
			test = recordService.updateTest(test);
			response.setResult(test);
		} catch (ServiceException e) {
			log.warn("-> Update Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Update OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@DELETE
	@Path("/delete/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTest(@PathParam("id") String id) {
		ServiceResponse<Test> response = new ServiceResponse<Test>();
		try {
			log.info("Delete Test " + id);
			recordService.deleteTest(id);
		} catch (ServiceException e) {
			log.warn("-> Delete Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Delete OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@DELETE
	@Path("/delete/record/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTestByRecord(@PathParam("id") String id) {
		ServiceResponse<Test> response = new ServiceResponse<Test>();
		try {
			log.info("Delete Test By Record " + id);
			recordService.deleteTestByRecord(id);
		} catch (ServiceException e) {
			log.warn("-> Delete Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Delete OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@DELETE
	@Path("/delete/patient/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTestByPatient(@PathParam("id") String id) {
		ServiceResponse<Test> response = new ServiceResponse<Test>();
		try {
			log.info("Delete Test By Patient " + id);
			recordService.deleteTestByPatient(id);
		} catch (ServiceException e) {
			log.warn("-> Delete Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Delete OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}

	public static void setRecordService(RecordService recordService) {
		TestController.recordService = recordService;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(recordService, "recordService can't be null");
	}

}
