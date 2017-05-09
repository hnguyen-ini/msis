package com.msis.core.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.msis.core.model.Drug;

public interface DrugRepository  extends ElasticsearchRepository<Drug, String>{

}
