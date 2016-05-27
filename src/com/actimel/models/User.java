package com.actimel.models;

public class User {
	private int id;
	private String name;
	private String pass;
	private String email;
	
	private int permission_level = 0;

	public User(int id, String name, String pass, String email, int permission_level) {
		super();
		this.id = id;
		this.name = name;
		this.pass = pass;
		this.email = email;
		this.permission_level = permission_level;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isPasswordCorrect(String pass) {
		if(pass == null) return false;
		return pass.trim().equals(this.pass.trim());
	}

	public int getPermissionLevel() {
		return permission_level;
	}
	
	
	
}
