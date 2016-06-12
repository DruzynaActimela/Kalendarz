package com.actimel.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.actimel.calendar.CalendarApp;
import com.actimel.models.Department;
import com.actimel.models.UzGroup;
import com.actimel.models.UzLesson;
import com.actimel.uz.PlanUZ;

/**
 * Test API planu UZ.
 * @author ActimelTeam
 *
 */
public class PlanUZApiTest {

	/**
	 * Kontekst aplikacji.
	 */
	private static CalendarApp app = new CalendarApp();
	
	/**
	 * Obiekt API planu UZ.
	 */
	private static PlanUZ api = new PlanUZ(); 


	/**
	 * Test pobierania wydzia³ów.
	 */
	@Test
	public final void testApi() {
	
		final int minimumDeptSizeToAssumeItsWorking = 10;
		final int informatykaId = 401;
		final int inf23spId = 16671;
		List<Department> depts = api.getDepartments();
		
		assertNotNull(depts);		
		assertTrue(depts.size() > minimumDeptSizeToAssumeItsWorking);
		
		List<UzGroup> groups = api.getGroupsForDepartment(informatykaId);
		assertNotNull(groups);
		assertTrue(depts.size() > minimumDeptSizeToAssumeItsWorking);
		
		HashMap<Integer, List<UzLesson>> days = api.getDaysForGroup(inf23spId);
		assertNotNull(days);
		assertTrue(days.size() > 2);
		
		
		
	}

	
}
