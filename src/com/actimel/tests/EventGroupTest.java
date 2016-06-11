package com.actimel.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.models.EventGroup;

public class EventGroupTest {

	
	private static EventGroup group;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		group = new EventGroup(1, "Nazwa", "#000", 1, true);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEventGroup() {
		assertNotNull(group);
	}
	
	@Test
	public void testEventGroupGetters() {
		assertEquals(group.getId(), 1);
		assertEquals(group.getName(), "Nazwa");
		assertEquals(group.getColor(), "#000");
		assertEquals(group.getOwnerId(), 1);
		assertTrue(group.isPublic());
		
		
	}

}
