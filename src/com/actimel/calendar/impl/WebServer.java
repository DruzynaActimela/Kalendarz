package com.actimel.calendar.impl;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.actimel.calendar.CalendarApp;
import com.actimel.controllers.SessionController;
import com.actimel.utils.HtmlTemplate;
import com.actimel.utils.Utils;

import fi.iki.elonen.NanoHTTPD;



public class WebServer extends NanoHTTPD {
	
	private CalendarApp app;
	private SessionController sessionController;
    public WebServer(CalendarApp app, int port) {
        super(port);
        
        sessionController = new SessionController();
        
        this.app = app;
    }
    
    @Override
    public Response serve(IHTTPSession session) {
    	
    	
    	try {
    		String SEPARATOR = File.separator;
    		
    		
    		
        	String www_root_dir = "";
        	String uri = session.getUri();
        	
			
			List<String> apiWhitelist = Arrays.asList("127.0.0.1");
			boolean whitelistEnabled = true;
			
        	if(uri.startsWith("/api")) {
        		
        		HashMap<String, String> response = new HashMap<String, String>();
        		
        		InetAddress IP=InetAddress.getLocalHost();
        		//System.out.println("IP of my system is := "+IP.getHostAddress());
        		//System.out.println("Remote addr is: " + session.getHeaders().toString());
        		String clientIp = session.getHeaders().get("http-client-ip");
        		
        		if(whitelistEnabled && apiWhitelist.contains(clientIp) == false) {
        			response.put("type", "error");
        			response.put("message", "Nieautoryzowany dostêp.");
        			return newFixedLengthResponse(Response.Status.OK, "text/json", app.getGson().toJson(response, Utils.getGsonHashMapType()));
        		}
        		
        		
        		Map<String, String> params = session.getParms();
        		String requestMode = params.get("m");
        		if(requestMode != null) {
        			requestMode = requestMode.toLowerCase().trim();
        		}
        		
        		Utils.log(params.toString());
	        		
	        	if("buy-city".equals(requestMode)) {

		        }
        	} else if(uri.startsWith("/login")) {
        		// logowanie
        		if(session.getMethod().equals(Method.POST)) {
        			// sprawdzenie danych
        			
        			
        		} else {
        			// pokazanie strony logowania
        			HtmlTemplate loginTemplate = HtmlTemplate.loadFromResource("login.html");        			
        			return newFixedLengthResponse(loginTemplate.render());
        		}
        	} else if(uri.equals("/") || uri.contains("index.")) {
    			HtmlTemplate loginTemplate = HtmlTemplate.loadFromResource("index.html");        			
    			return newFixedLengthResponse(loginTemplate.render());
    		} else {
        		
        		String resource_path = (www_root_dir + uri).trim();

        		
            	resource_path = resource_path.replaceAll("\\|\\/", SEPARATOR);
            	resource_path = resource_path.substring(1);
            	String ext = Utils.getExtension(resource_path);
            	Utils.log("extension: " + ext);
            	
            	HashMap<String, String> mimes = new HashMap<String, String>();
            	String defaultMime = MIME_PLAINTEXT;
            	mimes.put("js", MIME_JS);
            	mimes.put("css", MIME_CSS);
            	mimes.put("png", MIME_PNG);
            	mimes.put("html", MIME_HTML);
            	
            	String mime = (mimes.containsKey(ext) ? mimes.get(ext) : defaultMime);
            	
            	
            	
            	Utils.log("resource_path: " + resource_path);
            	
            	URL resourceURL = getClass().getClassLoader().getResource(resource_path);
            	if(resourceURL != null) {
                	InputStream is = resourceURL.openStream();            	
                	return newChunkedResponse(Response.Status.OK, mime, is);
            	}
        	}

    	} catch(Exception e) {
    		e.printStackTrace();
    	}

    	return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML, "404: Resource not Found");
    }
}
