package com.actimel.controllers;

import java.io.File;

import com.actimel.utils.Utils;

/**
 * Klasa odpowiadaj�ca za zarz�dzanie plikami w okre�lonym folderze.
 * @author ActimelTeam
 *
 */
public class FileController {
	
	/**
	 * Sta�a wartosc okre�laj�ca typ linku jako zdalny. (http://)
	 */
	public static final int URI_REMOTE = 7;
	
	/**
	 * Sta�a wartosc okre�laj�ca typ linku jako lokalny. (file://)
	 */
	public static final int URI_LOCAL = 9;
	
	/**
	 * Obiekt foldera, w kt�rym b�d� przechowywane pliki.
	 * @uml.property  name="store"
	 */
	private File store;
	
	/**
	 * Suffix nazwy pliku przechowywanego w store.
	 * @uml.property  name="filenameSuffix"
	 */
	private String filenameSuffix = "";
	
	/**
	 * Konstruktor przyjmuj�cy �cie�k� foldera, 
	 * w kt�rym b�d� przechowywane pliki.
	 * @param storePath �cie�ka foldera
	 * @throws Exception 
	 */
	public FileController(final String storePath) throws Exception {
		store = new File(storePath);
		store.mkdirs();
		if (!store.isDirectory()) {
			throw new Exception(store.getAbsolutePath() + " nie jest folderem!");
			
		}

	}
	
	/**
	 * Metoda umo�liwiaj�ca zapisywanie danych do pliku.
	 * @param content Dane do zapisania
	 * @param filename Nazwa pliku
	 * @param returnUriType Typ �cie�ki zwrotnej, URI_REMOTE lub URI_LOCAL
	 * @return �cie�ka do pliku
	 */
	public final String storeContent(final String content, final String filename, final int returnUriType) {
		String contentFilename = filename + filenameSuffix;
		File file = new File(store, contentFilename);
		
		Utils.writeToFile(file, content);
		
		if (returnUriType == URI_REMOTE) {
			return "/" + store.getName() + "/" + filename;
		}
		return file.getAbsolutePath();
	}
	
	
	
	/**
	 * Metoda zwracaj�ca suffix nazw plik�w.
	 * @return Suffix nazwy jako String.
	 */
	public final String getSuffix() {
		return filenameSuffix;
	}
}
