package com.msis.core.model;

public class Mail {
	private String address;
	private String name;
	private String token;
	private String uri;
	private String subject;
	private String velocityModel; // registerEmail.vm"
	
	public Mail() {}
	
	public Mail(String address, String name, String token, String uri, String subject, String velocityModel) {
		this.setAddress(address);
		this.setName(name);
		this.setToken(token);
		this.setUri(uri);
		this.setSubject(subject);
		this.setVelocityModel(velocityModel);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getVelocityModel() {
		return velocityModel;
	}

	public void setVelocityModel(String velocityModel) {
		this.velocityModel = velocityModel;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
