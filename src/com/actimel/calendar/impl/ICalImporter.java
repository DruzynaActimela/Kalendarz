package com.actimel.calendar.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import com.actimel.intfs.CalendarImporter;
import com.actimel.models.CalendarEvent;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.CompatibilityHints;

/**
 * Abstrakcyjna klasa, definiuj¹ca jak maj¹ wygl¹dac klasy importuj¹ce eventy z
 * plików iCal.
 * 
 * @author ActimelTeam
 *
 */

public class ICalImporter extends CalendarImporter {
	/**
	 * Funkcja importuj¹ca zdarzenia.
	 * 
	 * @param iCalFile Obiekt pliku, z którego nale¿y wczytaæ zdarzenia
	 * @throws IOException
	 */
	public final void importEvents(final File iCalFile) {
		try {
			importEvents(new FileReader(iCalFile));
		} catch (FileNotFoundException e) {
			
		}
	}

	/**
	 * Funkcja importuj¹ca zdarzenia.
	 * 
	 * @param reader Obiekt readera, z którego bêd¹ czytane dane CSV
	 * @throws IOException
	 */
	public final void importEvents(final Reader reader) {
		CalendarBuilder builder = new CalendarBuilder();

		try {
			// W³aœciwoœæ COLOR powoduje blad bez tej opcji
			CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);
			Calendar calendar = builder.build(reader);

			for (Iterator<CalendarComponent> i = calendar.getComponents().iterator(); i.hasNext();) {
				Component component = (Component) i.next();
				if (component.getName().equals("VEVENT")) {
					VEvent event = (VEvent) component;
					long start = event.getStartDate().getDate().getTime();
					long end = event.getEndDate().getDate().getTime();
					CalendarEvent cEvent = new CalendarEvent(0, event.getSummary().toString(), start, end, false, true);
					super.addEvent(cEvent);
				}
			}
		} catch (ParserException | IOException e) {
			e.printStackTrace();
		}
		
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
