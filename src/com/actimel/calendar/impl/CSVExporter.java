package com.actimel.calendar.impl;

import java.util.ArrayList;
import java.util.List;

import com.actimel.calendar.CalendarApp;
import com.actimel.calendar.Const;
import com.actimel.intfs.CalendarExporter;
import com.actimel.models.CalendarEvent;
import com.actimel.models.User;
import com.actimel.utils.Utils;

/**
 * Klasa odpowiadaj¹ca za eksport listy zdarzeñ do formatu CSV.
 * @author ActimelTeam
 *
 */
public class CSVExporter extends CalendarExporter {
	

	@Override
	public final String export(final List<CalendarEvent> events) {
		String lineEnd = "\n";
		String header = "\"ID\",\"Tytu³\",\"Data Pocz¹tkowa\",\"Data Koñcowa\",\"E-Mail W³aœciciela\",\"Kolor\"";
		List<String> lines = new ArrayList<String>();
		
		for (CalendarEvent calendarEvent : events) {
			User owner = CalendarApp.getInstance().getStorage().loadUser(calendarEvent.getOwnerId());
			String ownerEmail = "";
			if (owner != null) {
				ownerEmail = owner.getEmail();
			}
			String start = Utils.dateFromTimestamp(calendarEvent.getStampStart(), Const.DATE_FORMAT_DAY_TIME);
			String end = "";
			
			if (calendarEvent.getStampEnd() > 0) {
				end = Utils.dateFromTimestamp(calendarEvent.getStampEnd(), Const.DATE_FORMAT_DAY_TIME);
			}
			
			String groupColor = "";
			if (calendarEvent.hasEventGroup()) {
				groupColor = calendarEvent.getEventGroup().getColor();
			}
			
			StringBuilder line = new StringBuilder();
			line.append("\"" + calendarEvent.getId() + "\"" + ",");
			line.append("\"" + calendarEvent.getName() + "\"" + ",");
			line.append("\"" + start + "\"" + ",");
			line.append("\"" + end + "\"" + ",");
			line.append("\"" + ownerEmail + "\"" + ",");
			line.append("\"" + groupColor + "\"");	
			
			
			
			lines.add(line.toString());
		}
		
		
		
		
		
		StringBuilder builder = new StringBuilder();

		
		builder.append(header + lineEnd);
		
		for (String line : lines) {
			builder.append(line + lineEnd);
		}		
		
		return builder.toString();
	}

}
