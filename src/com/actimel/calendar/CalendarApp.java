package com.actimel.calendar;

import java.io.IOException;

import com.actimel.calendar.impl.WebServer;
import com.actimel.intfs.StorageIntf;
import com.actimel.utils.Utils;
import com.actimel.uz.PlanUZ;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.iki.elonen.NanoHTTPD;

/**
 * G��wna klasa aplikacji.
 */
public class CalendarApp {
	
	/**
	 * Kontekst aplikacji.
	 */
	private static CalendarApp appContext;
	
	/**
	 * Obiekt GSON, odpowiedzialny za serializacj� danych do/z formatu JSON.
	 * @uml.property  name="gson"
	 * @uml.associationEnd  
	 */
	private final Gson gson;
	
	/**
	 * Obiekt serwera WWW.
	 * @uml.property  name="webServer"
	 * @uml.associationEnd  inverse="app:com.actimel.calendar.impl.WebServer"
	 */
	private final WebServer webServer;
	
	/**
	 * Obiekt storage odpowiedzialny za I/O danych.
	 * @uml.property  name="storage"
	 * @uml.associationEnd  
	 */
	private final StorageIntf storage;

	
	/**
	 * Obiekt planu UZ.
	 */
	private final PlanUZ uzApi;
	
	/**
	 * Funkcja umo�liwiaj�ca pobranie kontekstu aplikacji.
	 * @return Kontekst aplikacji
	 */
	public static CalendarApp getInstance() {
		return appContext;
	}
	
	/** 
	 * Konstruktor aplikacji.
	 */
	public CalendarApp() {
		CalendarApp.appContext = this;
		
		gson = new GsonBuilder().disableHtmlEscaping().create();
		
		uzApi = new PlanUZ();
		
		storage = new FileStorage(this, 
								  Const.EVENTS_STORAGE_PATH, 
								  Const.EVENTS_GROUPS_STORAGE_PATH, 
								  Const.USERS_STORAGE_PATH);
		
		webServer = new WebServer(this, Const.WEBSERVER_PORT);
		
        try {
        	webServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException ioe) {
            Utils.log("Couldn't start server:\n" + ioe);
            //System.exit(-1);
        }
        
        if (storage instanceof FileStorage) {
        	final FileStorage fstorage = (FileStorage) storage;
        	fstorage.preloadEvents();
        	fstorage.preloadUsers();
        	fstorage.preloadEventGroups();
        }
        
        Utils.log("CalendarApp :: init success");
		
	}
	
	/**
	 * Metoda s�u��ca do pobierania obiektu GSON.
	 * @return  obiekt GSON
	 * @uml.property  name="gson"
	 */
	public final Gson getGson() {
		return gson;
	}
	
	/**
	 * Metoda s�u��ca do pobierania obiektu PlanUZ.
	 * @return obiekt PlanUZ
	 */
	public final PlanUZ getUZApi() {
		return uzApi;
	}
	
	/**
	 * Metoda s�u��ca do pobrania obiektu Storage.
	 * @return  obiekt storage
	 * @uml.property  name="storage"
	 */
	public final StorageIntf getStorage() {
		return storage;
	}
}
