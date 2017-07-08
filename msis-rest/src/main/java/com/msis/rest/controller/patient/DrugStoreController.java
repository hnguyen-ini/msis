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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.msis.common.parser.JsonHelper;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceResponse;
import com.msis.common.service.ServiceStatus;
import com.msis.core.model.Drug;
import com.msis.core.model.Patient;
import com.msis.core.model.Store;
import com.msis.core.service.StoreService;

@Path("/drugstore")
public class DrugStoreController implements InitializingBean {
	static Logger log = LoggerFactory.getLogger(DrugStoreController.class);
	private static StoreService storeService;
	
	@POST
	@Path("/save/drug")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveDrug(Drug drug, @QueryParam("accessToken") String accessToken) {
		ServiceResponse<Drug> response = new ServiceResponse<Drug>();
		try {
			log.info("Create Drug " + JsonHelper.toString(drug));
			drug = storeService.saveDrug(drug, accessToken);
		} catch (ServiceException e) {
			log.warn("-> Create Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Create Drug OK");
		response.setStatus(ServiceStatus.OK);
		response.setResult(drug);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@DELETE
	@Path("/delete/drug/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDrug(@PathParam("id") String id, @QueryParam("accessToken") String accessToken) {
		ServiceResponse<Drug> response = new ServiceResponse<Drug>();
		try {
			log.info("Delete Drug " + id);
			storeService.deleteDrug(id, accessToken);
		} catch (ServiceException e) {
			log.warn("-> Delete Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "DELETE").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Delete OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "DELETE").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@GET
	@Path("/get/drug/creator/{creator}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDrugByCreator(@PathParam("creator") String creator, @QueryParam("accessToken") String accessToken) {
		ServiceResponse<List<Drug>> response = new ServiceResponse<List<Drug>>();
		try {
			log.info("Get Drug by Creator " + creator);
			List<Drug> drugs = storeService.findDrugByCreator(creator, accessToken);
			response.setResult(drugs);
		} catch (ServiceException e) {
			log.warn("-> Get Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Get OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@GET
	@Path("/get/drug/creator/{creator}/name/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDrugByCreatorAndName(@PathParam("creator") String creator, @PathParam("name") String name, @QueryParam("accessToken") String accessToken) {
		ServiceResponse<List<Drug>> response = new ServiceResponse<List<Drug>>();
		try {
			log.info("Get Drug by Creator " + creator + " And Name " + name);
			List<Drug> drugs = storeService.searchDrugByCreatorAndNameLike(creator, name, accessToken);
			response.setResult(drugs);
		} catch (ServiceException e) {
			log.warn("-> Get Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Get OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@POST
	@Path("/save/store")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createStore(Store store, @QueryParam("accessToken") String accessToken) {
		ServiceResponse<Drug> response = new ServiceResponse<Drug>();
		try {
			log.info("Create Store " + JsonHelper.toString(store));
			Drug drug = storeService.createStore(store, accessToken);
			response.setResult(drug);
		} catch (ServiceException e) {
			log.warn("-> Create Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Create Store OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@PUT
	@Path("/update/store")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateStore(Store store, @QueryParam("accessToken") String accessToken) {
		ServiceResponse<Store> response = new ServiceResponse<Store>();
		try {
			log.info("Update Store " + JsonHelper.toString(store));
			store = storeService.updateStore(store, accessToken);
			response.setResult(store);
		} catch (ServiceException e) {
			log.warn("-> Update Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "PUT").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Update Store OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "PUT").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@DELETE
	@Path("/delete/store/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteStore(@PathParam("id") String id, @QueryParam("accessToken") String accessToken) {
		ServiceResponse response = new ServiceResponse();
		try {
			log.info("Delete Store " + id);
			storeService.deleteStore(id, accessToken);
		} catch (ServiceException e) {
			log.warn("-> Delete Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "DELETE").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> DELETE Store OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "DELETE").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@GET
	@Path("/get/store/drug/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStoreByDrug(@PathParam("id") String id, @QueryParam("accessToken") String accessToken) {
		ServiceResponse<List<Store>> response = new ServiceResponse<List<Store>>();
		try {
			log.info("Get Store by Drug " + id);
			response.setResult(storeService.getStoreByDrug(id, accessToken));
		} catch (ServiceException e) {
			log.warn("-> Get Store by Drug, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Get Store OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	
	
	public static void setStoreService(StoreService storeService) {
		DrugStoreController.storeService = storeService;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

}
