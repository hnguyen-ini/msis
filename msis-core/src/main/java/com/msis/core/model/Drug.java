package com.msis.core.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "${es.index}", type = "drug")
public class Drug {
	@Id
	private String id;
	private String name;
	private String description;
	private Integer inStock;
	private String creator;
	
	public Drug() {
		this.setId(UUID.randomUUID().toString());
		this.setInStock(0);
	}
	
	public Drug(String name, String description, String creator) {
		this.setId(UUID.randomUUID().toString());
		this.setInStock(0);
		this.setName(name);
		this.setDescription(description);
		this.setCreator(creator);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getInStock() {
		return inStock;
	}

	public void setInStock(Integer inStock) {
		this.inStock = inStock;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	
}
