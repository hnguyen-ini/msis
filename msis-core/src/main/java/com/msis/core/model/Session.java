package com.msis.core.model;

import java.util.Date;

public class Session {
	private String token;
	private String userToken;
	private String aesKey;
	private Date dateofExpiration;
	
	public Session(String token, String userToken, String aesKey, int minuteToLive) {
		this.token = token;
		this.userToken = userToken;
		this.aesKey = aesKey;
		if (minuteToLive != 0) {
			this.dateofExpiration = new java.util.Date();
	        java.util.Calendar cal = java.util.Calendar.getInstance();
	        cal.setTime(this.dateofExpiration);
	        cal.add(cal.MINUTE, minuteToLive);
	        this.dateofExpiration = cal.getTime();
		}
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAESKey() {
		return aesKey;
	}

	public void setAESKey(String aesKey) {
		this.aesKey = aesKey;
	}

	public Date getDateofExpiration() {
		return dateofExpiration;
	}

	public void setDateofExpiration(Date dateofExpiration) {
		this.dateofExpiration = dateofExpiration;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	
}
