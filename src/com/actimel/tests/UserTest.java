package com.actimel.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.models.User;

/**
 * Test klasy u�ytkownika.
 * @author ActimelTeam
 *
 */
public class UserTest {
	
	/**
	 * Obiekt, kt�ry b�dzie testowany.
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
	 * Test getter�w.
	 */
	@Test
	public final void testGetters() {
		assertEquals(user.getId(), 1);
		assertEquals(user.getName(), "User");		
		assertEquals(user.getEmail(), "email@email.com");
		assertEquals(user.getPermissionLevel(), 1);
	}

	/**
	 * Test poprawno�ci has�a.
	 */
	@Test
	public final void testPassword() {
		assertTrue(user.isPasswordCorrect("Pass"));
	}
}
