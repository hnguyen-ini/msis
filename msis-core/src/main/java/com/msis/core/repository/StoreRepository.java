package com.msis.core.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.msis.core.model.Store;

public interface StoreRepository extends ElasticsearchRepository<Store, String> {
	List<Store> findByDrugId(String drugId);
	List<Store> findByDrugIdAndCreateAt(String drugId, Long createAt);
	List<Store> findByDrugIdAndCreateAtLessThan(String drugId, Long createAt);
	List<Store> findByDrugIdAndCreateAtGreaterThan(String drugId, Long createAt);
}
