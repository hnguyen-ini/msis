package com.msis.core.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.msis.core.model.Drug;

public interface DrugRepository  extends ElasticsearchRepository<Drug, String>{
	Drug findByNameAndCreator(String name, String creator);
	List<Drug> findByCreator(String creator);
 	
}
