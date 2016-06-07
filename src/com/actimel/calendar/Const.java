package com.actimel.calendar;

/**
 * Klasa przechowuj¹ca wszystkie sta³e wartoœci.
 */

public final class Const {
	/**
	 * Port serwera WWW.
	 */
	public static final int WEBSERVER_PORT = 9090;
	
	/**
	 * Lokalizacja pliku, w którym przechowywane s¹ grupy zdarzeñ, 
	 * jeœli typ storage to FileStorage.
	 */
	public static final String 
	EVENTS_GROUPS_STORAGE_PATH = "events-groups-storage.json";
	
	/**
	 * Lokalizacja pliku, w którym przechowywane s¹ zdarzenia, 
	 * jeœli typ storage to FileStorage.
	 */
	public static final String EVENTS_STORAGE_PATH = "events-storage.json";
	
	/**
	 * Lokalizacja pliku, w którym przechowywani s¹ u¿ytkownicy, 
	 * jeœli typ storage to FileStorage.
	 */
	public static final String USERS_STORAGE_PATH = "users-storage.json";

	/**
	 * Lokalizacja foldera, w który przechowywane bêd¹ pliki.
	 */
	public static final String FILE_STORE_FOLDER = "./file_storage";
	
	/**
	 * Nazwa ciastka z kluczem sesji.
	 */
	public static final String COOKIE_SESSION_KEY = "_CalendarAppSessId";
	
	/**
	 * Iloœ dni, przez które sesja jest wa¿na.
	 */
	public static final int COOKIE_SESSION_LIFETIME = 3; //days
	
	/**
	 * Czy ³adowac templatki z dysku?
	 */
	public static final boolean LOAD_FROM_DISK = true;
	
	/**
	 * Czy wy³¹czyc cache?
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
