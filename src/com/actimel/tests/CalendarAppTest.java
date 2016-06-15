package com.actimel.tests;

import org.junit.Test;

import com.actimel.calendar.CalendarApp;
import static org.junit.Assert.*;

/**
 * Test sprawdzaj�cy poprawnosc klasy aplikacji.
 * @author ActimelTeam
 *
 */
public class CalendarAppTest {


	/**
	 * Test sprawdaj�cy podstawowe za�o�enia aplikacji.
	 */
	@Test
	public final void testApp() {
		CalendarApp app = new CalendarApp();
		
		assertNotNull(app);		
		assertNotNull(app.getGson());
		assertNotNull(app.getStorage());
		assertNotNull(app.getUZApi());
		
		assertEquals(CalendarApp.getInstance(), app);
		
	}

}
