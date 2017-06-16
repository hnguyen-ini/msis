package com.msis.core.service;

import java.util.List;

import com.msis.common.service.ServiceException;
import com.msis.core.model.Patient;

public interface PatientService {
	void save(Patient patient);
	void delete(Patient patient);
	void deleteById(String id) throws ServiceException;
	void deleteByCreator(String creator) throws ServiceException;
	void deleteAll();
	
	Patient findOne(String id);
	Patient findByIdnAndCreator(String idn, String creator);
	Patient findByIdn(String idn);
	List<Patient> findByName(String name);
	List<Patient> findByNameLike(String name);
	List<Patient> findByPhone(String phone);
	List<Patient> findByCreator(String creator, String accessToken) throws ServiceException;
	List<Patient> findByNameOrIDn(String search, String accessToken) throws ServiceException;
	
	Patient create(Patient patient, String accessToken) throws ServiceException;
	Patient update(Patient patient, String accessToken)  throws ServiceException;
	
	
	
}
