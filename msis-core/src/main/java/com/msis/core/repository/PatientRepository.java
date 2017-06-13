package com.msis.core.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.msis.core.model.Patient;

public interface PatientRepository extends ElasticsearchRepository<Patient, String> {
	Patient findByIdn(String idn);
	List<Patient> findByName(String name);
	List<Patient> findByPhone(String phone);
	
	List<Patient> findByCreator(String creator);
	Patient findByIdnAndCreator(String idn, String creator);
}
