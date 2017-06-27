package com.msis.core.service;

import java.util.List;

import com.msis.common.service.ServiceException;
import com.msis.core.model.Record;

public interface RecordService {
	void save(Record record);
	void delete(Record record);
	
	Record findOne(String id);
	List<Record> findByPid(String pid, String accessToken) throws ServiceException;
	
	Record saveRecord(Record record, String accessToken) throws ServiceException;
	void deleteRecord(String recordId, String accessToken) throws ServiceException;
	
}
