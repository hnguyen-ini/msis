package com.msis.core.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "${es.index}", type = "record")
public class Record {
	@Id
	private String id;
	private String pid;
	private Long createAt;
	private String complain;
	private String test;
	private String diagnose;
	private Long remindAt;
	private String description;
	
	public Record() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreatedAt(Long createAt) {
		this.createAt = createAt;
	}

	public String getComplain() {
		return complain;
	}

	public void setComplain(String complain) {
		this.complain = complain;
	}

	public String getDiagnose() {
		return diagnose;
	}

	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}

	public Long getRemindAt() {
		return remindAt;
	}

	public void setRemindAt(Long remindAt) {
		this.remindAt = remindAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	
}
