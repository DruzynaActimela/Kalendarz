package com.actimel.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
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
	private boolean parsed = false;
	
	
	public HtmlTemplate(File template) {
		this.template = template;

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
	
	private boolean isExcludedLine(String line, List<String> excluded) {
		for(String el : excluded) {
			if(line.equals(el)) return true;
		}
		return false;
	}
	
	private void parseTemplate() {
		if(parsed) return;
		
		content = Utils.readFile(template);
		content = parse(content);
		parsed = true;
	}
	
	private String parse(String content) {
		
		String newline = "\\r?\\n";
		String[] lines = content.split(newline);
		StringBuilder builder = new StringBuilder();
		

		List<String> excludedLines = new ArrayList<String>();
				
		for(String line : lines) {
			
			int lineIndex = content.indexOf(line);
			Utils.log("["+template.getName()+"] " + line);
			Matcher matcher = tVarRegex.matcher(line);
			
			if(line.matches("(.*)" + tVarRegex.pattern() + "(.*)") && !isExcludedLine(line, excludedLines)) {
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
						masterTemplate = HtmlTemplate.loadFromResource(firstValue, false);
						masterTemplate.parseTemplate();
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
						if(!variablesToReplace.containsKey(firstValue)) {
							String defaultVal = (secondValue != null) ? secondValue : "";
							putYieldVar(firstValue, defaultVal);
						}
					}
					
					if("section".equals(actionName) && secondValue == null) {
						
						String endPhrase = "@stop";
						
						int sectionPhraseLength = matcher.group().length();
						int stopPosition = content.indexOf(endPhrase, lineIndex + sectionPhraseLength - 1);
						int substrStart = lineIndex + sectionPhraseLength;
						String _sVal = content.substring(substrStart, stopPosition);
						List<String> excluded = (Arrays.asList(_sVal.split(newline)));
						String parsedSection = parse(_sVal);
						Utils.log("PARSED SECTION" + parsedSection);
						sectionValue = replaceVars(parsedSection, variablesToReplace);						
						excludedLines.addAll(excluded);

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
				if(!isExcludedLine(line, excludedLines)) {
					builder.append(line + "\n");
				}			
				
			}

		}
		
		Utils.log("-- post parse -- ");
		
		Utils.log(builder.toString());
		
		//content = builder.toString();
		return builder.toString();
	}
	
	public String render() {
		if(!parsed) parseTemplate();
		
		Utils.log("RENDER IN " + template.getName());
		return _render(content, variablesToReplace);
	}
	
	private String replaceVars(String content, HashMap<String, String> vars) {
		
		for(Entry<String, String> entry : vars.entrySet()) {
			String variableName = entry.getKey();
			String variableValue = entry.getValue();
			Utils.log(variableName + "=>" + variableValue);
			
			String yieldVar = YIELD_VAR_PREFIX + variableName + YIELD_VAR_SUFFIX;
			
			content = content.replaceAll(yieldVar, variableValue);
		}
		return content;
	}
	private String _render(String content, HashMap<String, String> vars) {
		
		String render = "";
		String parsed_content = content;
		if(masterTemplate != null) {
			render += masterTemplate.render();
		}
		

		parsed_content = replaceVars(content, vars);
		
		
		render += parsed_content;
		
		return render.trim();
	}
}
