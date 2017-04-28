package com.msis.core.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.msis.core.model.Treatment;

public interface TreatmentRepository extends ElasticsearchRepository<Treatment, String> {
	List<Treatment> findByIdn(String idn);
	List<Treatment> findByRecordId(String recordId);
}
