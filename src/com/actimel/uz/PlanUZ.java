package com.actimel.uz;

import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.actimel.calendar.CalendarApp;
import com.actimel.models.Department;
import com.actimel.models.SimpleCacheEntry;
import com.actimel.models.UzGroup;
import com.actimel.models.UzLesson;
import com.actimel.utils.SimpleCache;
import com.actimel.utils.Utils;
import com.actimel.uz.Responses.DepartmentsResponse;
import com.actimel.uz.Responses.UzGroupDaysResponse;
import com.actimel.uz.Responses.UzGroupsResponse;

/**
 * Klasa, której zadaniem jest wystawienie API do planu UZ.
 * @author ActimelTeam
 *
 */
public class PlanUZ {

	/**
	 * Endpoint API.
	 */
	public static final String API_ENDPOINT = "http://koropiecki.net/uz/plan.php";
	
	/**
	 * Maksymalny czas zycia obiektow w cache.
	 */
	public static final int CACHE_LIFETIME = 3000;

	/**
	 * Obiekt cache.
	 */
	private final SimpleCache cache;

	/**
	 * Konstruktor.
	 */
	public PlanUZ() {
		cache = new SimpleCache(CACHE_LIFETIME);
	}	

	/**
	 * Metoda umo¿liwiaj¹ca pobranie listy wydzia³ów i kierunków.
	 * @return Lista wydzia³ów i kierunków.
	 */
	public final List<Department> getDepartments() {
		String response = syncHttpRequest("m=get_divisions");
		DepartmentsResponse resp = CalendarApp.getInstance().getGson().fromJson(response, DepartmentsResponse.class);
		return resp.data;
		
	}

	/**
	 * Metoda umo¿liwiaj¹ca pobranie listy grup.
	 * @param divId ID grupy dla której nale¿y pobrac liste grup.
	 * @return Lista grup
	 */
	public final List<UzGroup> getGroupsForDepartment(final int divId) {
		String response = syncHttpRequest("m=get_groups&div=" + divId);
		UzGroupsResponse resp = CalendarApp.getInstance().getGson().fromJson(response, UzGroupsResponse.class);
		return resp.data;
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca pobranie planu lekcji dla grupy.
	 * @param groupId ID grupy
	 * @return Plan lekcji
	 */
	public final HashMap<Integer, List<UzLesson>> getDaysForGroup(final int groupId) {
		String response = syncHttpRequest("m=get_plan&group=" + groupId);
		UzGroupDaysResponse resp = CalendarApp.getInstance().getGson().fromJson(response, UzGroupDaysResponse.class);
		return resp.data;
	}
	
	

	/**
	 * Metoda wysy³aj¹ca synchroniczne zapytanie HTTP na podany adres.
	 * @param params Parametry
	 * @return Odpowiedz serwera
	 */
	private String syncHttpRequest(final String params) {
		if (cache.hasEntry(params)) {
			SimpleCacheEntry e = cache.getEntry(params);
			return String.valueOf(e.getVal());
		}
		
		HttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(API_ENDPOINT + "?" + params);

            Utils.log("Executing request " + httpget.getRequestLine());
            /*
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    final int statusOk = 200, statusRedir = 300;
                    
                    if (status >= statusOk && status < statusRedir) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            */
            HttpResponse response = httpclient.execute(httpget);
            String responseBody = EntityUtils.toString(response.getEntity());
            Utils.log("----------------------------------------");
            Utils.log(responseBody);
            
            cache.store(params, responseBody);

            return responseBody;
            
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
	}
}