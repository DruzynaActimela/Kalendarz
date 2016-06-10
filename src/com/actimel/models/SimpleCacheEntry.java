package com.actimel.models;

import com.actimel.utils.Utils;

/**
 * Klasa reprezentuj¹ca obiekt w cache.
 * @author ActimelTeam
 *
 */
public class SimpleCacheEntry {
	
	/**
	 * Klucz.
	 */
	private String key;
	
	/**
	 * Wartosc.
	 */
	private Object val;
	
	/**
	 * Timestamp umieszczenia w cache.
	 */
	private long storeTime;

	
	/**
	 * Konstrutor.
	 * @param keyRef Klucz
	 * @param valRef Wartosc
	 */
	public SimpleCacheEntry(final String keyRef, final Object valRef) {
		super();
		this.key = keyRef;
		this.val = valRef;
		this.storeTime = Utils.timestamp();
	}

	/**
	 * Getter klucza.
	 * @return Klucz
	 */
	public final String getKey() {
		return key;
	}

	/**
	 * Getter wartosci.
	 * @return Wartosc
	 */
	public final Object getVal() {
		return val;
	}

	/**
	 * Setter wartosci.
	 * @param valRef Nowa wartosc
	 */
	public final void setVal(final Object valRef) {
		this.storeTime = Utils.timestamp();
		this.val = valRef;
	}

	/**
	 * Getter timestampu dodania.
	 * @return Timestamp dodania do cache
	 */
	public final long getStoreTime() {
		return storeTime;
	}

	/**
	 * Setter timestampa dodania do cache.
	 * @param storeTimeRef Nowy timestamp
	 */
	public final void setStoreTime(final long storeTimeRef) {
		this.storeTime = storeTimeRef;
	}
	
	/**
	 * Metoda sprawdzaj¹ca, czy czas tego obiektu up³yn¹³.
	 * @param maxCooldownTime Czas przez który obiekt moze istniec
	 * @return true/false w zale¿noœci od tego, czy czas up³yn¹³
	 */
	public final boolean isAlive(final int maxCooldownTime) {
		return (storeTime + maxCooldownTime) > Utils.timestamp();
	}
	
}
