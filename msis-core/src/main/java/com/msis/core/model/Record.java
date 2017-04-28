package com.msis.core.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "${es.index}", type = "record")
public class Record {
	@Id
	private String id;
	private String idn;
	private Long createAt;
	private String complain;
	private List<String> testIds;
	private String diagnose;
	private List<String> treatmentIds;
	private Long remindAt;
	
	public Record() {}

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

	public List<String> getTestIds() {
		return testIds;
	}

	public void setTestIds(List<String> testIds) {
		this.testIds = testIds;
	}

	public String getDiagnose() {
		return diagnose;
	}

	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}

	public List<String> getTreatmentIds() {
		return treatmentIds;
	}

	public void setTreatmentIds(List<String> treatmentIds) {
		this.treatmentIds = treatmentIds;
	}

	public Long getRemindAt() {
		return remindAt;
	}

	public void setRemindAt(Long remindAt) {
		this.remindAt = remindAt;
	}
	
}
