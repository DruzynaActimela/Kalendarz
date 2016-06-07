package com.actimel.calendar.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.actimel.calendar.CalendarApp;
import com.actimel.calendar.Const;
import com.actimel.calendar.FileStorage;
import com.actimel.controllers.FileController;
import com.actimel.controllers.SessionController;
import com.actimel.intfs.CalendarExporter;
import com.actimel.intfs.CalendarImporter;
import com.actimel.models.CalendarEvent;
import com.actimel.models.EventGroup;
import com.actimel.models.Session;
import com.actimel.models.User;
import com.actimel.utils.HtmlTemplate;
import com.actimel.utils.JsonResponseBuilder;
import com.actimel.utils.Utils;
import com.google.gson.reflect.TypeToken;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;


/**
 * Implementacja serwera WWW.
 * @author 4i60r
 *
 */
public class WebServer extends NanoHTTPD {
	
	/**
	 * Obiekt aplikacji kalendarza do ³atwego dostêpu przy obs³udze zapytañ.
	 * @uml.property  name="app"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="webServer:com.actimel.calendar.CalendarApp"
	 */
	private final CalendarApp app;
	
	/**
	 * Obiekt kontrolera odpowiedzialnego za obs³ugê sesji u¿ytkowników.
	 * @uml.property  name="sessionController"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private final SessionController sessionController;
	
	/**
	 * Obiekt kontrolera odpowiedzialnego za obs³ugê plików.
	 * @uml.property  name="fileController"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private FileController fileController = null;

	
	/**
	 * Lista folderów, które nie s¹ brane pod uwagê  przy sprawdzaniu stanu zalogowania u¿ytkownia.
	 * @uml.property  name="excludeFoldersFromLoginCheck"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private final List<String> excludeFoldersFromLoginCheck = Arrays.asList(
			"js",
			"css",
			"fullcalendar",
			"images",
			"font-awesome"			
	);
	
	/**
	 * Lista œcie¿ek, na które mog¹  siê zaczynac lokalizacje przy pobieraniu.
	 * @uml.property  name="allowedDownloadLocations"
	 */
	private final List<String> allowedDownloadLocations = Arrays.asList(
			"file_storage/"
	);
	
	/**
	 * Lista stron (dróg), które s¹ dostêpne bez zalogowania.
	 * @uml.property  name="allowedRoutesWithoutLogin"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private final List<String> allowedRoutesWithoutLogin = Arrays.asList(
			"login",
			"register"
	);
	
	/**
	 * Konstruktor implementacji serwera WWW.
	 * @param calendarApp Instancja aplikacji kalendarza
	 * @param port Port, na którym serwer bêdzie nas³uchiwa³ po³¹czeñ
	 */
    public WebServer(final CalendarApp calendarApp, final int port) {
        super(port);
        
        sessionController = new SessionController();
        try {
			fileController = new FileController(Const.FILE_STORE_FOLDER);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        this.app = calendarApp;
    }
    
    /**
     * Funkcja pomocnicza, zwraca builder odpowiedzi JSON.
     * @param key Pocz¹tkowy klucz
     * @param val Pocz¹tkowa wartoœc dla klucza
     * @return builder odpowiedzi JSON
     */
    public final JsonResponseBuilder jsonResponse(final String key, final Object val) {
		return new JsonResponseBuilder(app, key, String.valueOf(val));
    }
    
    /**
     * Funkcja odpowiedzialna za przekierowanie u¿ytkownika pod podany adres.
     * @param uri Adres, na który u¿ytkownik ma zostac przekierowany
     * @return Instacja odpowiedzi NanoHTTPD z nag³ówkiem przekierowania
     */
    private Response doRedirect(final String uri) {
    	NanoHTTPD.Response res = newFixedLengthResponse(Status.REDIRECT, "text/plain", "");
		res.addHeader("Location", uri);
		return res;
    }


    @Override
	public final Response serve(final IHTTPSession httpSession) {

    	try {
    		String filePathSeparator = File.separator;
    		
        	Map<String, String> files = new HashMap<String, String>();
        	httpSession.parseBody(files);
    		
    		
    		String clientIp = httpSession.getHeaders().get("http-client-ip");
        	String wwwRootDir = "";
        	String uri = httpSession.getUri();

        	
			
			List<String> apiWhitelist = Arrays.asList("127.0.0.1");
			boolean whitelistEnabled = true;
			
			String[] uriSplit = uri.split("/");
			String currentRoute = (uriSplit.length > 1) ? uriSplit[1] :  uriSplit[0];
			
			Utils.log("currentRoute: " + currentRoute);
			//Utils.log(httpSession.getHeaders().toString());
			String cookieSessionKey = httpSession.getCookies().read(Const.COOKIE_SESSION_KEY);
			Session userSession = null;
			
			if (cookieSessionKey != null) {
				userSession = sessionController.getSessionByKey(cookieSessionKey.trim());
			}
			final int sessionUserId = (userSession != null) ? userSession.getUserId() : 1;	
			
			if (!allowedRoutesWithoutLogin.contains(currentRoute) && userSession == null && !excludeFoldersFromLoginCheck.contains(currentRoute)) {
				// wy³¹czone na czas pisania
				//return doRedirect("login#message:not-authorized");
			}

        	if (uri.startsWith("/api")) {
        		
        		HashMap<String, String> response = new HashMap<String, String>();
        		
        		if (whitelistEnabled && !apiWhitelist.contains(clientIp)) {
        			response.put("type", "error");
        			response.put("message", "Nieautoryzowany dostêp.");
        			return newFixedLengthResponse(Response.Status.OK, "text/json", app.getGson().toJson(response, Utils.getGsonHashMapType()));
        		}
        		
        		
        		Map<String, String> params = httpSession.getParms();
        		String requestMode = null; // /api/mode
        		String subRequestMode = null; // /api/mode/submode
        		
        		if (uriSplit.length > 2) {
        			requestMode = uriSplit[2].trim();
        		}
        		
        		final int paramCountThree = 3;
        		if (uriSplit.length > paramCountThree) {
        			subRequestMode = uriSplit[paramCountThree].trim();
        		}
        		

        		Utils.log(requestMode, params.toString());
	        		
	        	if ("events".equals(requestMode)) {
	        		if (subRequestMode != null) {
	        			if ("create".equals(subRequestMode)) {
	        				String name = params.get("name");
	        				
	        				if (Utils.isGroupNameValid(name)) {
	        					String groupId = params.get("group");
	        					int groupIdNum = Integer.parseInt(groupId);
	        					EventGroup eGroup = app.getStorage().loadEventGroup(groupIdNum);
	        					if (eGroup != null) {
			        				String dateStart = params.get("date_start");
			        				String dateEnd = params.get("date_end");
			        				boolean isPublic = ("1".equals(params.get("public")));
			        				boolean isWholeday = ("1".equals(params.get("wholeday")));
			        				
			        				int ownerId = (userSession != null) ? userSession.getUser().getId() : 1; // dla testow
			        				
			        				int uid = 0;
			        				if (app.getStorage() instanceof FileStorage) {
			        					uid = ((FileStorage) app.getStorage()).getHighestEventId() + 1;
			        				}
			        				long timestampStart = Utils.dateToTimestamp(dateStart, Const.DATE_FORMAT_DAY_TIME);
			        				long timestampEnd = Utils.dateToTimestamp(dateEnd, Const.DATE_FORMAT_DAY_TIME);
	
	
			        				CalendarEvent event = new CalendarEvent(uid, name, timestampStart, timestampEnd, isWholeday, isPublic);
			        				event.setOwnerId(ownerId);
			        				event.setParentGroupId(groupIdNum);
			        				
			        				app.getStorage().saveEvent(event);
			        				
			        				if (app.getStorage() instanceof FileStorage) {
			        					((FileStorage) app.getStorage()).saveEvents();
			        				}

			        				return jsonResponse("message", "Zdarzenie '" + name + "' zosta³o utworzone!").put("type", "success").put("eventId", "" + uid).create();
	        					} else {
	        						return jsonResponse("message", "Podany event nie istnieje!").put("type", "error").create();
	        					}
	        				} else {
	        					return jsonResponse("message", "W nazwie zdarzenia znajduj¹ siê niedozwolone znaki!").put("type", "error").create();
	        				}
	        			} else if ("saveEvent".equals(subRequestMode)) {
	        				Integer eventId = Utils.parseInt(params.get("id"), -1);
	        				if (eventId > 0) {
	        					String name = params.get("name");
		        				if (Utils.isGroupNameValid(name)) {
		        					String groupId = params.get("group");
		        					int groupIdNum = Integer.parseInt(groupId);
		        					EventGroup eGroup = app.getStorage().loadEventGroup(groupIdNum);
		        					if (eGroup != null) {

				        				CalendarEvent event = app.getStorage().loadEvent(eventId);
				        				if (event != null && event.getOwnerId() == sessionUserId) {
				        					
					        				String dateStart = params.get("date_start");
					        				String dateEnd = params.get("date_end");
					        				boolean isPublic = ("1".equals(params.get("public")));
					        				boolean isWholeday = ("1".equals(params.get("wholeday")));
					        				
					        				final long timestampStart = Utils.dateToTimestamp(dateStart, Const.DATE_FORMAT_DAY_TIME);
					        				final long timestampEnd = Utils.dateToTimestamp(dateEnd, Const.DATE_FORMAT_DAY_TIME);
					        				
					        				
					        				event.setName(name);
					        				event.setStampStart(timestampStart);
					        				event.setStampEnd(timestampEnd);
					        				event.setAllDay(isWholeday);
					        				event.setPublic(isPublic);
					        				event.setParentGroupId(groupIdNum);
					        				
					        				app.getStorage().saveEvent(event);
					        				if (app.getStorage() instanceof FileStorage) {
					        					((FileStorage) app.getStorage()).saveEvents();
					        				}

					        				return jsonResponse("message", "Zdarzenie '" + name + "' zosta³o zapisane!").put("type", "success").create();
				        				} else {
				        					return jsonResponse("message", "Podany event nie istnieje lub nie jesteœ w³aœcicielem!").put("type", "error").create();
				        				}
		        					} else {
		        						return jsonResponse("message", "Podany event nie istnieje!").put("type", "error").create();
		        					}
		        				} else {
		        					return jsonResponse("message", "W nazwie zdarzenia znajduj¹ siê niedozwolone znaki!").put("type", "error").create();
		        				}
	        				} else {
	        					return jsonResponse("message", "Zdarzenie nie istnieje!").put("type", "error").create();
	        				}
	        				
	        			} else if ("change".equals(subRequestMode)) {
	        				Integer eventId = Utils.parseInt(params.get("eventId"), -1);
	        				Long newStart = Utils.dateToTimestamp(params.get("new_start"), Const.DATE_FORMAT_DAY_TIME);
	        				Long newEnd =  Utils.dateToTimestamp(params.get("new_end"), Const.DATE_FORMAT_DAY_TIME);
	        				
	        				Response errorResponse = jsonResponse("message", "B³êdne dane!").put("type", "error").create();
	        				
	        				if (eventId > 0 && newStart > 0) {
	        					CalendarEvent evt = app.getStorage().loadEvent(eventId);
	        					if (evt != null) {
	        						if (newEnd < 1 && !evt.isAllDay()) {
	        							return errorResponse;
	        						}
	        						
	        						evt.setStampStart(newStart);
	        						evt.setStampEnd(newEnd);
	        						app.getStorage().saveEvent(evt);
	        						
	        						if (app.getStorage() instanceof FileStorage) {
			        					((FileStorage) app.getStorage()).saveEvents();
			        				}	        						
	        						
	        						return jsonResponse("message", "Zmiany zosta³y zapisane.").put("type", "success").create();
	        					} else {
	        						return jsonResponse("message", "Nie ma takiego zdarzenia!").put("type", "error").create();
	        					}
	        				} else {
	        					return errorResponse;
	        				}	   
	        			} else if ("delete".equals(subRequestMode)) {
	        				Integer eventId = Utils.parseInt(params.get("id"), -1);
	        				Response errorResponse = jsonResponse("message", "B³êdne dane!").put("type", "error").create();
	        				
	        				if (eventId > 0) {
	        					CalendarEvent evt = app.getStorage().loadEvent(eventId);
	        					if (evt != null) {
	        						
	        						if (evt.getOwnerId() == sessionUserId) {
	        							evt = null;
	        							app.getStorage().deleteEvent(eventId);
	        							return jsonResponse("message", "Zdarzenie zosta³o usuniête.").put("type", "success").create();
	        						} else {
	        							return jsonResponse("message", "Nie jesteœ w³aœcicielem tego zdarzenia!").put("type", "error").create();
	        						}	        						
	        					} else {
	        						return jsonResponse("message", "Nie ma takiego zdarzenia!").put("type", "error").create();
	        					}
	        				} else {
	        					return errorResponse;
	        				}	
	        			} else if ("import".equals(subRequestMode)) {
	        				final boolean confirmed = "yes".equals(params.get("confirm"));
	        				
	        				String importType = params.get("importType");
	        				String groupId = params.get("groupId");
	
	        				List<CalendarEvent> eventsFound = new ArrayList<CalendarEvent>();
	        				File calendarFile = null;
	        				
	        				Integer pGroupId = Utils.parseInt(groupId, -1);
	        				
	        				if (files.size() > 0) {
	        				 	
	        				 	for (Entry<String, String> f : files.entrySet()) {
	        				 		calendarFile = new File(f.getValue());
	        				 		break;
	        				 	}
	        				 	Utils.log("Import file path: " + calendarFile.getAbsolutePath());
	        				 	

	        				 	if ("ical".equals(importType)) {
	        				 		ICalImporter importer = new ICalImporter();
	        				 		importer.importEvents(calendarFile);
	        				 		eventsFound = importer.getEvents();
	        				 	} else {
	        				 		CSVImporter importer = new CSVImporter();
	        				 		importer.importEvents(calendarFile);
	        				 		eventsFound = importer.getEvents();
	        				 	}
	        				 	
	        				} else {
	        					return jsonResponse("message", "Nie za³¹czono pliku!").put("type", "error").create();
	        				}
	        				
	        				if (confirmed) {
	        					
	        					int newHighestId = 0;
	        					if (app.getStorage() instanceof FileStorage) {
	        						newHighestId = ((FileStorage) app.getStorage()).getHighestEventId() + 1;
	        					}
	        					for (CalendarEvent cEvent : eventsFound) {
	        						cEvent.setId(newHighestId);
	        						cEvent.setOwnerId(sessionUserId);
	        						if (pGroupId > 0) {
										cEvent.setParentGroupId(pGroupId);
									}
	        						app.getStorage().saveEvent(cEvent);
	        						Utils.log("Saved imported event at ID: " + newHighestId + ", parent group ID: " + pGroupId);
	        						newHighestId++;
	        						
	        						
	        					}
	        					
	        					if (app.getStorage() instanceof FileStorage) {
	        						((FileStorage) app.getStorage()).saveEvents();
	        					}
	        					
	        					return jsonResponse("message", "Zaimportowano <b>" + eventsFound.size() + "</b> zdarzeñ.").put("type", "success").create();
	        				} else {
	        					return jsonResponse("affected_events", "" + eventsFound.size()).put("type", "success").create();
	        				}
	        			
	        			} else if ("export".equals(subRequestMode)) {
	        				final boolean confirmed = "yes".equals(params.get("confirm"));
	        				
	        				String exportType = params.get("exportType");
	        				String groupIds = params.get("groupIds");
	        				String[] ids = groupIds.split(",");
	        				List<Integer> allowedGroups = new ArrayList<Integer>();
	        				for (String id : ids) {
	        					allowedGroups.add(Utils.parseInt(id, -1));
	        				}
	        				Long start = Utils.dateToTimestamp(params.get("start"), Const.DATE_FORMAT_DAY_TIME);
	        				Long end = Utils.dateToTimestamp(params.get("end"), Const.DATE_FORMAT_DAY_TIME);
	        				
	        				List<CalendarEvent> eventsInTimeline = app.getStorage().searchEventsBetween(start, end, sessionUserId);
	        				List<CalendarEvent> selectedEvents = new ArrayList<CalendarEvent>();
	        				for (CalendarEvent evt : eventsInTimeline) {
	        					if (allowedGroups.contains(evt.getParentGroupId())) {
	        						selectedEvents.add(evt);
	        					}
	        				}
	        				
	        				if (confirmed) {
	        					
	        					CalendarExporter exporter = null;
	        					String extension = "";
	        					
	        					if ("ical".equals(exportType)) {
	        						exporter = new ICalExporter();
	        						extension = ".ics";
	        					} else {
	        						exporter = new CSVExporter();
	        						extension = ".csv";
	        					}
	        					final int filenameLength = 32;
	        					String result = exporter.export(selectedEvents);
	        					String filename = Utils.getRandomString(filenameLength) + "." + extension;
	        					
	        					String calendarFileUri = fileController.storeContent(result, filename, FileController.URI_REMOTE);
	        					
	        					String downloadLink = "http://localhost:" + Const.WEBSERVER_PORT + "/download" + calendarFileUri;
	        					
	        					return jsonResponse("message", "Confirmando").put("type", "success").put("download_link", downloadLink).create();
	        				} else {
	        					return jsonResponse("affected_events", "" + selectedEvents.size()).put("type", "success").create();
	        				}
	        			} else {
	        				// nie ma takiego zapytania dla eventów
	        			}
	        		} else {
	        			// display events
		        		String start = params.get("start");
		        		String end = params.get("end");
	        			// wyœwietl listê grup u¿ytkownika
		        		
		        		long multiplyStart = 1, multiplyEnd = 1;
		        		
		        		/**
		        		 * D³ugosc timestampa w sekundach.
		        		 */
		        		final int timestampLength = 10;
		        		
		        		/**
		        		 * Wartosc, przez któr¹ trzeba przemno¿yc sekundy,
		        		 * aby otrzymac milisekundy.
		        		 */
		        		final int multiplyMiliseconds = 1000;
		        		if (start != null && start.length() == timestampLength) {
							multiplyStart = multiplyMiliseconds;
						}
		        		if (end != null && end.length() == timestampLength) {
							multiplyEnd = multiplyMiliseconds;
						}
		        		
		        		
		        		long tStart = Long.valueOf(start) * multiplyStart;
		        		long tEnd = Long.valueOf(end) * multiplyEnd;
		        		
		        		
		        		Utils.log("start: " + tStart);
		        		Utils.log("end: " + tEnd);
		        		
		        		Utils.log("userID: " + sessionUserId);
		        		
	        			        			
	        			List<CalendarEvent> events = app.getStorage().searchEventsBetween(tStart, tEnd, sessionUserId);
	        			Utils.log("foundCount: " + events.size());
	        			Type t = new TypeToken<List<CalendarEvent>>() { }.getType();
	        			String json = app.getGson().toJson(events, t);
	        			return newFixedLengthResponse(Response.Status.OK, "text/json", json);
	        		}	 
	        	} else if ("groups".equals(requestMode)) {
	        		if (subRequestMode != null) {
	        			if ("create".equals(subRequestMode)) {
	        				String name = params.get("name");
	        				if (Utils.isGroupNameValid(name)) {		        					
		        				
		        				String color = params.get("color");
		        				int ownerId = (userSession != null) ? userSession.getUser().getId() : 1; // dla testow
		        				boolean isPublic = ("1".equals(params.get("public")));
		        				
		        				int uid = 0;
		        				if (app.getStorage() instanceof FileStorage) {
		        					uid = ((FileStorage) app.getStorage()).getHighestEventGroupId() + 1;
		        				}
		        				
		        				EventGroup group = new EventGroup(uid, name, color, ownerId, isPublic);
		        				app.getStorage().saveEventGroup(group);
		        				
		        				if (app.getStorage() instanceof FileStorage) {
		        					((FileStorage) app.getStorage()).saveEventGroups();
		        				}
		        				
		        				return jsonResponse("message", "Grupa zdarzeñ '" + name + "' zosta³a utworzona!").put("type", "success").create();
	        				} else {
	        					return jsonResponse("message", "Nazwa zawiera niedozwolone znaki.").put("type", "error").create();
	        				}
	        			} else {
	        				// nie ma takiego zapytania dla grup
	        			}
	        		} else {
	        			// wyœwietl listê grup u¿ytkownika      			
	        			List<EventGroup> groups = app.getStorage().searchEventsGroups("" + sessionUserId, "ownerId");
	        			Type t = new TypeToken<List<EventGroup>>() { }.getType();
	        			String json = app.getGson().toJson(groups, t);
	        			return newFixedLengthResponse(Response.Status.OK, "text/json", json);
	        		}
		        } else {
		        	// nie ma takiego zapytania
		        }
        	} else if (uri.startsWith("/download")) {
        		String toFind = "/download/";
        		String path = uri.substring(uri.indexOf(toFind) + toFind.length());
        		boolean matches = false;
        		for (String prefix : allowedDownloadLocations) {
        			if (path.startsWith(prefix)) {
						matches = true;
						break;
					}
        		}
        		
        		if (matches) {
        		    FileInputStream fis = null;
        		    try {
        		        fis = new FileInputStream(path + fileController.getSuffix());
        		    } catch (FileNotFoundException e) {
        		        // TODO Auto-generated catch block
        		        e.printStackTrace();
        		        return jsonResponse("type", "error").put("message", "Not found").create();
        		    }
        		    
        		    return newChunkedResponse(Status.OK, "application/octet-stream", fis);
        		    
        		} else {
        			return jsonResponse("type", "error").put("message", "Not allowed").create();
        		}
        	} else if (uri.startsWith("/dashboard")) {
        		String username = "user"; //userSession.getUser().getName();
        		int adminLevel = 1;
        		HtmlTemplate dashboardTemplate = HtmlTemplate.loadFromResource("dashboard.html@" + username); // mo¿liwoœæ cache'owania dla poszczegolnych uzytkownikow
        		dashboardTemplate.putYieldVar("current_username", username);
        		if (adminLevel > 0) {
        			dashboardTemplate.putYieldVar("is_admin", "" + adminLevel);
        		}
        		
    			return newFixedLengthResponse(dashboardTemplate.render());
        	} else if (uri.startsWith("/login")) {
        		
        		if (userSession != null) {
        			return doRedirect("dashboard");
        		}
        		
        		// logowanie
        		if (httpSession.getMethod().equals(Method.POST)) {        			
        			String username = httpSession.getParms().get("username");
        			String password = httpSession.getParms().get("password");
        			
        			User user = app.getStorage().loadUser(username);
        			if (user != null) {
        				if (user.isPasswordCorrect(password)) {
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
        	} else if (uri.startsWith("/register")) {
        		if (userSession != null) {
        			return doRedirect("dashboard");
        		}
        		
        		// rejestracja
        		
        		if (httpSession.getMethod().equals(Method.POST)) {
        			// sprawdzenie danych
        			String username = httpSession.getParms().get("username");
        			String password = httpSession.getParms().get("password");
        			String email = httpSession.getParms().get("email");
        			
        			if (username != null && password != null && email != null) {
        				if (Utils.isUsernameValid(username)) {       					
	        				
	        				int uid = 0;
	        				if (app.getStorage() instanceof FileStorage) {
	        					uid = ((FileStorage) app.getStorage()).getHighestUserId() + 1;
	        				}
	        				
	        				User user = new User(uid, username, password, email, 0);
	        				app.getStorage().storeUser(user);
	        				
	        				if (app.getStorage() instanceof FileStorage) {
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
        	} else if (uri.equals("/") || uri.contains("index.")) {
    			HtmlTemplate loginTemplate = HtmlTemplate.loadFromResource("index.html");        			
    			return newFixedLengthResponse(loginTemplate.render());
    		} else {
        		
        		String resourcePath = (wwwRootDir + uri).trim();

        		
            	resourcePath = resourcePath.replaceAll("\\|\\/", filePathSeparator);
            	resourcePath = resourcePath.substring(1);
            	String ext = Utils.getExtension(resourcePath);
            	//Utils.log("extension: " + ext);
            	
            	HashMap<String, String> mimes = new HashMap<String, String>();
            	String defaultMime = MIME_PLAINTEXT;
            	mimes.put("js", MIME_JS);
            	mimes.put("css", MIME_CSS);
            	mimes.put("png", MIME_PNG);
            	mimes.put("html", MIME_HTML);
            	
            	String mime = (mimes.containsKey(ext) ? mimes.get(ext) : defaultMime);


            	//Utils.log("resource_path: " + resource_path);
            	
            	URL resourceURL = getClass().getClassLoader().getResource(resourcePath);
        		if (Const.LOAD_FROM_DISK) {
        			URL location = HtmlTemplate.class.getProtectionDomain().getCodeSource().getLocation();
        			File directory = new File(location.getFile());
        			Utils.log(directory.getAbsolutePath());
        			File wwwDirectory = new File(directory.getAbsolutePath() + File.separator + ".." + File.separator + "www");
        			
        			resourceURL = new File(wwwDirectory, resourcePath).toURI().toURL();
        		}
            	
        		File resourceFile = new File(resourceURL.getFile());
            	if (resourceURL != null && resourceFile.exists()) {
                	InputStream is = resourceURL.openStream();
                	return newChunkedResponse(Response.Status.OK, mime, is);
            	}
        	}

    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML, "404: Resource not Found");
    }
}
