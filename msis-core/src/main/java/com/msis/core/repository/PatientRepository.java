package com.msis.core.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.msis.core.model.Patient;

public interface PatientRepository extends ElasticsearchRepository<Patient, String> {
	Patient findByIdn(String idn);
	Patient findByName(String name);
	
	List<Patient> findByCreator(String creator);
	
}
