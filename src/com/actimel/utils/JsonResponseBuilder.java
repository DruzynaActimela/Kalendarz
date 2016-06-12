package com.actimel.utils;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.actimel.calendar.CalendarApp;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

/**
 * Klasa odpowiedzialna za budowanie odpowiedzi JSON.
 * @author ActimelTeam
 *
 */
public class JsonResponseBuilder {
	
	/**
	 * Referencja do aplikacji kalendarza.
	 * @uml.property  name="app"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private CalendarApp app;
	
	/**
	 * Lista parametrów.
	 * @uml.property  name="params"
	 * @uml.associationEnd  qualifier="key:java.lang.String java.lang.String"
	 */
	private HashMap<String, String> params = new HashMap<String, String>();
	
	/**
	 * Konstruktor.
	 * @param cApp Referencja do aplikacji kalendarza.
	 * @param key Pocz¹tkowy klucz
	 * @param val Pocz¹tkowa wartoœc
	 */
	public JsonResponseBuilder(final CalendarApp cApp, final String key, final String val) {
		this.app = cApp;
		params.put(key, val);
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca dodanie kolejnych parametrów klucz => wartoœc.
	 * @param key Klucz
	 * @param val Wartosc
	 * @return Obiekt buildera do którego zosta³a dodana wartoœc
	 */
	public final JsonResponseBuilder put(final String key, final Object val) {
		params.put(key, String.valueOf(val));
		return this;
	}
	
	/**
	 * Metoda zwracaj¹ca odpowiedŸ NanoHTTPD 
	 * ze wszystkimi dodanymi parametrami, jako JSON.
	 * @return NanoHttpd.Response
	 */
	public final Response create() {

		return NanoHTTPD.newFixedLengthResponse(
				NanoHTTPD.Response.Status.OK, 
				"text/json", 
				this.toJson()
		);
		
	}
	
	
	/**
	 * Metoda zwracaj¹ca przechowywane wartosci jako obiekt JSON.
	 * @return wartosci jako JSON
	 */
	public final String toJson() {
		Type t = Utils.getGsonHashMapType();		
		return app.getGson().toJson(params, t);		
	}
}
