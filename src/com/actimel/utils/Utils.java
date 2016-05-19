package com.actimel.utils;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


import com.google.gson.reflect.TypeToken;



public class Utils {
	public static void log(String ... strings) {
		String msg = "";
		for (int i = 0; i < strings.length; i++) {
			if (msg.length() > 0)
				msg += " ";
			msg += strings[i];
		}
		System.out.println(msg);
	}

	public static void writeToFile(File f, String content) {
		
		try {
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);			
			bw.write(content);
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String readFile(File f) {

		try {
			FileReader reader = new FileReader(f);
			BufferedReader br = new BufferedReader(reader);
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    
		    br.close();
		    reader.close();
		    
		    return sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int rand(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static Type getGsonHashMapType() {		
		return new TypeToken<HashMap<String, String>>(){}.getType();
	}
	public static Type getGsonListType() {		
		return new TypeToken<List<String>>(){}.getType();
	}


	public static String getExtension(String name) {
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}
	public static String readFromStream(InputStream in) throws IOException {
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
	public static String readInternalFile(String internalPath) throws IOException {
		return Utils.readFromStream(Utils.class.getClassLoader().getResource(internalPath).openStream());

	}
}
