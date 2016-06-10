package com.actimel.controllers;

import java.io.File;

import com.actimel.utils.Utils;

/**
 * Klasa odpowiadaj¹ca za zarz¹dzanie plikami w okreœlonym folderze.
 * @author ActimelTeam
 *
 */
public class FileController {
	
	/**
	 * Sta³a wartosc okreœlaj¹ca typ linku jako zdalny. (http://)
	 */
	public static final int URI_REMOTE = 7;
	
	/**
	 * Sta³a wartosc okreœlaj¹ca typ linku jako lokalny. (file://)
	 */
	public static final int URI_LOCAL = 9;
	
	/**
	 * Obiekt foldera, w którym bêd¹ przechowywane pliki.
	 * @uml.property  name="store"
	 */
	private File store;
	
	/**
	 * Suffix nazwy pliku przechowywanego w store.
	 * @uml.property  name="filenameSuffix"
	 */
	private String filenameSuffix = "";
	
	/**
	 * Konstruktor przyjmuj¹cy œcie¿kê foldera, 
	 * w którym bêd¹ przechowywane pliki.
	 * @param storePath Œcie¿ka foldera
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
	 * Metoda umo¿liwiaj¹ca zapisywanie danych do pliku.
	 * @param content Dane do zapisania
	 * @param filename Nazwa pliku
	 * @param returnUriType Typ œcie¿ki zwrotnej, URI_REMOTE lub URI_LOCAL
	 * @return Œcie¿ka do pliku
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
	 * Metoda zwracaj¹ca suffix nazw plików.
	 * @return Suffix nazwy jako String.
	 */
	public final String getSuffix() {
		return filenameSuffix;
	}
}
