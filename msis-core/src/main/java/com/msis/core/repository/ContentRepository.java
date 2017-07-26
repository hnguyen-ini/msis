package com.msis.core.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.msis.core.model.Content;

public interface ContentRepository extends ElasticsearchRepository<Content, String>{
	
}
