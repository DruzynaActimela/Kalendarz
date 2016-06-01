package com.actimel.calendar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.actimel.calendar.impl.WebServer;
import com.actimel.intfs.StorageIntf;
import com.actimel.models.CalendarEvent;
import com.actimel.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.iki.elonen.NanoHTTPD;


public class CalendarApp {
	
	private Gson gson;
	private WebServer webServer;
	private StorageIntf storage;

	public CalendarApp() {
		gson = new GsonBuilder().disableHtmlEscaping().create();
		
		storage = new FileStorage(this, Const.EVENTS_STORAGE_PATH, Const.EVENTS_GROUPS_STORAGE_PATH, Const.USERS_STORAGE_PATH);
		webServer = new WebServer(this, 9090);
		
        try {
        	webServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
            System.exit(-1);
        }
        
        if(storage instanceof FileStorage) {
        	FileStorage fs = (FileStorage) storage;
        	fs.preloadEvents();
        	fs.preloadUsers();
        	fs.preloadEventGroups();
        }
        
        Utils.log("CalendarApp :: init success");
		
	}
	
	public Gson getGson() {
		return gson;
	}
	
	public StorageIntf getStorage() {
		return storage;
	}
}
