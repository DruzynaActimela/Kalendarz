package com.actimel.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Klasa reprezentuj¹ca wydzia³.
 * @author ActimelTeam
 *
 */
public class Department {

	/**
	 * Nazwa wydzia³u.
	 */
	@SerializedName("nazwa")
	private String name;
	
	/**
	 * Lista kierunków wydzia³u.
	 */
	@SerializedName("kierunki")
	private List<Division> divisions;
	
}
