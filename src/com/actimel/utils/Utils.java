package com.actimel.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.google.gson.reflect.TypeToken;


/**
 * Klasa skupiaj�ca w sobie wszystkie 
 * niezb�dne funkcje do poprawnego dzia�ania aplikacji.
 * @author ActimelTeam
 *
 */
public final class Utils {
	
	/** 
	 * Czy ten konstruktor nie jest super();?.
	 */
	private Utils() {
		super();
	}
	
	/**
	 * Funkcja umo�liwiaj�ca logowanie do konsoli.
	 * @param strings Lista element�w, kt�re maj� zostac wyswietlone
	 */
	public static void log(final String ... strings) {

		String msg = "";
		for (int i = 0; i < strings.length; i++) {
			if (msg.length() > 0) {
				msg += " ";
			}
			msg += strings[i];
		}
		System.out.println(msg); // NOPMD
	}

	/**
	 * Funkcja umo�liwiaj�ca zapis tekstu do pliku.
	 * @param f Plik, do kt�rego ma zostac zapisany tekst
	 * @param content Tresc do zapisania
	 */
	public static void writeToFile(final File f, final String content) {
		try {
			FileOutputStream fos = new FileOutputStream(f);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(content);
			bw.close();
			osw.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Metoda umo�liwiaj�ca wygenerowanie losowego ci�gu znak�w.
	 * @param length D�ugosc ci�gu
	 * @return Losowy ci�g znak�w o podanej d�ugo�ci
	 */
	public static String getRandomString(final int length) {
		final String characters = 
				"abcdefghijklmnopqrstuvwxyzABCDEFGHIJLMNOPQRSTUVWXYZ1234567890";
		int len = length;
		StringBuilder result = new StringBuilder();
		while (len > 0) {
			Random rand = new Random();
			result.append(characters.charAt(rand.nextInt(characters.length())));
			len--;
		}
		return result.toString();
	}
	
	/**
	 * Metoda umo�liwiaj�ca odczytanie zawartosci pliku.
	 * @param f Plik
	 * @return Zawartosc pliku jako String
	 */
	public static String readFile(final File f) {

		try {

			FileInputStream fis = new FileInputStream(f);			
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}

			br.close();
			isr.close();
			fis.close();

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Metoda umo�liwiaj�ca wygenerowanie losowej liczby z podanego przedzia�u.
	 * @param min Dolna granica przedzia�u
	 * @param max G�rna granica przedzia�u
	 * @return Losowa liczba z przedzia�u
	 */
	public static int rand(final int min, final int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
	
	/**
	 * Funkcja zwracaj�ca token typu dla HashMap'�w typu String,String.
	 * @return Type
	 */
	
	public static Type getGsonHashMapType() {
		return new TypeToken<HashMap<String, String>>() { }.getType();
	}
	/**
	 * Funkcja zwracaj�ca token typu dla List typu String.
	 * @return Type
	 */
	
	public static Type getGsonListType() {
		return new TypeToken<List<String>>() { }.getType();
	}

	/**
	 * Metoda umo�liwiaj�ca pobranie rozszerzenia pliku.
	 * @param name Nazwa pliku jako String
	 * @return Rozszerzenie podanego pliku
	 */
	public static String getExtension(final String name) {
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * Metoda umo�liwiaj�ca odczytanie zawarto�ci InputSteam.
	 * @param in InputSteam, z kt�rego zostan� odczytane dane
	 * @return Tekst, kt�ry zosta� odczytany z InputSteam
	 * @throws IOException B��d
	 */
	public static String readFromStream(final InputStream in) 
			throws IOException {
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader reader = new BufferedReader(isr);
		StringBuilder out = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line);
			out.append(newLine);
		}
		isr.close();
		in.close();
		reader.close();

		return out.toString();
	}
	
	/**
	 * Metoda umo�liwiaj�ca pobranie zawartosci pliku 
	 * przechowywanego w JAR aplikacji.
	 * @param internalPath Wewn�trzna sciezka do pliku. 
	 * (W kontekscie JARa aplikacji).
	 * @return Zawartosc pliku
	 * @throws IOException B��d
	 */
	public static String readInternalFile(final String internalPath) 
			throws IOException {
		return Utils.readFromStream(Utils.class.getClassLoader()
				.getResource(internalPath)
				.openStream());

	}

	/**
	 * Metoda zwracaj�ca hash SHA-256 dla podanego ci�gu znak�w.
	 * @param input Ci�g znak�w
	 * @return skr�t SHA-256 dla ci�gu znak�w.
	 * @throws NoSuchAlgorithmException B��d
	 */
	public static String sha256(final String input) 
			throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		final int magicStuffFF = 0xff;
		final int magicStuff00 = 0x100;
		final int theAlmighty16 = 16;
		
		for (int i = 0; i < result.length; i++) {
			sb
			.append(Integer.toString((result[i] & magicStuffFF)
					+ magicStuff00, theAlmighty16)
					.substring(1));
		}

		return sb.toString();
	}

	/**
	 * Metoda sprawdzaj�ca, czy podana nazwa u�ytkownika jest poprawna.
	 * @param input Nazwa u�ytkownika, kt�ra ma zostac sprawdzona.
	 * @return Rezultat sprawdzania jako true/false.
	 */
	public static boolean isUsernameValid(final String input) {
		if (input == null) {
			return false;
		}
		return input.matches("[a-zA-Z0-9_]{3,16}");
	}
	/**
	 * Metoda sprawdzaj�ca, czy podana nazwa grupy jest poprawna.
	 * @param input Nazwa, kt�ra ma zostac sprawdzona.
	 * @return Rezultat sprawdzania jako true/false.
	 */
	public static boolean isGroupNameValid(final String input) {
		if (input == null) {
			return false;
		}
		return input.matches("[�󹜳����a-zA-Z0-9_\\s]{3,128}");
	}
	
	/**
	 * Metoda konwertuj�ca dat� w podanym formacie 
	 * na timestamp w milisekundach.
	 * @param strDate Data do przekonwertowania
	 * @param format Format daty
	 * @return Timestamp w milisekundach
	 */
	public static long dateToTimestamp(
			final String strDate, 
			final String format
		) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Utils.log("strDate: "  + strDate);
		Utils.log("Using format: "  + format);
		
		Date date = null;
		
		try {
			date = dateFormat.parse(strDate);
			Utils.log("Parsed date: " + date.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		if (date == null) {
			return 0;
		}

		return date.getTime();
		
	}

	/**
	 * Funkcja umo�liwiaj�ca przedstawienie timestampa jako daty.
	 * @param timestampMilis Timestamp w milisekundach
	 * @param format Format daty
	 * @return Data jako String
	 */
	public static String 
	dateFromTimestamp(final Long timestampMilis, final String format) {
		
		Date date = new Date(timestampMilis);
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);

	}
	
	/**
	 * Funkcja umo�liwiaj�ca przedstawienie timestampa jako daty.
	 * @param timestampMilis Timestamp w milisekundach
	 * @return Data jako obiekt Date
	 */
	public static Date 
	dateObjectFromTimestamp(final Long timestampMilis) {
		return new Date(timestampMilis);
	}
	
	/**
	 * Metoda umo�liwiaj�ca przekonwertowanie ci�gu znak�w na Integer.
	 * @param val Ci�g znak�w 
	 * @param defaultVal Wartosc domy�lna
	 * @return Przekonwertowana wartosc lub wartosc domy�lna
	 */	
	public static Integer parseInt(final Object val, final Integer defaultVal) {
		try {
			return Integer.parseInt(String.valueOf(val));
		} catch (Exception e) {
			return defaultVal;
		}
	}
	
	/**
	 * Metoda umo�liwiaj�ca przekonwertowanie ci�gu znak�w na Float.
	 * @param val Ci�g znak�w 
	 * @param defaultVal Wartosc domy�lna
	 * @return Przekonwertowana wartosc lub wartosc domy�lna
	 */	
	public static Float parseFloat(final Object val, final Float defaultVal) {
		try {
			return Float.parseFloat(String.valueOf(val));
		} catch (Exception e) {
			return defaultVal;
		}
	}
	
	/**
	 * Metoda umo�liwiaj�ca przekonwertowanie ci�gu znak�w na Long.
	 * @param val Ci�g znak�w 
	 * @param defaultVal Wartosc domy�lna
	 * @return Przekonwertowana wartosc lub wartosc domy�lna
	 */	
	public static Long parseLong(final Object val, final Long defaultVal) {
		try {
			return Long.parseLong(String.valueOf(val));
		} catch (Exception e) {
			return defaultVal;
		}
	}
	
	/**
	 * Funkcja umo�liwiaj�ca obliczenie odst�pu czasu mi�dzy dwoma datami.
	 * @param startDate Data pocz�tkowa
	 * @param endDate Data ko�cowa
	 * @param dateFormat Format dat
	 * @return Pozytywny odst�p czasowy 
	 * mi�dzy dwoma datami wyra�ony w sekundach.
	 */
	public static long 
	calculateDuration(final String startDate, 
			final String endDate, final String dateFormat) {
		long start = Utils.dateToTimestamp(startDate, dateFormat);
		long end = Utils.dateToTimestamp(endDate, dateFormat);
		final int milis = 1000;
		return (Math.abs(end - start) / milis);
	}
	
	/**
	 * Funkcja umo�liwiaj�ca obliczenie odst�pu czasu mi�dzy dwoma datami.
	 * @param startDate Data pocz�tkowa
	 * @param endDate Data ko�cowa
	 * @return Pozytywny odst�p czasowy 
	 * mi�dzy dwoma datami wyra�ony w sekundach.
	 */
	public static int 
	calculateDuration(final Date startDate, final Date endDate) {
		long start = startDate.getTime();
		long end = endDate.getTime();
		final int milis = 1000;
		return (int) (Math.abs(end - start) / milis);
	}

	/**
	 * Metoda zwracaj�ca aktualny timestamp w milisekundach.
	 * @return Timestamp w milisekundach
	 */
	public static long timestamp() {
		return System.currentTimeMillis();
	}
	
	/**
	 * Metoda zwracaj�ca reprezentacj� godziny w sekundach.
	 * @param time Godzina
	 * @return Sekundy
	 */
	public static long strTimeToLong(final String time) {
		
		if (time == null || !time.contains(":")) {
			return 0;
		}
		
		String[] split = time.split(":");
		int sec = 0;
		final int minute = 60;
		if (split.length > 1) {
			sec += Utils.parseInt(split[0], 0) * minute;
			sec += Utils.parseInt(split[1], 0);
		}		
		return sec;
	}
	
	/**
	 * Metoda konwertuj�ca zakodowane urle na normalne.
	 * @param str Zakodowany URL
	 * @return URL
	 */
	public static String urlDecode(final String str) {
		try {
			return java.net.URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
}
