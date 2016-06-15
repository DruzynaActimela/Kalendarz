package com.actimel.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Klasa reprezentuj�ca wydzia�.
 * @author ActimelTeam
 *
 */
public class Department {

	/**
	 * Nazwa wydzia�u.
	 */
	@SerializedName("nazwa")
	private String name;
	
	/**
	 * Lista kierunk�w wydzia�u.
	 */
	@SerializedName("kierunki")
	private List<Division> divisions;
	
}
