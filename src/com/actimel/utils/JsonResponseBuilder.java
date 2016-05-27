package com.actimel.utils;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.actimel.calendar.CalendarApp;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

public class JsonResponseBuilder {
	
	private CalendarApp app;
	
	private HashMap<String, String> params = new HashMap<String, String>();
	
	public JsonResponseBuilder(CalendarApp app, String key, String val) {
		this.app = app;
		params.put(key, val);
	}
	
	public JsonResponseBuilder put(String key, Object val) {
		params.put(key, String.valueOf(val));
		return this;
	}
	
	public Response create() {
		Type t = Utils.getGsonHashMapType();		
		String json = app.getGson().toJson(params, t); 
		return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/json", json);
	}
}
