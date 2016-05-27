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
import com.actimel.calendar.Const;
import com.actimel.calendar.FileStorage;
import com.actimel.controllers.SessionController;
import com.actimel.models.Session;
import com.actimel.models.User;
import com.actimel.utils.HtmlTemplate;
import com.actimel.utils.HttpResponseBuilder;
import com.actimel.utils.JsonResponseBuilder;
import com.actimel.utils.Utils;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;



public class WebServer extends NanoHTTPD {
	
	private CalendarApp app;
	private SessionController sessionController;
	
	List<String> excludeFoldersFromLoginCheck = Arrays.asList(
			"js",
			"css",
			"fullcalendar",
			"images",
			"font-awesome"			
	);
	
	List<String> allowedRoutesWithoutLogin = Arrays.asList(
			"login",
			"register"
	);
	
    public WebServer(CalendarApp app, int port) {
        super(port);
        
        sessionController = new SessionController();
        
        this.app = app;
    }
    
    public HttpResponseBuilder outputResponse(String template) {
    	HtmlTemplate tmpl = HtmlTemplate.loadFromResource(template);
		return new HttpResponseBuilder(tmpl);
    }
    public JsonResponseBuilder jsonResponse(String key, Object val) {
		return new JsonResponseBuilder(app, key, String.valueOf(val));
    }
    
    private Response doRedirect(String uri) {
    	NanoHTTPD.Response res = newFixedLengthResponse(Status.REDIRECT, "text/plain", "");
		res.addHeader("Location", uri);
		return res;
    }
    
    @Override
    public Response serve(IHTTPSession httpSession) {

    	try {
    		String SEPARATOR = File.separator;
    		
        	Map<String, String> files = new HashMap<String, String>();
        	httpSession.parseBody(files);
    		
    		
    		String clientIp = httpSession.getHeaders().get("http-client-ip");
        	String www_root_dir = "";
        	String uri = httpSession.getUri();

        	
			
			List<String> apiWhitelist = Arrays.asList("127.0.0.1");
			boolean whitelistEnabled = true;
			
			String[] uriSplit = uri.split("/");
			String currentRoute = (uriSplit.length > 1) ? uriSplit[1] :  uriSplit[0];
			
			Utils.log("currentRoute: " + currentRoute);
			//Utils.log(httpSession.getHeaders().toString());
			String cookieSessionKey = httpSession.getCookies().read(Const.COOKIE_SESSION_KEY);
			Session userSession = null;
			
			if(cookieSessionKey != null) {
				userSession = sessionController.getSessionByKey(cookieSessionKey.trim());
			}
			
			
			if(!allowedRoutesWithoutLogin.contains(currentRoute) && userSession == null && !excludeFoldersFromLoginCheck.contains(currentRoute)) {
				// wy³¹czone na czas pisania
				//return doRedirect("login#message:not-authorized");
			}

        	if(uri.startsWith("/api")) {
        		
        		HashMap<String, String> response = new HashMap<String, String>();
        		
        		InetAddress IP=InetAddress.getLocalHost();
        		//System.out.println("IP of my system is := "+IP.getHostAddress());
        		//System.out.println("Remote addr is: " + session.getHeaders().toString());
        		
        		
        		if(whitelistEnabled && apiWhitelist.contains(clientIp) == false) {
        			response.put("type", "error");
        			response.put("message", "Nieautoryzowany dostêp.");
        			return newFixedLengthResponse(Response.Status.OK, "text/json", app.getGson().toJson(response, Utils.getGsonHashMapType()));
        		}
        		
        		
        		Map<String, String> params = httpSession.getParms();
        		String requestMode = params.get("m");
        		if(requestMode != null) {
        			requestMode = requestMode.toLowerCase().trim();
        		}
        		
        		Utils.log(params.toString());
	        		
	        	if("buy-city".equals(requestMode)) {

		        }
        	} else if(uri.startsWith("/dashboard")) {
        		
        		HtmlTemplate dashboardTemplate = HtmlTemplate.loadFromResource("dashboard.html", false);
        		dashboardTemplate.putYieldVar("current_username", "user");
        		//dashboardTemplate.putYieldVar("current_username", userSession.getUser().getName());
    			return newFixedLengthResponse(dashboardTemplate.render());
        	} else if(uri.startsWith("/login")) {
        		
        		if(userSession != null) {
        			return doRedirect("dashboard");
        		}
        		
        		// logowanie
        		if(httpSession.getMethod().equals(Method.POST)) {        			
        			String username = httpSession.getParms().get("username");
        			String password = httpSession.getParms().get("password");
        			
        			User user = app.getStorage().loadUser(username);
        			if(user != null) {
        				if(user.isPasswordCorrect(password)) {
        					Session userAuthSession = sessionController.createSession(user, clientIp);
        					httpSession.getCookies().set(Const.COOKIE_SESSION_KEY, userAuthSession.getKey(), Const.COOKIE_SESSION_LIFETIME);
        					
        					return jsonResponse("message", "Zalogowano.").put("type", "success").put("redirect", "dashboard").create();
        				} else {
        					return jsonResponse("message", "B³êdne has³o.").put("type", "error").create();
        				}
        			} else {
        				return jsonResponse("message", "Nie odnaleziono u¿ytkownika.").put("type", "error").create();
        			}
        		} else {
        			// pokazanie strony logowania
        			HtmlTemplate loginTemplate = HtmlTemplate.loadFromResource("login.html");        			
        			return newFixedLengthResponse(loginTemplate.render());
        		}
        	} else if(uri.startsWith("/register")) {
        		if(userSession != null) {
        			return doRedirect("dashboard");
        		}
        		
        		// rejestracja
        		
        		if(httpSession.getMethod().equals(Method.POST)) {
        			// sprawdzenie danych
        			String username = httpSession.getParms().get("username");
        			String password = httpSession.getParms().get("password");
        			String email = httpSession.getParms().get("email");
        			
        			if(username != null && password != null && email != null) {
        				if(Utils.isUsernameValid(username)) {       					
	        				
	        				int uid = 0;
	        				if(app.getStorage() instanceof FileStorage) {
	        					uid = ((FileStorage) app.getStorage()).getHighestUserId() + 1;
	        				}
	        				
	        				User user = new User(uid, username, password, email, 0);
	        				app.getStorage().storeUser(user);
	        				
	        				if(app.getStorage() instanceof FileStorage) {
	        					((FileStorage) app.getStorage()).saveUsers();
	        				}

	        				return jsonResponse("message", "Konto zosta³o zarejestrowane!").put("type", "success").create();
        				} else {
        					return jsonResponse("message", "Nazwa u¿ytkownika nie spe³nia wymagañ. (3-16 znaków, znaki alfanumeryczne)").put("type", "error").create();
        				}
        			} else {
        				return jsonResponse("message", "Nie podano danych.").put("type", "error").create();
        			}
        		} else {
        			// pokazanie strony
        			HtmlTemplate loginTemplate = HtmlTemplate.loadFromResource("register.html");        			
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
            	//Utils.log("extension: " + ext);
            	
            	HashMap<String, String> mimes = new HashMap<String, String>();
            	String defaultMime = MIME_PLAINTEXT;
            	mimes.put("js", MIME_JS);
            	mimes.put("css", MIME_CSS);
            	mimes.put("png", MIME_PNG);
            	mimes.put("html", MIME_HTML);
            	
            	String mime = (mimes.containsKey(ext) ? mimes.get(ext) : defaultMime);
            	
            	
            	
            	//Utils.log("resource_path: " + resource_path);
            	
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
