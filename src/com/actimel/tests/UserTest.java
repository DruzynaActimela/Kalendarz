package com.actimel.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.models.User;

public class UserTest {
	
	private static User user;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		user = new User(1, "User", "Pass", "email@email.com", 1);
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
		assertNotNull(user);
	}
	
	@Test
	public void testGetters() {
		assertEquals(user.getId(), 1);
		assertEquals(user.getName(), "User");		
		assertEquals(user.getEmail(), "email@email.com");
		assertEquals(user.getPermissionLevel(), 1);
	}

	@Test
	public void testPassword() {
		assertTrue(user.isPasswordCorrect("Pass"));
	}
}
