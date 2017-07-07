package com.msis.core.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "${es.index}", type = "store")
public class Store {
	@Id
	private String id;
	private String drugId;
	private Integer number = 0;
	private Integer price = 0;
	private Long createAt;
	
	public Store() {
		this.id = UUID.randomUUID().toString();
	}
	
	public Store(String drugId, Integer number) {
		this.id = UUID.randomUUID().toString();
		this.drugId = drugId;
		this.number = number;
		this.createAt = System.currentTimeMillis();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDrugId() {
		return drugId;
	}

	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
}
