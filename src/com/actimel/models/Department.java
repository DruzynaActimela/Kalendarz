package com.actimel.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Klasa reprezentująca wydział.
 * @author ActimelTeam
 *
 */
public class Department {

	/**
	 * Nazwa wydziału.
	 */
	@SerializedName("nazwa")
	private String name;
	
	/**
	 * Lista kierunków wydziału.
	 */
	@SerializedName("kierunki")
	private List<Division> divisions;
	
}
