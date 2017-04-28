package com.msis.core.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "${es.index}", type = "user")
public class User {
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Long createAt;
	private Long modifyAt;
	private String status = "A";
	
	// login info
	private String token;
	private Long loginAt;
	
	public User() {}
	
	public User(String email) {
		this.email = email;
	}
	
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public User(String firstName, String lastName, String email, String password) {
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.email = email;
		this.password = password;
	}
	
	public User(User user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.email;
		this.password = user.getPassword();
		this.createAt = user.getCreateAt();
		this.modifyAt = user.getModifyAt();
		this.loginAt = user.getLoginAt();
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public Long getModifyAt() {
		return modifyAt;
	}

	public void setModifyAt(Long modifyAt) {
		this.modifyAt = modifyAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getLoginAt() {
		return loginAt;
	}

	public void setLoginAt(Long loginAt) {
		this.loginAt = loginAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
