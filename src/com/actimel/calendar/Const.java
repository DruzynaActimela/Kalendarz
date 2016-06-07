package com.actimel.calendar;

/**
 * Klasa przechowuj�ca wszystkie sta�e warto�ci.
 */

public final class Const {
	/**
	 * Port serwera WWW.
	 */
	public static final int WEBSERVER_PORT = 9090;
	
	/**
	 * Lokalizacja pliku, w kt�rym przechowywane s� grupy zdarze�, 
	 * je�li typ storage to FileStorage.
	 */
	public static final String 
	EVENTS_GROUPS_STORAGE_PATH = "events-groups-storage.json";
	
	/**
	 * Lokalizacja pliku, w kt�rym przechowywane s� zdarzenia, 
	 * je�li typ storage to FileStorage.
	 */
	public static final String EVENTS_STORAGE_PATH = "events-storage.json";
	
	/**
	 * Lokalizacja pliku, w kt�rym przechowywani s� u�ytkownicy, 
	 * je�li typ storage to FileStorage.
	 */
	public static final String USERS_STORAGE_PATH = "users-storage.json";

	/**
	 * Lokalizacja foldera, w kt�ry przechowywane b�d� pliki.
	 */
	public static final String FILE_STORE_FOLDER = "./file_storage";
	
	/**
	 * Nazwa ciastka z kluczem sesji.
	 */
	public static final String COOKIE_SESSION_KEY = "_CalendarAppSessId";
	
	/**
	 * Ilo� dni, przez kt�re sesja jest wa�na.
	 */
	public static final int COOKIE_SESSION_LIFETIME = 3; //days
	
	/**
	 * Czy �adowac templatki z dysku?
	 */
	public static final boolean LOAD_FROM_DISK = true;
	
	/**
	 * Czy wy��czyc cache?
	 */
	public static final boolean FORCE_DISABLE_CACHE = true;
	
	/**
	 * Format daty dnia.
	 */
	public static final String DATE_FORMAT_DAY = "dd-MM-yyyy";
	
	/**
	 * Format daty dnia i godziny.
	 */
	public static final String 
	DATE_FORMAT_DAY_TIME = DATE_FORMAT_DAY + " HH:mm";
	
	/**
	 * Checkstyler wie lepiej.
	 */
	private Const() {
		super();
	}
}
