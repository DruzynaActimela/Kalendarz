package com.actimel.models;

public class EventGroup {
	private int id;
	private String name;
	private String color;
	private int owner_id; // user
	private boolean is_public;
	
	public EventGroup(int id, String name, String color, int owner_id, boolean is_public) {
		super();
		this.id = id;
		this.name = name;
		this.color = color;
		this.owner_id = owner_id;
		this.is_public = is_public;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getOwnerId() {
		return owner_id;
	}

	public void setOwnerId(int owner_id) {
		this.owner_id = owner_id;
	}

	public boolean isPublic() {
		return is_public;
	}

	public void setPublic(boolean is_public) {
		this.is_public = is_public;
	}

	
}
