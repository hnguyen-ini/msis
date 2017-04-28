package com.msis.core.service.impl;

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
import com.msis.core.service.RecordService;
import com.msis.common.utils.ListUtils;

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
	public List<Record> findByIdn(String idn) {
		return ListUtils.okList(recordRepo.findByIdn(idn));
	}
	
	/**
	 * TEST ZONE
	 */
	@Override
	public List<Test> findTestByIdn(String idn) {
		return ListUtils.okList(testRepo.findByIdn(idn));
	}
	
	@Override
	public List<Test> findTestByRecordId(String recordId) {
		return ListUtils.okList(testRepo.findByRecordId(recordId));
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
	
	
	
}
