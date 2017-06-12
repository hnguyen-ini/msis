package com.msis.core.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "${es.index}", type = "patient")
public class Patient {
	@Id
	private String id;
	private String idn;
	private String name;
	private Integer age;
	private String sex;
	private String addr;
	private String phone;
	private String creator;
	private String description;
	
	private Long createAt;
	private Long modifiedAt;
	
	public Patient() {
		id = UUID.randomUUID().toString();
	}
	
	public Patient(String idn, String name, Integer age, String sex, String addr, String creator) {
		this.id = UUID.randomUUID().toString();
		this.idn = idn;
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.addr = addr;
		this.creator = creator;
	} 
	
	public Patient(Patient patient) {
		this.id = patient.getId();
		this.idn = patient.getIdn();
		this.name = patient.getName();
		this.age = patient.getAge();
		this.sex = patient.getSex();
		this.addr = patient.getAddr();
		this.creator = patient.getCreator();
		this.createAt = patient.getCreateAt();
		this.modifiedAt = patient.getModifiedAt();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdn() {
		return idn;
	}

	public void setIdn(String idn) {
		this.idn = idn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public Long getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
