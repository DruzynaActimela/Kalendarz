package com.actimel.calendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.actimel.intfs.StorageIntf;
import com.actimel.models.CalendarEvent;

public class FileStorage implements StorageIntf {
	
	private File storageFile;
	private CalendarApp app;
	
	public FileStorage(CalendarApp appInstance, String path) throws FileNotFoundException {
		this.app = appInstance;
		storageFile = new File(path);
		if(!storageFile.exists()) {
			throw new FileNotFoundException("Plik " + storageFile.getAbsolutePath() + " nie istnieje!");
		}
		
	}
	
	@Override
	public List<CalendarEvent> loadEvents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveEvents(List<CalendarEvent> events) {
		// TODO Auto-generated method stub
		
	}

}
