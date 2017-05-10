package com.msis.core.service;

import java.util.List;

import com.msis.common.service.ServiceException;
import com.msis.core.model.Record;
import com.msis.core.model.Test;
import com.msis.core.model.Treatment;

public interface RecordService {
	void save(Record record);
	void saveTest(List<Test> tests);
	void saveTreatment(List<Treatment> treatments);
	
	void delete(Record record);
	void deleteTest(List<Test> tests);
	void deleteTreatment(List<Treatment> treatments);
	
	Record findOne(String id);
	List<Record> findByPid(String pid);
	
	Record createRecord(Record record) throws ServiceException;
	Record updateRecord(Record record) throws ServiceException;
	void deleteRecord(String recordId) throws ServiceException;
	void deleteRecordByPatient(String pid) throws ServiceException;
	
	// TEST
	List<Test> findTestByRecordId(String recordId);
	Test createTest(Test test) throws ServiceException;
	Test updateTest(Test test) throws ServiceException;
	void deleteTest(String testId) throws ServiceException;
	void deleteTestByRecord(String recordId) throws ServiceException;
	void deleteTestByPatient(String patientId) throws ServiceException;
	
	// TREATMENT
	List<Treatment> findTreatmentByIdn(String idn);
	List<Treatment> findTreatmentByRecordId(String recordId);
	
	// Relationship
	void deleteAllByRecord(String recordId);
	void deleteAllByPatient(String patientId);
	
}
