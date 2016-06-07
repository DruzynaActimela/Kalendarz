package com.actimel.calendar;

import java.io.File;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import com.actimel.calendar.impl.CSVExporter;
import com.actimel.calendar.impl.CalendarCSVImporter;
import com.actimel.calendar.impl.ICalExporter;
import com.actimel.intfs.CalendarExporter;
import com.actimel.models.CalendarEvent;
import com.actimel.utils.HtmlTemplate;
import com.actimel.utils.Utils;

/**
 * Klasa startuj�ca aplikacj�.
 * @author ActimelTeam
 *
 */
public final class Starter {
	
	/**
	 * Punkt wej�ciowy do aplikacji.
	 * @param args Lista argument�w wej�ciowych
	 */
	public static void main(final String[] args) {
		new Starter(args);
	}
	
	/**
	 * Konstruktor klasy startuj�cej.
	 * @param args Lista argument�w
	 */
	private Starter(final String[] args) {
		URL res = Starter.class.getClassLoader().getResource(File.separator);
		if (res != null) {
			try {
	            final File apps = new File(res.toURI());
	            for (File app : apps.listFiles()) {
	                System.out.println(app);
	            }
	        } catch (URISyntaxException ex) {
	        	ex.printStackTrace();
	        }
		} else {
			Utils.log("Something went wrong");
		}
	
		HtmlTemplate 
		index = HtmlTemplate.loadFromResource("dashboard.html", false);
		Utils.log("-- render -- ");
		index.putYieldVar("current_username", "test");
		//index.putYieldVar("zmienna", "wartosc zmiennej");
		String renderedTemplate = index.render();
		
		Utils.log(renderedTemplate);
		
		
		
		CalendarApp app = null;
		
		try {
			app = new CalendarApp();
			if (app.getStorage() instanceof FileStorage) {
				Utils.log("Highest Event Id: " + ((FileStorage) app.getStorage()).getHighestEventId());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Utils.log("date: " + Utils.dateToTimestamp("16-04-2016", Const.DATE_FORMAT_DAY));
		Utils.log("date with time: " + Utils.dateToTimestamp("16-04-2016 14:30", Const.DATE_FORMAT_DAY_TIME));
		
		
		List<CalendarEvent> 
		eventsToExport = app.getStorage().searchEvents("1", "ownerId");
		CalendarExporter exp = new ICalExporter();
		
		String result = exp.export(eventsToExport);
		
		File f = new File("./calendar.ics");
		Utils.writeToFile(f, result);
		
		Utils.log("iCal Export result: \n" + result);
		
	
		CalendarExporter csv = new CSVExporter();
		String csvResult = csv.export(eventsToExport);
		
		Utils.log("CSV export result: \n" + csvResult);
		
		CalendarCSVImporter csvImporter = new CalendarCSVImporter();
		csvImporter.importCSV(new StringReader(csvResult));
		
		csvResult = csv.export(csvImporter.getEvents());
		
		Utils.log("CSV export from imported result: \n" + csvResult);
	}
}
