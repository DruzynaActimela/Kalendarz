package com.actimel.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.models.EventGroup;

/**
 * Klasa testuj�ca klas� grupy event�w.
 * @author 4i60r
 *
 */
public class EventGroupTest {

	
	/**
	 * Obiekt, kt�ry jest testowany.
	 */
	private static EventGroup group;
	
	
	/**
	 * Przygotowanie.
	 * @throws Exception wyj�tek.
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		group = new EventGroup(1, "Nazwa", "#000", 1, true);
	}
	
	/**
	 * Test klasy.
	 */
	@Test
	public final void testEventGroup() {
		assertNotNull(group);
	}
	
	/**
	 * Test getter�w.
	 */
	@Test
	public final void testEventGroupGetters() {
		assertEquals(group.getId(), 1);
		assertEquals(group.getName(), "Nazwa");
		assertEquals(group.getColor(), "#000");
		assertEquals(group.getOwnerId(), 1);
		assertTrue(group.isPublic());
		
		
	}

}
