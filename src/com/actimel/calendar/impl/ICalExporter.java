package com.actimel.calendar.impl;

import java.io.IOException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.actimel.intfs.CalendarExporter;
import com.actimel.models.CalendarEvent;
import com.actimel.utils.Utils;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Duration;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

/**
 * Klasa odpowiedzialna za eksport listy zdarzeñ do pliku iCal.
 * @author ActimelTeam
 *
 */
public class ICalExporter extends CalendarExporter {

	@Override
	public final String export(final List<CalendarEvent> events) {
		Calendar calendar = new Calendar();
		calendar.getProperties().add(
				new ProdId("-//com.actimel.calendar//iCal4j 1.0//EN")
		);
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		java.util.TimeZone defaultTimezone = super.getDefaultTimezone();
		
		Utils.log("Using timezone: " + defaultTimezone.getDisplayName());

		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone(defaultTimezone.getDisplayName());
				
		//SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DATE_FORMAT_DAY_TIME);
		
		for (CalendarEvent calendarEvent : events) {
			Date startDate = Utils.dateObjectFromTimestamp(calendarEvent.getStampStart());
			Date endDate = Utils.dateObjectFromTimestamp(calendarEvent.getStampEnd());
			
			DateTime dtStart = new DateTime(calendarEvent.getStampStart());
			DateTime dtEnd = new DateTime(calendarEvent.getStampEnd());
			
			dtStart.setTimeZone(timezone);
			dtEnd.setTimeZone(timezone);
			
			int durationSeconds = Utils.calculateDuration(startDate, endDate);
			
			Dur duration = new Dur(0, 0, 0, durationSeconds);
			
			VEvent vevent = null;
			
			if (calendarEvent.isAllDay()) {
				vevent = new VEvent(dtStart, calendarEvent.getName());
			} else {
				vevent = new VEvent(dtStart, dtEnd, calendarEvent.getName());
			}		
			
			vevent.getProperties().add(new Property("COLOR", null) {
				private static final long serialVersionUID = 1L;
				private String color;
				@Override
				public void setValue(final String arg0) throws IOException, URISyntaxException, ParseException {
					color = arg0;
				}

				@Override
				public void validate() throws ValidationException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public String getValue() {
					return color;
				}
				
			});
			
			String color = "#fffff";
			if (calendarEvent.hasEventGroup()) {
				color = calendarEvent.getEventGroup().getColor();
			}
			try {
				vevent.getProperties().getProperty("COLOR").setValue(color);
			} catch (IOException | URISyntaxException | ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (!calendarEvent.isAllDay()) {
				
				vevent.getProperties().add(new Duration());
				try {
					vevent.getProperties().getProperty(Property.DURATION).setValue(duration.toString());
				} catch (IOException | URISyntaxException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			UidGenerator ug = null;
			try {
				ug = new UidGenerator("" + calendarEvent.getId());
			} catch (SocketException e) {
				e.printStackTrace();
			}
			vevent.getProperties().add(ug.generateUid());
			
			calendar.getComponents().add(vevent);
		}
		
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.setValidating(false);
		
		return calendar.toString();
		
		/*Task task = t.getTask();
	        String url = t.getFormManagerURL();

	       
	        vtask.getProperties().add(new DtStart(new DateTime(task.getCreationDate()), false));
	        vtask.getProperties().add(new Description(task.getDescription()));
	        vtask.getProperties().add(new Uid(task.getID()));
	        vtask.getProperties().add(new Summary(task.getDescription()));
	        Url ur = new Url();
	        ur.setUri(URI.create(url));
	        vtask.getProperties().add(ur);

	        calendar.getComponents().add(vtask);
	      */  

	}

}
