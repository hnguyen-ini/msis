package com.msis.core.service;

import java.util.List;

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
	List<Record> findByIdn(String idn);
	
	List<Test> findTestByIdn(String idn);
	List<Test> findTestByRecordId(String recordId);
	
	List<Treatment> findTreatmentByIdn(String idn);
	List<Treatment> findTreatmentByRecordId(String recordId);
	
}
