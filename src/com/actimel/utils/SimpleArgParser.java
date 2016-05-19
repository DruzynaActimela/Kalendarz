package com.actimel.utils;

import java.util.HashMap;


public class SimpleArgParser {
	
	String[] rawArgs;
	HashMap<String, String> parsedArgs = new HashMap<String, String>();
	
	public SimpleArgParser(String[] args) {
		rawArgs = args;
		parse();
	}
	
	private void parse() {
		

		for(int i = 0; i < rawArgs.length; i++) {
			String arg = rawArgs[i];
			if(arg.startsWith("-")) {
				String value = (i + 1 < rawArgs.length) ? rawArgs[i + 1] : "";
				parsedArgs.put(arg.substring(1), value);
				Utils.log(arg.substring(1), "|", value);
			}			
		}
	}
	
	public boolean hasArg(String key) {
		return parsedArgs.containsKey(key);
	}
	
	public String getArg(String key) {
		return parsedArgs.get(key);
	}
}
