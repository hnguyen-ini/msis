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
import com.msis.core.service.PatientService;

@Path("/patient")
public class PatientController implements InitializingBean {
	static Logger log = LoggerFactory.getLogger(PatientController.class);
	private static PatientService patientService;
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Patient patient) {
		ServiceResponse<Patient> response = new ServiceResponse<Patient>();
		try {
			log.info("Create Patient " + JsonHelper.toString(patient));
			patient = patientService.create(patient);
		} catch (ServiceException e) {
			log.warn("-> Create Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Create OK");
		response.setStatus(ServiceStatus.OK);
		response.setResult(patient);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@PUT
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Patient patient) {
		ServiceResponse<Patient> response = new ServiceResponse<Patient>();
		try {
			log.info("Update Patient " + JsonHelper.toString(patient));
			patient = patientService.update(patient);
		} catch (ServiceException e) {
			log.warn("-> Update Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Update OK");
		response.setStatus(ServiceStatus.OK);
		response.setResult(patient);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@DELETE
	@Path("/delete/{idn}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("idn") String idn) {
		ServiceResponse<Patient> response = new ServiceResponse<Patient>();
		try {
			patientService.deleteByIdn(idn);
		} catch (ServiceException e) {
			log.warn("-> Delete Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> delete OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@DELETE
	@Path("/delete/creator/{creator}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteByCreator(@PathParam("creator") String creatorId) {
		ServiceResponse<Patient> response = new ServiceResponse<Patient>();
		try {
			patientService.deleteByCreator(creatorId);
		} catch (ServiceException e) {
			log.warn("-> Delete Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> delete OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@GET
	@Path("/get/{idn}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("idn") String idn) {
		ServiceResponse<Patient> response = new ServiceResponse<Patient>();
		try {
			Patient patient = patientService.findByIdn(idn);
			if (patient == null) {
				log.info("Not found patient by " + idn);
				response.setStatus(new ServiceStatus(ServiceStatus.NOT_FOUND, "Not found patient by " + idn));
				return Response.status(Response.Status.NOT_FOUND).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
			}
			response.setResult(patient);
		} catch (Exception e) {
			log.warn("-> Get Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(ServiceStatus.SERVICE_ERROR, e.getMessage()));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> get OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@GET
	@Path("/get/name/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByName(@PathParam("name") String name) {
		ServiceResponse<List<Patient>> response = new ServiceResponse<List<Patient>>();
		try {
			List<Patient> patients = patientService.findByName(name);
			if (patients.size() == 0) {
				log.info("Not found patient by name " + name);
				response.setStatus(new ServiceStatus(ServiceStatus.NOT_FOUND, "Not found patient by name " + name));
				return Response.status(Response.Status.NOT_FOUND).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
			}
			response.setResult(patients);
		} catch (Exception e) {
			log.warn("-> Get Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(ServiceStatus.SERVICE_ERROR, e.getMessage()));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> get OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@GET
	@Path("/get/creator/{creator}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByCreator(@PathParam("creator") String creator) {
		ServiceResponse<List<Patient>> response = new ServiceResponse<List<Patient>>();
		try {
			List<Patient> patients = patientService.findByCreator(creator);
			if (patients.size() == 0) {
				log.info("Not found patient by creator " + creator);
				response.setStatus(new ServiceStatus(ServiceStatus.NOT_FOUND, "Not found patient by creator " + creator));
				return Response.status(Response.Status.NOT_FOUND).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
			}
			response.setResult(patients);
		} catch (Exception e) {
			log.warn("-> Get Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(ServiceStatus.SERVICE_ERROR, e.getMessage()));
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> get OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	public void setPatientService(PatientService patientService) {
		PatientController.patientService = patientService;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(patientService, "patientService can't be null");
	}

}
