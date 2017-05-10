package com.msis.rest.controller.patient;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import com.msis.core.model.Test;
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
			log.info("Create Record " + JsonHelper.toString(record));
			record = recordService.createRecord(record);
			response.setResult(record);
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
	public Response update(Record record) {
		ServiceResponse<Record> response = new ServiceResponse<Record>();
		try {
			log.info("Update Record " + JsonHelper.toString(record));
			record = recordService.updateRecord(record);
			response.setResult(record);
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
	public Response delete(@PathParam("id") String id) {
		ServiceResponse<Record> response = new ServiceResponse<Record>();
		try {
			log.info("Delete record by id " + id);
			recordService.deleteRecord(id);
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
	@Path("/delete/patient/{pid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteByPatient(@PathParam("pid") String pid) {
		ServiceResponse<Record> response = new ServiceResponse<Record>();
		try {
			log.info("Delete record by patient " + pid);
			recordService.deleteRecordByPatient(pid);
		} catch (ServiceException e) {
			log.warn("-> Delete Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Delete OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@GET
	@Path("/get/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") String id) {
		ServiceResponse<Record> response = new ServiceResponse<Record>();
		try {
			log.info("Get by id " + id);
			Record record = recordService.findOne(id);
			if (record == null) {
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found record by " + id);
			}
			response.setResult(record);
		} catch (ServiceException e) {
			log.warn("-> Get Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Get OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@GET
	@Path("/get/patient/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByPatient(@PathParam("id") String id) {
		ServiceResponse<List<Record>> response = new ServiceResponse<List<Record>>();
		try {
			log.info("Get by patient " + id);
			List<Record> records = recordService.findByPid(id);
			if (records.size() == 0) {
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found record by patient" + id);
			}
			response.setResult(records);
		} catch (ServiceException e) {
			log.warn("-> Get Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Get OK");
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
