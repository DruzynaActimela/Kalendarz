package com.actimel.calendar.impl;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.actimel.calendar.CalendarApp;
import com.actimel.calendar.Const;
import com.actimel.calendar.FileStorage;
import com.actimel.controllers.SessionController;
import com.actimel.models.CalendarEvent;
import com.actimel.models.EventGroup;
import com.actimel.models.Session;
import com.actimel.models.User;
import com.actimel.utils.HtmlTemplate;
import com.actimel.utils.HttpResponseBuilder;
import com.actimel.utils.JsonResponseBuilder;
import com.actimel.utils.Utils;
import com.google.gson.reflect.TypeToken;

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
        		
        		if(whitelistEnabled && apiWhitelist.contains(clientIp) == false) {
        			response.put("type", "error");
        			response.put("message", "Nieautoryzowany dostêp.");
        			return newFixedLengthResponse(Response.Status.OK, "text/json", app.getGson().toJson(response, Utils.getGsonHashMapType()));
        		}
        		
        		
        		Map<String, String> params = httpSession.getParms();
        		String requestMode = null; // /api/mode
        		String subRequestMode = null; // /api/mode/submode
        		
        		if(uriSplit.length > 2) {
        			requestMode = uriSplit[2].trim();
        		}
        		if(uriSplit.length > 3) {
        			subRequestMode = uriSplit[3].trim();
        		}

        		Utils.log(requestMode, params.toString());
	        		
	        	if("events".equals(requestMode)) { // {start=2016-04-25, end=2016-06-06, _=1464436912054}
	        		if(subRequestMode != null) {
	        			if("create".equals(subRequestMode)) {
	        				String name = params.get("name");
	        				
	        				if(Utils.isGroupNameValid(name)) {
	        					String groupId = params.get("group");
	        					int groupIdNum = Integer.parseInt(groupId);
	        					EventGroup eGroup = app.getStorage().loadEventGroup(groupIdNum);
	        					if(eGroup != null) {
			        				String date_start = params.get("date_start");
			        				String date_end = params.get("date_end");
			        				boolean is_public = ("1".equals(params.get("public")));
			        				boolean is_wholeday = ("1".equals(params.get("wholeday")));
			        				
			        				int ownerId = (userSession != null) ? userSession.getUser().getId() : 1; // dla testow
			        				
			        				int uid = 0;
			        				if(app.getStorage() instanceof FileStorage) {
			        					uid = ((FileStorage) app.getStorage()).getHighestEventId() + 1;
			        				}
			        				long timestamp_start = Utils.dateToTimestamp(date_start, Const.DATE_FORMAT_DAY_TIME);
			        				long timestamp_end = Utils.dateToTimestamp(date_end, Const.DATE_FORMAT_DAY_TIME);
	
	
			        				CalendarEvent event = new CalendarEvent(uid, name, timestamp_start, timestamp_end, is_wholeday, is_public);
			        				event.setOwnerId(ownerId);
			        				event.setParentGroupId(groupIdNum);
			        				
			        				app.getStorage().saveEvent(event);
			        				
			        				if(app.getStorage() instanceof FileStorage) {
			        					((FileStorage) app.getStorage()).saveEvents();
			        				}

			        				return jsonResponse("message", "Zdarzenie '"+name+"' zosta³o utworzone!").put("type", "success").put("eventId", ""+uid).create();
	        					} else {
	        						return jsonResponse("message", "Podana grupa nie istnieje!").put("type", "error").create();
	        					}
	        				} else {
	        					return jsonResponse("message", "W nazwie zdarzenia znajduj¹ siê niedozwolone znaki!").put("type", "error").create();
	        				}
	        			} else if("change".equals(subRequestMode)) {
	        				Integer eventId = Utils.parseInt(params.get("eventId"), -1);
	        				Long newStart = Utils.dateToTimestamp(params.get("new_start"), Const.DATE_FORMAT_DAY_TIME);
	        				Long newEnd =  Utils.dateToTimestamp(params.get("new_end"), Const.DATE_FORMAT_DAY_TIME);
	        				
	        				if(eventId > 0 && newStart > 0 && newEnd > 0) {
	        					CalendarEvent evt = app.getStorage().loadEvent(eventId);
	        					if(evt != null) {
	        						evt.setStampStart(newStart);
	        						evt.setStampEnd(newEnd);
	        						app.getStorage().saveEvent(evt);
	        						
	        						if(app.getStorage() instanceof FileStorage) {
			        					((FileStorage) app.getStorage()).saveEvents();
			        				}	        						
	        						
	        						return jsonResponse("message", "Zmiany zosta³y zapisane.").put("type", "success").create();
	        					} else {
	        						return jsonResponse("message", "Nie ma takiego zdarzenia!").put("type", "error").create();
	        					}	        					
	        				} else {
	        					return jsonResponse("message", "B³êdne dane!").put("type", "error").create();
	        				}	        				
	        			} else {
	        				// nie ma takiego zapytania dla eventów
	        			}
	        		} else {
	        			// display events
		        		String start = params.get("start");
		        		String end = params.get("end");
	        			// wyœwietl listê grup u¿ytkownika
		        		
		        		long multiply_start = 1, multiply_end = 1;
		        		if(start != null && start.length() == 10) multiply_start = 1000;
		        		if(end != null && end.length() == 10) multiply_end = 1000;
		        		
		        		
		        		long tStart = Long.valueOf(start) * multiply_start;
		        		long tEnd = Long.valueOf(end) * multiply_end;
		        		
		        		
	        			int sessionUserId = (userSession != null) ? userSession.getUser().getId() : 1;	        			
	        			List<CalendarEvent> events = app.getStorage().searchEventsBetween(tStart, tEnd, sessionUserId);
	        			Type t = new TypeToken<List<CalendarEvent>>(){}.getType();
	        			String json = app.getGson().toJson(events, t);
	        			return newFixedLengthResponse(Response.Status.OK, "text/json", json);
	        		}	 
	        	} else if("groups".equals(requestMode)) {
	        		if(subRequestMode != null) {
	        			if("create".equals(subRequestMode)) {
	        				String name = params.get("name");
	        				if(Utils.isGroupNameValid(name)) {		        					
		        				
		        				String color = params.get("color");
		        				int ownerId = (userSession != null) ? userSession.getUser().getId() : 1; // dla testow
		        				boolean is_public = ("1".equals(params.get("public")));
		        				
		        				int uid = 0;
		        				if(app.getStorage() instanceof FileStorage) {
		        					uid = ((FileStorage) app.getStorage()).getHighestEventGroupId() + 1;
		        				}
		        				
		        				EventGroup group = new EventGroup(uid, name, color, ownerId, is_public);
		        				app.getStorage().saveEventGroup(group);
		        				
		        				if(app.getStorage() instanceof FileStorage) {
		        					((FileStorage) app.getStorage()).saveEventGroups();
		        				}
		        				
		        				return jsonResponse("message", "Grupa zdarzeñ '"+name+"' zosta³a utworzona!").put("type", "success").create();
	        				} else {
	        					return jsonResponse("message", "Nazwa zawiera niedozwolone znaki.").put("type", "error").create();
	        				}
	        			} else {
	        				// nie ma takiego zapytania dla grup
	        			}
	        		} else {
	        			// wyœwietl listê grup u¿ytkownika
	        			int sessionUserId = (userSession != null) ? userSession.getUser().getId() : 1;	        			
	        			List<EventGroup> groups = app.getStorage().searchEventsGroups("" + sessionUserId, "owner_id");
	        			Type t = new TypeToken<List<EventGroup>>(){}.getType();
	        			String json = app.getGson().toJson(groups, t);
	        			return newFixedLengthResponse(Response.Status.OK, "text/json", json);
	        		}
		        } else {
		        	// nie ma takiego zapytania
		        }
        	} else if(uri.startsWith("/dashboard")) {
        		String username = "user"; //userSession.getUser().getName();
        		int admin_level = 1;
        		HtmlTemplate dashboardTemplate = HtmlTemplate.loadFromResource("dashboard.html@" + username); // mo¿liwoœæ cache'owania dla poszczegolnych uzytkownikow
        		dashboardTemplate.putYieldVar("current_username", username);
        		if(admin_level > 0) {
        			dashboardTemplate.putYieldVar("is_admin", "" + admin_level);
        		}
        		
        		
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
        		if(Const.LOAD_FROM_DISK) {
        			URL location = HtmlTemplate.class.getProtectionDomain().getCodeSource().getLocation();
        			File directory = new File(location.getFile());
        			Utils.log(directory.getAbsolutePath());
        			File www_directory = new File(directory.getAbsolutePath() + File.separator + ".."+ File.separator + "www");
        			
        			resourceURL = new File(www_directory, resource_path).toURI().toURL();
        		}
            	
        		File resourceFile = new File(resourceURL.getFile());
            	if(resourceURL != null && resourceFile.exists()) {
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
