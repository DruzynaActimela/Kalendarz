package com.actimel.uz;

import java.util.HashMap;
import java.util.List;

import com.actimel.models.Department;
import com.actimel.models.UzGroup;
import com.actimel.models.UzLesson;

/**
 * Klasa skupiaj¹ca w sobie odpowiedzi API Planu UZ.
 * @author ActimelTeam
 *
 */

public class Responses {
	
	/**
	 * Model podstawowej odpowiedzi.
	 * @author ActimelTeam
	 *
	 */
	public class JsonResponse {
		
		/**
		 * Typ odpowiedzi. error lub success
		 */
		String type;
		
		/**
		 * Tresc wiadomoœci pozytywnej.
		 */
		String success;
		
		/**
		 * Tresc wiadomoœci negatywnej.
		 */
		String error;
		
	}
	
	/**
	 * Model odpowiedzi z list¹ wydzia³ów.
	 * @author ActimelTeam
	 *
	 */
	public class DepartmentsResponse extends JsonResponse {
		
		/**
		 * Lista wydzia³ów.
		 */
		List<Department> data;
	}
	
	/**
	 * Model odpowiedzi z list¹ grup.
	 * @author ActimelTeam
	 *
	 */
	public class UzGroupsResponse extends JsonResponse {
		
		/**
		 * Lista grup.
		 */
		List<UzGroup> data;
	}
	
	/**
	 * Model odpowiedzi z list¹ lekcji dla grupy.
	 * @author ActimelTeam
	 *
	 */
	public class UzGroupDaysResponse extends JsonResponse {
		
		/**
		 * Lista lekcji.
		 */
		HashMap<Integer, List<UzLesson>> data;
	}
	
	
}
