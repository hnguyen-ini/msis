package com.msis.core.service.impl;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msis.core.model.Record;
import com.msis.core.model.Test;
import com.msis.core.model.Treatment;
import com.msis.core.repository.RecordRepository;
import com.msis.core.repository.TestRepository;
import com.msis.core.repository.TreatmentRepository;
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
	private TestRepository testRepo;
	@Autowired
	private TreatmentRepository treatmentRepo;
	
	@Autowired
	private PatientService patientService;

	@Autowired
	private CDNService cdnService;
	
	@Override
	public void save(Record record) {
		recordRepo.save(record);
	}
	
	@Override
	public void saveTest(List<Test> tests) {
		testRepo.save(tests);		
	}
	
	@Override
	public void saveTreatment(List<Treatment> treatments) {
		treatmentRepo.save(treatments);		
	}
	
	@Override
	public void delete(Record record) {
		recordRepo.delete(record);
	}
	
	@Override
	public void deleteTest(List<Test> tests) {
		testRepo.delete(tests);
	}
	
	@Override
	public void deleteTreatment(List<Treatment> treatments) {
		treatmentRepo.delete(treatments);
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
	public Record createRecord(Record record) throws ServiceException {
		try {
			String pid = record.getPid(); 
			if (pid == null || pid.isEmpty()) {
				log.warn("Create Record::  Missing pid");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing pid");
			}
			if (patientService.findOne(pid) == null) {
				log.warn("Create Record:: Not found patient by " + pid);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found patient by " + pid);
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
	
	/**
	 * TEST ZONE
	 */
	@Override
	public List<Test> findTestByRecordId(String recordId) {
		return ListUtils.okList(testRepo.findByRecordId(recordId));
	}
	
	@Override
	public Test createTest(Test test) throws ServiceException {
		try {
			String pid = test.getPid(); 
			if (pid == null || pid.isEmpty()) {
				log.warn("Create Test:: Missing Pid");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing Pid");
			}
			if (patientService.findOne(pid) == null) {
				log.warn("Create Test:: Not found patient by " + pid);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found patient by " + pid);
			}
			String recordId = test.getRecordId();
			if (recordId != null && !recordId.isEmpty() && findOne(recordId) == null) {
				log.warn("Create Test:: Not found record by " + recordId);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found record by " + recordId);
			}
			String content = test.getContent(); 
			if (content != null && !content.isEmpty()) {
				test.setUri(UriUtils.buildTestUri(pid, recordId, test.getItem() + test.getContentType()));
				cdnService.saveContent(new ByteArrayInputStream(content.getBytes()), test.getUri());
			}
			test.setCreateAt(System.currentTimeMillis());
			testRepo.save(test);
			test.setContent(null);
			return test;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Create Test:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	@Override
	public Test updateTest(Test test)  throws ServiceException {
		try {
			if (test.getId() == null || test.getId().isEmpty()) {
				log.warn("Update Test:: Missing Id" );
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing Id");
			}
			String pid = test.getPid();
			if (patientService.findByIdn(pid) == null) {
				log.warn("Update Test:: Not found patient by " + pid);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found patient by " + pid);
			}
			String recordId = test.getRecordId();
			if (recordId != null && !recordId.isEmpty() && findOne(recordId) == null) {
				log.warn("Create Test:: Not found record by " + recordId);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found record by " + recordId);
			}
			Test edit = testRepo.findOne(test.getId());
			if (edit == null) {
				log.warn("Update Test:: Not found test by " + test.getId());
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found test by " + test.getId());
			}
			String content = test.getContent(); 
			if (content != null && !content.isEmpty()) {
				edit.setUri(UriUtils.buildTestUri(pid, recordId, test.getItem() + test.getContentType()));
				cdnService.saveContent(new ByteArrayInputStream(content.getBytes()), edit.getUri());
			}
			edit.setDescription(test.getDescription());
			edit.setRecordId(recordId);
			edit.setPid(pid);
			edit.setItem(test.getItem());
			testRepo.save(edit);
			test.setContent(null);
			return edit;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Create Test:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	@Override
	public void deleteTest(String testId) throws ServiceException {
		try {
			if (testId == null || testId.isEmpty()) {
				log.warn("Delete Test:: Missing Id" );
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing Id");
			}
			Test del = testRepo.findOne(testId);
			if (del == null) {
				log.warn("Delete Test:: Not found test by " + testId);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found test by " + testId);
			}
			cdnService.deleteContent(del.getUri());
			testRepo.delete(del);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Delete Test:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	@Override
	public void deleteTestByRecord(String recordId) throws ServiceException {
		try {
			if (recordId == null || recordId.isEmpty()) {
				log.warn("Delete Test:: Missing recordId" );
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing recordId");
			}
			List<Test> tests = testRepo.findByRecordId(recordId);
			if (tests == null) {
				log.warn("Delete Test By Record:: Not found test by record " + recordId);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found test by record " + recordId);
			}
			for (Test del : tests) {
				cdnService.deleteContent(del.getUri());
				testRepo.delete(del);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Delete Test:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	@Override
	public void deleteTestByPatient(String patientId) throws ServiceException {
		try {
			if (patientId == null || patientId.isEmpty()) {
				log.warn("Delete Test:: Missing patientId" );
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing patientId");
			}
			int count = 0;
			List<Record> records = findByPid(patientId);
			for (Record record : records) {
				List<Test> tests = testRepo.findByRecordId(record.getId());
				if (tests != null && tests.size() > 0) {
					for (Test del : tests) {
						cdnService.deleteContent(del.getUri());
						testRepo.delete(del);
						count++;
					}
				}
			}
			if (count == 0) {
				log.warn("Delete Test By Patient:: Not found test by patient " + patientId);
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found test by record " + patientId);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			log.warn("Delete Test:: Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	
	/**
	 * TREATMENT ZONE
	 */
	@Override
	public List<Treatment> findTreatmentByIdn(String idn) {
		return ListUtils.okList(treatmentRepo.findByIdn(idn));
	}
	
	@Override
	public List<Treatment> findTreatmentByRecordId(String recordId) {
		return ListUtils.okList(treatmentRepo.findByRecordId(recordId));
	}
	
	// Relatioship
	@Override
	public void deleteAllByRecord(String recordId) {
		testRepo.delete(findTestByRecordId(recordId));
		treatmentRepo.delete(findTreatmentByRecordId(recordId));
	}
	
	@Override
	public void deleteAllByPatient(String patientId) {
		List<Record> records = findByPid(patientId);
		for (Record record : records) {
			deleteAllByRecord(record.getId());
		}
	}
	
}
