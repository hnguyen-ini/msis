package com.msis.core.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.msis.core.model.Record;

public interface RecordRepository extends ElasticsearchRepository<Record, String> {
	List<Record> findByIdn(String idn);
}
