package com.actimel.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.models.Division;

public class DivisionTest {
	
	private static Division div;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		div = new Division("1", "Nazwa", "http://link.com/");
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
	public void test() {
		assertNotNull(div);
		
		assertEquals(div.getId(), "1");
		assertEquals(div.getName(), "Nazwa");
		assertEquals(div.getLink(), "http://link.com/");
		
		
	}

}
