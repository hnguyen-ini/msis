package com.msis.core.service.impl;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msis.core.cache.CacheService;
import com.msis.core.model.Drug;
import com.msis.core.model.Record;
import com.msis.core.model.Store;
import com.msis.core.model.Treatment;
import com.msis.core.repository.DrugRepository;
import com.msis.core.repository.RecordRepository;
import com.msis.core.repository.StoreRepository;
import com.msis.core.service.CDNService;
import com.msis.core.service.PatientService;
import com.msis.core.service.RecordService;
import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;
import com.msis.common.utils.ListUtils;
import com.msis.common.utils.UriUtils;

/**
 *  * @author hien.nguyen
 */

@Service(value="recordService")
public class RecordServiceImpl implements RecordService {
	static Logger log = LoggerFactory.getLogger(RecordServiceImpl.class);
	
	@Autowired
	private RecordRepository recordRepo;
	@Autowired
	private PatientService patientService;
	@Autowired
	private DrugRepository drugRepo;

	@Autowired
	private CacheService cacheService;
	
	@Override
	public void save(Record record) {
		recordRepo.save(record);
	}
	
	@Override
	public void delete(Record record) {
		recordRepo.delete(record);
	}
	
	@Override
	public Record findOne(String id) {
		return recordRepo.findOne(id);
	}
	
	@Override
	public List<Record> findByPid(String pid) {
		return ListUtils.okList(recordRepo.findByPid(pid));
	}
	
	@Override
	public Record createRecord(Record record, String accessToken) throws ServiceException {
		try {
			cacheService.checkAccessToken(accessToken);
			String pid = record.getPid(); 
			if (pid == null || pid.isEmpty()) {
				log.warn("Create Record::  Missing pid");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing pid");
			}
			if (patientService.findOne(pid) == null) {
				log.warn("Create Record:: Not found patient by " + pid);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found patient by " + pid);
			}
			List<Treatment> treatments = (List<Treatment>) new Gson().fromJson(record.getTreatment(), new TypeToken<List<Treatment>>(){}.getType());
			for (Treatment t : treatments) {
				Drug drug = drugRepo.findOne(t.getId());
				if (drug == null)
					continue;
				drug.setInStock(drug.getInStock() - t.getNumber());
				drugRepo.save(drug);
			}
			
			record.setCreatedAt(System.currentTimeMillis());
			save(record);
			return record;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Create Record:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	@Override
	public Record updateRecord(Record record) throws ServiceException {
		try {
			String id = record.getId(); 
			if (id == null || id.isEmpty()) {
				log.warn("Update Record::  Missing Id");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing Id");
			}
			String pid = record.getPid(); 
			if (pid == null || pid.isEmpty()) {
				log.warn("Update Record::  Missing pid");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing pid");
			}
			if (patientService.findOne(pid) == null) {
				log.warn("Update Record:: Not found patient by " + pid);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found patient by " + pid);
			}
			Record edit = findOne(id);
			if (edit == null) {
				log.warn("Update Record:: Not found record by " + id);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found record by " + id);
			}
			edit.setComplain(record.getComplain());
			edit.setDescription(record.getDescription());
			edit.setDiagnose(record.getDiagnose());
			edit.setPid(record.getPid());
			edit.setRemindAt(record.getRemindAt());
			save(edit);
			return edit;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Update Record:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	@Override
	public void deleteRecord(String recordId) throws ServiceException {
		try {
			if (recordId == null || recordId.isEmpty()) {
				log.warn("Delete Record::  Missing recordId");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing recordId");
			}
			Record del = findOne(recordId);
			if (del == null) {
				log.warn("Delete Record:: Not found record by " + recordId);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found record by " + recordId);
			}
			delete(del);
			// TODO delete test & treatment
			// deleteAllByRecord(recordId);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Delete Record:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	@Override
	public void deleteRecordByPatient(String pid) throws ServiceException {
		try {
			if (pid == null || pid.isEmpty()) {
				log.warn("Delete Record By Patient::  Missing pid");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing pid");
			}
			List<Record> dels = findByPid(pid);
			if (dels.size() == 0) {
				log.warn("Delete Record By Patient:: Not found record by patient " + pid);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found record by patient " + pid);
			}
			recordRepo.delete(dels);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Delete Record By Patient:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	

}
