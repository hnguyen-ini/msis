package com.msis.rest.controller.cdn;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.multipart.MultipartFile;

import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceResponse;
import com.msis.common.service.ServiceStatus;
import com.msis.core.model.Content;
import com.msis.core.service.CDNService;
import com.msis.core.service.ContentService;
import com.sun.jersey.multipart.FormDataParam;

@Path("/cdn")
public class ContentController implements InitializingBean {
	private static Logger log = LoggerFactory.getLogger(ContentController.class);
	private static ContentService contentService;
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response upload(@FormDataParam("file") InputStream file, @QueryParam("fileName") String fileName, @QueryParam("pid") String pid, @QueryParam("recordId") String recordId, @QueryParam("accessToken") String accessToken) {
		ServiceResponse<Content> response = new ServiceResponse<Content>();
		try {
			log.info("Uploading file " + fileName + "..");
			Content content = contentService.upload(file, fileName, pid, recordId, accessToken);
			response.setResult(content);
		} catch (ServiceException e) {
			log.warn("-> Upload Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Upload OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@PUT
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@QueryParam("fileName") String fileName, @QueryParam("pid") String pid, @QueryParam("recordId") String recordId, @QueryParam("accessToken") String accessToken) {
		ServiceResponse response = new ServiceResponse();
		try {
			log.info("Deleting files " + fileName + "..");
			contentService.delete(fileName, pid, recordId, accessToken);
		} catch (ServiceException e) {
			log.warn("-> Delete Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "PUT").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Delete OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "PUT").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	@GET
	@Path("/gets")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByRecord(@QueryParam("recordId") String recordId, @QueryParam("accessToken") String accessToken) {
		ServiceResponse response = new ServiceResponse();
		try {
			log.info("Gets by record " + recordId);
			response.setResult(contentService.getByRecord(recordId, accessToken));
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
	@Path("/download")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response download(@QueryParam("fileName") String fileName, @QueryParam("pid") String pid, @QueryParam("recordId") String recordId, @QueryParam("accessToken") String accessToken) {
		ServiceResponse<String> response = new ServiceResponse<String>();
		try {
			log.info("Downloading file " + fileName + "..");
			response.setResult(contentService.download(fileName, pid, recordId, accessToken));
		} catch (ServiceException e) {
			log.warn("-> Download Failed, " + e.getMessage());
			response.setStatus(new ServiceStatus(e.getStatus().getCode(), e.getMessage()));
			return Response.status(e.getStatus().getCode()).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
		}
		log.info("-> Download OK");
		response.setStatus(ServiceStatus.OK);
		return Response.status(Response.Status.OK).entity(response).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "POST").header("Access-Control-Allow-Headers", "Content-Type").build();
	}
	
	public static void setContentService(ContentService contentService) {
		ContentController.contentService = contentService;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

}
