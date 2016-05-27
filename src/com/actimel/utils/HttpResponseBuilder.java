package com.actimel.utils;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response;

public class HttpResponseBuilder {
	
	private HtmlTemplate template;
	
	public HttpResponseBuilder(HtmlTemplate template) {
		this.template = template;
	}
	
	public HttpResponseBuilder putVar(String key, String val) {
		template.putYieldVar(key, val);
		return this;
	}
	
	public Response execute() {
		return NanoHTTPD.newFixedLengthResponse(template.render());
	}
}
