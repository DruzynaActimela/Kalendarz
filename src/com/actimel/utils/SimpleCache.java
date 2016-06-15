package com.actimel.utils;

import java.util.HashMap;


import com.actimel.models.SimpleCacheEntry;

/**
 * Klasa, której zadaniem jest czasowe przechowywanie danych.
 * @author ActimelTeam
 *
 */
public class SimpleCache {

	
	/**
	 * Przechowywane wartoœci.
	 */
	private final HashMap<String, SimpleCacheEntry> entries = new HashMap<String, SimpleCacheEntry>();
	
	/**
	 * Maksymalny czas ¿ycia wartoœci.
	 */
	private final int entryLifetime;
	
	/**
	 * Konstruktor.
	 * @param entryLifetimeRef Maksymalny czas ¿ycia wartoœci w tym obiekcie cache.
	 */
	public SimpleCache(final int entryLifetimeRef) {
		entryLifetime = entryLifetimeRef;
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca przechowanie danych w tym obiekcie.
	 * @param key Klucz
	 * @param val Wartosc
	 * @return Obiekt przechowywanej wartosci
	 */
	public final SimpleCacheEntry store(final String key, final String val) {
		SimpleCacheEntry sce = new SimpleCacheEntry(key, val);
		entries.put(key, sce);
		return sce;
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca sprawdzenie, czy obiekt o podanym kluczu znajduje siê w tym obiekcie cache.
	 * @param key Klucz
	 * @return true/false
	 */
	public final boolean hasEntry(final String key) {
		 return hasEntry(key, entryLifetime);
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca sprawdzenie, czy obiekt o podanym kluczu znajduje siê w tym obiekcie cache.
	 * @param key Klucz
	 * @param customCooldown Customowy cooldown
	 * @return true/false
	 */
	public final boolean hasEntry(final String key, final int customCooldown) {
		 if (entries.containsKey(key)) {
			 SimpleCacheEntry sce = entries.get(key);
			 return sce.isAlive(customCooldown);
		 }
		 return false;
	}
	

	/**
	 * Metoda umo¿liwiaj¹ca pobranie obiektu o podanym kluczu.
	 * @param key Klucz
	 * @return Obiekt wartosci
	 */
	public final SimpleCacheEntry getEntry(final String key) {
		if (hasEntry(key)) {
			return entries.get(key);
		}		
		return null;
	}
}
