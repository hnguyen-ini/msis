package com.msis.core.model;

import java.util.Date;

public class Session {
	private String token;
	private String key;
	private Date dateofExpiration;
	
	public Session(String token, String key, int minuteToLive) {
		this.key = key;
		this.token = token;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getDateofExpiration() {
		return dateofExpiration;
	}

	public void setDateofExpiration(Date dateofExpiration) {
		this.dateofExpiration = dateofExpiration;
	}
	
}
