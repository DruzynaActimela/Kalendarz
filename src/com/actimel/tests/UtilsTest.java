package com.actimel.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.actimel.calendar.Const;
import com.actimel.utils.Utils;


/**
 * Testy zwi¹zane z klas¹ Utils.
 * @author ActimelTeam
 *
 */
public class UtilsTest {
	

	/**
	 * Test zapisu i odczytu z pliku.
	 */
	@Test
	public final void testFileWriteAndRead() {
		String content = "contentToWrite";
		
		File fileToWrite = new File("randomTestFile.txt");		
		Utils.writeToFile(fileToWrite, content);		
		
		String fileContent = Utils.readFile(fileToWrite);
		
		fileToWrite.delete();		
		assertNotNull(fileContent);		
		
		fileContent = fileContent.trim();		
		assertTrue(content.equals(fileContent));
	}
	
	/**
	 * Test funkcji generuj¹cej pseudolosowe wartoœci z zakresu.
	 */
	@Test
	public final void testRandom() {
		final int min = 10;
		final int max = 20;
		int randResult = Utils.rand(min, max);
		
		assertTrue(randResult >= min && randResult <= max);
	}
	
	/**
	 * Test funkcji pobieraj¹cej rozszerzenie z nazwy.
	 */
	@Test
	public final void testFileExtension() {
		String path = "path/to/file/with.ext";
		
		assertEquals(Utils.getExtension(path), "ext");
		
	}
	
	/**
	 * Test funkcji waliduj¹cych tekst.
	 */
	@Test
	public final void testValidationFunctions() {
		String validUsername = "username";
		String invalidUsername = "<script src=''>";
		
		String validGroupName = "Moja grupa";
		String invalidGroupName = "<XSS attack, witam!";
		
		assertTrue(Utils.isUsernameValid(validUsername));
		assertFalse(Utils.isUsernameValid(invalidUsername));
		
		assertTrue(Utils.isGroupNameValid(validGroupName));
		assertFalse(Utils.isGroupNameValid(invalidGroupName));

	}
	
	/**
	 * Test funkcji konwertuj¹cych.
	 */
	@Test
	public final void testConvertingFunctions() {
		String dateFormat = Const.DATE_FORMAT_DAY_TIME;
		String dateToTest = "12-12-2016 13:30";
		
		String wrongDate = "13-14-15 lol";
		
		long rightStamp = Utils.dateToTimestamp(dateToTest, dateFormat);
		long wrongStamp = Utils.dateToTimestamp(wrongDate, dateFormat);
		
		assertTrue(rightStamp > 0);
		assertTrue(wrongStamp == 0);

		String rightDateFromStamp = Utils.dateFromTimestamp(rightStamp, dateFormat);
		assertEquals(rightDateFromStamp, dateToTest);
		
		
		String intStr = "2";
		assertTrue(Utils.parseInt(intStr, -1) == 2);
		assertTrue(Utils.parseInt("b³¹d600", -1) == -1);
		
		String floatStr = "2";
		assertTrue(Utils.parseFloat(floatStr, -1f) == 2f);
		assertTrue(Utils.parseFloat("b³¹d600", -1f) == -1f);
		
		String longStr = "2";
		assertTrue(Utils.parseLong(longStr, -1L) == 2L);
		assertTrue(Utils.parseLong("b³¹d600", -1L) == -1L);
		
		
		String startDate = "12-12-2016 13:30";
		String endDate = "12-12-2016 14:30";
		final long expectedDuration = 3600;
		long dur = Utils.calculateDuration(startDate, endDate, dateFormat);
		
		assertEquals(dur, expectedDuration);
		
		String strTime = "1:30";
		final long expectedLongTime = 90;
		
		assertEquals(Utils.strTimeToLong(strTime), expectedLongTime);
		
		
	
	}

}
