package com.actimel.tests;

import static org.junit.Assert.*;
import org.junit.Test;

import com.actimel.calendar.CalendarApp;
import com.actimel.utils.JsonResponseBuilder;
import fi.iki.elonen.NanoHTTPD;


/**
 * Klasa testujaca builder odpowiedzi JSON.
 * @author ActimelTeam
 *
 */
public class JsonResponseBuilderTest {


	/**
	 * Test.
	 */
	@Test
	public final void test() {
		CalendarApp app = new CalendarApp();
		
		JsonResponseBuilder builder = new JsonResponseBuilder(app, "klucz", "wartosc");
		builder.put("druga", "wartosc2");
		
		final String expectedResult = "{\"druga\":\"wartosc2\",\"klucz\":\"wartosc\"}";
		
		NanoHTTPD.Response response = builder.create();
		
		assertEquals(response.getMimeType(), "text/json");
		assertEquals(response.getStatus(), NanoHTTPD.Response.Status.OK);
		
		assertEquals(builder.toJson(), expectedResult);
	}

}
