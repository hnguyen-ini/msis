package com.msis.core.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.msis.core.model.Test;

public interface TestRepository extends ElasticsearchRepository<Test, String> {
	List<Test> findByIdn(String idn);
	List<Test> findByRecordId(String recordId);
}
