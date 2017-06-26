package com.msis.core.service;

import java.util.List;

import com.msis.common.service.ServiceException;
import com.msis.core.model.Record;

public interface RecordService {
	void save(Record record);
	void delete(Record record);
	
	Record findOne(String id);
	List<Record> findByPid(String pid);
	
	Record createRecord(Record record, String accessToken) throws ServiceException;
	
	
	Record updateRecord(Record record) throws ServiceException;
	void deleteRecord(String recordId) throws ServiceException;
	void deleteRecordByPatient(String pid) throws ServiceException;
	
	
}
