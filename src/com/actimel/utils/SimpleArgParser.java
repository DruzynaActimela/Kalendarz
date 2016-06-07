package com.actimel.utils;

import java.util.HashMap;


/**
 * Parser argumentów z CLI.
 * @author ActimelTeam
 *
 */
public class SimpleArgParser {
	
	/**
	 * Surowe argumenty.
	 * @uml.property  name="rawArgs" multiplicity="(0 -1)" dimension="1"
	 */
	private String[] rawArgs;
	
	/**
	 * Przeparsowane argumenty.
	 * @uml.property  name="parsedArgs"
	 * @uml.associationEnd  qualifier="key:java.lang.String java.lang.String"
	 */
	private HashMap<String, String> parsedArgs = new HashMap<String, String>();
	
	/**
	 * Konstruktor.
	 * @param args Surowe parametry z konsoli.
	 */
	public SimpleArgParser(final String[] args) {
		rawArgs = args;
		parse();
	}
	
	/**
	 * Metoda parsuj¹ca przekazane parametry.
	 */
	private void parse() {
		

		for (int i = 0; i < rawArgs.length; i++) {
			String arg = rawArgs[i];
			if (arg.startsWith("-")) {
				String value = "";
				if (i + 1 < rawArgs.length) {
					value = rawArgs[i + 1];
				}
				parsedArgs.put(arg.substring(1), value);
				Utils.log(arg.substring(1), "|", value);
			}			
		}
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca sprawdzenie, 
	 * czy obiekt przechowuje wartosc o podanym kluczu.
	 * @param key Klucz
	 * @return true/false jesli wartosc zostanie znaleziona lub nie.
	 */
	public final boolean hasArg(final String key) {
		return parsedArgs.containsKey(key);
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca pobranie wartosci o podanym kluczu.
	 * @param key Klucz
	 * @return Wartosc
	 */
	public final String getArg(final String key) {
		return parsedArgs.get(key);
	}
}
