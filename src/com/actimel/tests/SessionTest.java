package com.actimel.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.actimel.controllers.SessionController;
import com.actimel.models.Session;
import com.actimel.models.User;

/**
 * Test sesji.
 * @author ActimelTeam
 *
 */
public class SessionTest {

	/**
	 * Obiekt sesji, który zostanie przetestowany.
	 */
	private static Session sessionObj;
	
	/**
	 * Kontroler sesji, który zostanie przetestowany.
	 */
	private static SessionController controller;
	
	/**
	 * U¿ytkownik, który bêdzie u¿ywany do testowania.
	 */
	private static User user;
	
	/**
	 * Testowe IP.
	 */
	private static String testIp = "127.0.0.1";
	
	/**
	 * Przygotowanie do testowania.
	 * @throws Exception wyj¹tek
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sessionObj = new Session(null, "key");
				
		controller = new SessionController();
		user = new User(1, "User", "Pass", "email@email.com", 1);
	}

	
	/**
	 * Ogólny test klasy Session.
	 */
	@Test
	public final void testSessionObject() {
		assertNull(sessionObj.getUser());
		assertEquals(sessionObj.getKey(), "key");
		assertEquals(sessionObj.getUserId(), 0); // user == null, wiêc ID = 0
	}

	/**
	 * Test tworzenia sesji, operowania na kontrolerze i obiekcie sesji. 
	 */
	@Test
	public final void testCreatingSession() {
		Session session = controller.createSession(user, testIp);
		assertNotNull(session);
		assertEquals(session.getUserId(), 1);
		assertEquals(session.getUser(), user);
	}
	
	/**
	 * Test szukania sesji po kluczu.
	 */
	@Test
	public final void testSearchingSession() {
		Session session = controller.createSession(user, testIp);
		String keySearch = session.getKey();
		
		Session searchResultSession = controller.getSessionByKey(keySearch);
		Session searchResultWrong = controller.getSessionByKey("wrongKey");
		assertNotNull(searchResultSession);
		assertNull(searchResultWrong);
	}
	
	/**
	 * Test usuwania sesji.
	 */
	@Test
	public final void testRemovingSession() {
		Session session = controller.createSession(user, testIp);
		String keySearch = session.getKey();
		
		assertTrue(controller.destroySession(keySearch));
		assertFalse(controller.destroySession("wrongSessionKey"));
	}
	
	/**
	 * Test walidacji klucza sesji dla przypadku poprawnych i niepoprawnych danych.
	 */
	@Test
	public final void testValidatingSession() {
		Session session = controller.createSession(user, testIp);
		String keySearch = session.getKey();
		

		boolean resultRight = controller.isSessionKeyValidForUser(keySearch, user, testIp);
		boolean resultWrong = controller.isSessionKeyValidForUser(keySearch + "asdwe123", user, testIp + "hehe");
		
		assertTrue(resultRight);
		assertFalse(resultWrong);
	}
	
	/**
	 * Test tworzenia klucza sesji.
	 */
	@Test
	public final void testSessionKeyCreation() {
		String key = SessionController.createSessionKey("a", "b", "c");
		assertNotNull(key);
	}
	
	
	
	
	

}
