package com.msis.core.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.msis.core.model.Content;

public interface ContentRepository extends ElasticsearchRepository<Content, String>{
	List<Content> findByRecordId(String recordId);
	List<Content> findByRecordIdAndName(String recordId, String name);
}
