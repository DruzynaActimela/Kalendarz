package com.actimel.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.actimel.models.DayOfWeek;

/**
 * Klasa testujaca poprawnosc enuma z dniami tygodnia.
 * @author ActimelTeam
 *
 */
public class DayOfWeekTest {

	/**
	 * Test DayOfWeek.
	 */
	@Test
	public final void test() {
		
		final int idSroda = 3, idCzwartek = 4, idPiatek = 5, idSobota = 6;
		
		assertEquals(DayOfWeek.MONDAY.getId(), 1);
		assertEquals(DayOfWeek.TUESDAY.getId(), 2);
		assertEquals(DayOfWeek.WEDNESDAY.getId(), idSroda);
		assertEquals(DayOfWeek.THURSDAY.getId(), idCzwartek);
		assertEquals(DayOfWeek.FRIDAY.getId(), idPiatek);
		assertEquals(DayOfWeek.SATURDAY.getId(), idSobota);
		assertEquals(DayOfWeek.SUNDAY.getId(), 0);
		
		
	}

}
