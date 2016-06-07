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

public class ICalImporter extends CalendarImporter {
	/**
	 * Funkcja importuj�ca zdarzenia.
	 * 
	 * @param csvFile
	 *            Obiekt pliku, z kt�rego nale�y wczyta� zdarzenia
	 * @throws IOException
	 */
	public void importEvents(File iCalFile) {
		try {
			importEvents(new FileReader(iCalFile));
		} catch (FileNotFoundException e) {
		}
	}

	/**
	 * Funkcja importuj�ca zdarzenia.
	 * 
	 * @param csvFile
	 *            Obiekt pliku, z kt�rego nale�y wczyta� zdarzenia
	 * @throws IOException
	 */
	public void importEvents(Reader reader) {
		CalendarBuilder builder = new CalendarBuilder();

		try {
			// W�a�ciwo�� COLOR powoduje blad bez tej opcji
			CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);
			Calendar calendar = builder.build(reader);

			for (Iterator<CalendarComponent> i = calendar.getComponents().iterator(); i.hasNext();) {
				Component component = (Component) i.next();
				if (component.getName().equals("VEVENT")) {
					VEvent event = (VEvent) component;
					long start = event.getStartDate().getDate().getTime();
					long end = event.getEndDate().getDate().getTime();
					CalendarEvent cEvent = new CalendarEvent(0, event.getSummary().toString(), start, end, false, true);
					events.add(cEvent);
				}
			}
		} catch (ParserException | IOException e) {
			e.printStackTrace();
		}
	}
}