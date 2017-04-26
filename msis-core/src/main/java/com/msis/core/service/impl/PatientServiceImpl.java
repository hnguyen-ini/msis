package com.msis.core.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;
import com.msis.core.model.Patient;
import com.msis.core.repository.PatientRepository;
import com.msis.core.service.PatientService;

@Service(value="patientService")
public class PatientServiceImpl implements PatientService {
	
	static Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);
	
	@Autowired
	private PatientRepository patientRepositoty;
	
	@Override
	public void save(Patient patient) {
		patientRepositoty.save(patient);
	}

	@Override
	public void delete(Patient patient) {
		patientRepositoty.delete(patient);
	}

	@Override
	public void deleteByIdn(String idn) throws ServiceException {
		Patient patient = patientRepositoty.findByIdn(idn);
		if (patient == null)
			throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found patient by idn " + idn);
		delete(patient);
	}

	@Override
	public void deleteByCreator(String creator) throws ServiceException {
		List<Patient> patients = patientRepositoty.findByCreator(creator);
		if (patients == null)
			throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found patient by creator " + creator);
		patientRepositoty.delete(patients);
	}

	@Override
	public Patient findByIdn(String idn) {
		return patientRepositoty.findByIdn(idn);
	}

	@Override
	public Patient findByName(String name) {
		return patientRepositoty.findByName(name);
	}

	@Override
	public List<Patient> findByCreator(String creator) {
		return patientRepositoty.findByCreator(creator);
	}
	
}
