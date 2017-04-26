package com.msis.core.service;

import java.util.List;

import com.msis.common.service.ServiceException;
import com.msis.core.model.Patient;

public interface PatientService {
	void save(Patient patient);
	void delete(Patient patient);
	void deleteByIdn(String idn) throws ServiceException;
	void deleteByCreator(String creator) throws ServiceException;
	
	Patient findByIdn(String idn);
	Patient findByName(String name);
	List<Patient> findByCreator(String creator);
}
