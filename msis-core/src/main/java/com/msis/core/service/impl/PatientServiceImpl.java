package com.msis.core.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;
import com.msis.common.utils.ListUtils;
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
	public void deleteAll() {
		patientRepositoty.deleteAll();
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
		return ListUtils.okList(patientRepositoty.findByCreator(creator));
	}

	@Override
	public Patient create(Patient patient) throws ServiceException {
		try {
			if (!okPatient(patient)) {
				log.warn("Missing Idn or name");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing Idn or name");
			}
			if (findByIdn(patient.getIdn()) != null) {
				log.warn("Duplicated Idn " + patient.getIdn());
				throw new ServiceException(ServiceStatus.DUPLICATE_USER, "Duplicated Idn");
			}
			Long time = System.currentTimeMillis();
			patient.setCreateAt(time);
			patient.setModifiedAt(time);
			save(patient);
			return patient;
		} catch (Exception e) {
			log.warn("Running Time Error " + e.getMessage());
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}

	@Override
	public Patient update(Patient patient) throws ServiceException {
		try {
			if (!okPatient(patient)) {
				log.warn("Missing Idn or name");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing Idn or name");
			}
			Patient editPatient = findByIdn(patient.getIdn()); 
			if (editPatient == null) {
				log.warn("Not found patient idn " + patient.getIdn());
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found patient by Idn " + patient.getIdn());
			}
			editPatient = new Patient(patient);
			editPatient.setModifiedAt(System.currentTimeMillis());
			save(editPatient);
			return editPatient;
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	private boolean okPatient(Patient patient) {
		if (patient.getIdn() == null || patient.getIdn().isEmpty() || patient.getName() == null || patient.getName().isEmpty()) 
			return false;
		return true;
	}
}
