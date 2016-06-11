package com.actimel.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.models.SimpleCacheEntry;
import com.actimel.utils.SimpleCache;

/**
 * Test Cache.
 * @author ActimelTeam
 *
 */
public class SimpleCacheTest {

	/**
	 * Zapis, który bêdzie testowany.
	 */
	private static SimpleCacheEntry entry;
	
	/**
	 * Obiekt cache, który zostanie przetestowany.
	 */
	private static SimpleCache cache;
	
	/**
	 * Czas ¿ycia zapisu w cache.
	 */
	private static final int COOLDOWN = 3600;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		entry = new SimpleCacheEntry("klucz", "wartosc");
		cache = new SimpleCache(COOLDOWN);
	}


	@Test
	public void testEntry() {
		assertNotNull(entry);
		assertEquals(entry.getKey(), "klucz");
		assertEquals(entry.getVal(), "wartosc");
		
	}
	
	@Test
	public void testCache() {
		assertNotNull(cache);
		
		cache.store("dane", "tekstowe");
		assertTrue(cache.hasEntry("dane"));
		assertFalse(cache.hasEntry("dane_costamasd123"));
		
		boolean expired = cache.hasEntry("dane", 0);
		
		assertFalse(expired);
		
		SimpleCacheEntry storedEntry = cache.getEntry("dane");
		assertEquals(String.valueOf(storedEntry.getVal()), "tekstowe");
		
		
	}

}
