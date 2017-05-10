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
import com.msis.core.service.RecordService;
import com.msis.core.service.UserService;

@Service(value="patientService")
public class PatientServiceImpl implements PatientService {
	
	static Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);
	
	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private RecordService recordService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void save(Patient patient) {
		patientRepository.save(patient);
	}

	@Override
	public void delete(Patient patient) {
		patientRepository.delete(patient);
	}

	@Override
	public void deleteById(String id) throws ServiceException {
		Patient patient = patientRepository.findOne(id);
		if (patient == null)
			throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found patient by id " + id);
		delete(patient);
		// TODO: delete record & other
	}

	@Override
	public void deleteByCreator(String creator) throws ServiceException {
		List<Patient> patients = patientRepository.findByCreator(creator);
		if (patients == null)
			throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found patient by creator " + creator);
		patientRepository.delete(patients);
		// TODO: delete record & other
	}
	
	@Override
	public void deleteAll() {
		patientRepository.deleteAll();
	}

	@Override
	public Patient findOne(String id) {
		return patientRepository.findOne(id);
	}
	
	@Override
	public Patient findByIdnAndCreator(String idn, String creator) {
		return patientRepository.findByIdnAndCreator(idn, creator);
	}

	@Override
	public List<Patient> findByIdn(String idn) {
		return ListUtils.okList(patientRepository.findByIdn(idn));
	}
	
	@Override
	public List<Patient> findByName(String name) {
		return ListUtils.okList(patientRepository.findByName(name));
	}
	
	@Override
	public List<Patient> findByPhone(String phone) {
		return ListUtils.okList(patientRepository.findByPhone(phone));
	} 

	@Override
	public List<Patient> findByCreator(String creator) {
		return ListUtils.okList(patientRepository.findByCreator(creator));
	}

	@Override
	public Patient create(Patient patient) throws ServiceException {
		try {
			if (!okPatient(patient)) {
				log.warn("Missing Idn or name");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing Idn or Name or Creator");
			}
			if (findByIdnAndCreator(patient.getIdn(), patient.getCreator()) != null) {
				log.warn("Duplicated Idn " + patient.getIdn());
				throw new ServiceException(ServiceStatus.DUPLICATE_USER, "Duplicated Idn");
			}
			String creator = patient.getCreator(); 
			if (creator != null && !creator.isEmpty()) {
				if (userService.findByEmail(creator) == null) {
					log.warn("Not found creator by " + creator);
					throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found creator by " + creator);
				}
			}
			Long time = System.currentTimeMillis();
			patient.setCreateAt(time);
			patient.setModifiedAt(time);
			save(patient);
			return patient;
		} catch (ServiceException e) {
			throw e;
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
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing Idn or name or creator");
			}
			if (patient.getId() == null || patient.getId().isEmpty()) {
				log.warn("Missing Id");
				throw new ServiceException(ServiceStatus.BAD_REQUEST, "Missing Id");
			}
			Patient editPatient = findOne(patient.getId()); 
			if (editPatient == null) {
				log.warn("Not found patient id " + patient.getId());
				throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found patient by Id " + patient.getId());
			}
			String creator = patient.getCreator(); 
			if (creator != null && !creator.isEmpty()) {
				if (userService.findByEmail(creator) == null) {
					log.warn("Not found creator by " + creator);
					throw new ServiceException(ServiceStatus.NOT_FOUND, "Not found creator by " + creator);
				}
			}
			editPatient.setAddr(patient.getAddr());
			editPatient.setAge(patient.getAge());
			editPatient.setCreator(patient.getCreator());
			editPatient.setIdn(patient.getIdn());
			editPatient.setName(patient.getName());
			editPatient.setSex(patient.getSex());
			editPatient.setPhone(patient.getPhone());
			editPatient.setModifiedAt(System.currentTimeMillis());
			save(editPatient);
			return editPatient;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(ServiceStatus.RUNNING_TIME_ERROR, e.getMessage());
		}
	}
	
	private boolean okPatient(Patient patient) {
		if (patient.getIdn() == null || patient.getIdn().isEmpty() 
				|| patient.getName() == null || patient.getName().isEmpty()
				|| patient.getCreator() == null || patient.getCreator().isEmpty()) 
			return false;
		return true;
	}
}
