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
import com.msis.core.model.Patient;
import com.msis.core.model.Record;
import com.msis.core.service.RecordService;

@Path("/record")
public class RecordController implements InitializingBean{
	static Logger log = LoggerFactory.getLogger(RecordController.class);
	private static RecordService recordService;
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Record record) {
		ServiceResponse<Record> response = new ServiceResponse<Record>();
		try {
			log.info("Create Record " + JsonHelper.toString(response));
			record = recordService.createRecord(record);
		} catch (ServiceException e) {
			log.warn("-> Create Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Create OK");
		response.setStatus(ServiceStatus.OK);
		response.setResult(record);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Record record) {
		ServiceResponse<Record> response = new ServiceResponse<Record>();
		try {
			log.info("Update Record " + JsonHelper.toString(response));
			record = recordService.createRecord(record);
		} catch (ServiceException e) {
			log.warn("-> Update Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Update OK");
		response.setStatus(ServiceStatus.OK);
		response.setResult(record);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@DELETE
	@Path("/delete/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") String id) {
		ServiceResponse<Patient> response = new ServiceResponse<Patient>();
		try {
			log.info("Delete record by id " + id);
			recordService.deleteRecord(id);
		} catch (ServiceException e) {
			log.warn("-> Delete Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> delete OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	public static void setRecordService(RecordService recordService) {
		RecordController.recordService = recordService;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(recordService, "recordService can't be null");
	}

}
