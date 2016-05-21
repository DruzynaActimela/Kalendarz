package com.actimel.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlTemplate {
	private static HashMap<String, HtmlTemplate> templateCache = new HashMap<String, HtmlTemplate>();
	
	private String YIELD_VAR_PREFIX = "__YIELD_START__";
	private String YIELD_VAR_SUFFIX = "__YIELD_END__";
	
	private File template;
	private Pattern tVarRegex = Pattern.compile("@([a-zA-Z0-9]+)(\\([\\'](.*?[^,])[\\'](,\\s?[\\'](.*?)[\\'])?\\))?",
												Pattern.CASE_INSENSITIVE
											   );
	// non-Java: @([a-zA-Z0-9]+)(\([\'](.*?[^,])[\'](,\s?[\'](.*?)[\'])?\))?
	
	private HashMap<String, String> variablesToReplace = new HashMap<String, String>(); // varName => defaultValue
	private String content;
	
	private HtmlTemplate masterTemplate = null;
	
	
	public HtmlTemplate(File template) {
		this.template = template;
		
		parse();
	}
	
	public static HtmlTemplate loadFromResource(String resName) {
		return loadFromResource(resName, true);
	}
	public static HtmlTemplate loadFromResource(String resName, boolean cachedIfExists) {
		if(cachedIfExists) {
			if(templateCache.containsKey(resName)) {
				return templateCache.get(resName);
			}
		}
		HtmlTemplate inst = new HtmlTemplate(new File(HtmlTemplate.class.getClassLoader().getResource(resName).getFile()));
		templateCache.put(resName, inst);
		return inst;
		
	}
	
	public void putYieldVar(String name, String val) {
		variablesToReplace.put(name, val);
	}
	
	private void parse() {
		content = Utils.readFile(template);
		String[] lines = content.split("\\r?\\n");
		StringBuilder builder = new StringBuilder();
		
		int currentLine = 0;
		
		List<String> leftOvers = new ArrayList<String>();
		
		for(String line : lines) {
			
			int lineIndex = content.indexOf(line);
			Utils.log("["+template.getName()+"] " + line);
			Matcher matcher = tVarRegex.matcher(line);
			
			if(line.matches("(.*)" + tVarRegex.pattern() + "(.*)")) {
				String newLine = "";
				Utils.log("--while begin--");
				int i = 0;
				while(matcher.find()) {
					int matchCount = 0;
					
					
					for(int m = 0; m < matcher.groupCount(); m++) {
						if(matcher.group(m) != null) {
							Utils.log(m + ". " + matcher.group(m));
							matchCount = m + 1;
						}
					}
					Utils.log("Match count: " + matchCount);
					
					String actionName = matcher.group(1);
					String firstValue = null;
					String secondValue = null;
					String sectionValue = "";
					
					if(matchCount == 4) {
						firstValue = matcher.group(3);
					}
					else if(matchCount == 5) {
						firstValue = matcher.group(3);
						secondValue = matcher.group(4);
						if(secondValue != null) {
							secondValue = secondValue.replaceAll("'|,", "").trim();							
						}
					}
					
					Utils.log(actionName + ", " + firstValue + ", " + secondValue);
					
					if("extends".equals(actionName) && masterTemplate == null) {
						masterTemplate = HtmlTemplate.loadFromResource(firstValue);
						Utils.log("Got master template: " + firstValue);
					}
					
					if("yield".equals(actionName)) {
						int startPos = matcher.start();
						int endPos = startPos + matcher.group().length();
						StringBuffer strbuf = new StringBuffer(line);
						String replacement = YIELD_VAR_PREFIX + firstValue + YIELD_VAR_SUFFIX;
						strbuf = strbuf.replace(startPos, endPos, replacement);
						Utils.log("REPLACING ["+line.substring(startPos, endPos )+"] to ["+replacement+"]");
						newLine += strbuf.toString();
						String defaultVal = (secondValue != null) ? secondValue : "";
						putYieldVar(firstValue, defaultVal);
					}
					
					if("section".equals(actionName) && secondValue == null) {
						
						String endPhrase = "@stop";
						
						int sectionPhraseLength = matcher.group().length();
						int stopPosition = content.indexOf(endPhrase, lineIndex + sectionPhraseLength - 1);
						int substrStart = lineIndex + sectionPhraseLength;
						sectionValue = content.substring(substrStart, stopPosition);
						leftOvers.add(sectionValue);
						
					} else if(secondValue != null) {
						sectionValue = secondValue;
					}
					if(sectionValue.length() > 0) 
						Utils.log("section of the content is: ["+sectionValue+"]");
					
					
					if("section".equals(actionName)) {
						if(masterTemplate != null) masterTemplate.putYieldVar(firstValue, sectionValue);						
					}
					
					
					i++;
				}
				Utils.log("--while end--");
				builder.append(newLine + "\n");
			} else {
				boolean is_leftover = false;
				for(String leftover : leftOvers) {
					if(leftover.trim().equals(line.trim())) {
						is_leftover = true;
					}
				}
				if(!is_leftover) {
					builder.append(line + "\n");
				}
				
				
			}
			
		
			
			currentLine++;
		}
		
		Utils.log("-- post parse -- ");
		
		Utils.log(builder.toString());
		
		content = builder.toString();
	}
	
	public String render() {
		
		
		String render = "";
		String parsed_content = content;
		if(masterTemplate != null) {
			render += masterTemplate.render();
		}
		
		Utils.log("RENDER IN " + template.getName());
		
		for(Entry<String, String> entry : variablesToReplace.entrySet()) {
			String variableName = entry.getKey();
			String variableValue = entry.getValue();
			Utils.log(variableName + "=>" + variableValue);
			
			String yieldVar = YIELD_VAR_PREFIX + variableName + YIELD_VAR_SUFFIX;
			
			parsed_content = parsed_content.replaceAll(yieldVar, variableValue);
		}
		
		
		render += parsed_content;
		
		return render.trim();
		
		
		
		

			/*
	      if (m.find( )) {
	         System.out.println("Found value: " + m.group(0) );
	         System.out.println("Found value: " + m.group(1) );
	         System.out.println("Found value: " + m.group(2) );
	      } else {
	         System.out.println("NO MATCH");
	      }
	      */
	   
	}
}
