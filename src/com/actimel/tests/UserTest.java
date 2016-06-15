package com.actimel.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.models.User;

/**
 * Test klasy u¿ytkownika.
 * @author ActimelTeam
 *
 */
public class UserTest {
	
	/**
	 * Obiekt, który bêdzie testowany.
	 */
	private static User user;

	/**
	 * Przed testem.
	 * @throws Exception wyjatek.
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		user = new User(1, "User", "Pass", "email@email.com", 1);
	}

	/**
	 * Test obiektu.
	 */
	@Test
	public final void test() {
		assertNotNull(user);
	}
	
	/**
	 * Test getterów.
	 */
	@Test
	public final void testGetters() {
		assertEquals(user.getId(), 1);
		assertEquals(user.getName(), "User");		
		assertEquals(user.getEmail(), "email@email.com");
		assertEquals(user.getPermissionLevel(), 1);
	}

	/**
	 * Test poprawnoœci has³a.
	 */
	@Test
	public final void testPassword() {
		assertTrue(user.isPasswordCorrect("Pass"));
	}
}
