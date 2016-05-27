package com.actimel.models;

public class Session {
	private User user;
	private String key;	
	
	public Session(User user, String key) {
		this.user = user;
		this.key = key;
		
	}

	public User getUser() {
		return user;
	}

	public String getKey() {
		return key;
	}
	
	
}
