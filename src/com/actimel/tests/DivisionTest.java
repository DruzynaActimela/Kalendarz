package com.actimel.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.models.Division;

/**
 * Test klasy reprezentuj¹cej kierunek.
 * @author ActimelTeam
 *
 */
public class DivisionTest {
	
	/**
	 * Obiekt kierunku.
	 */
	private static Division div;

	/**
	 * Przygotowanie.
	 * @throws Exception wyj¹tek
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		div = new Division("1", "Nazwa", "http://link.com/");
	}

	/**
	 * Test klasy.
	 */
	@Test
	public final void test() {
		assertNotNull(div);
		
		assertEquals(div.getId(), "1");
		assertEquals(div.getName(), "Nazwa");
		assertEquals(div.getLink(), "http://link.com/");
		
		
	}

}
