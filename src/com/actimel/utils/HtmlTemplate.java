package com.actimel.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.actimel.calendar.Const;


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
		String uniqCacheKey = "";
		if(resName != null && resName.contains("@")) {
			int pos = resName.indexOf("@");
			uniqCacheKey = resName.substring(pos+1);
			resName = resName.substring(0, pos);
			Utils.log("Got uniqueCacheKey: " + uniqCacheKey);
		}
		return loadFromResource(resName, true, uniqCacheKey);
	}
	public static HtmlTemplate loadFromResource(String resName, boolean cachedIfExists) {
		return loadFromResource(resName, cachedIfExists, "");
	}
	public static HtmlTemplate loadFromResource(String resName, boolean cachedIfExists, String uniqCacheKey) {
		String cacheKey = resName + "_" + uniqCacheKey;
		if(cachedIfExists && !Const.FORCE_DISABLE_CACHE) {
			if(templateCache.containsKey(cacheKey)) {
				return templateCache.get(cacheKey);
			}
		}
		
		HtmlTemplate inst = null; 
		if(Const.LOAD_FROM_DISK) {
			URL location = HtmlTemplate.class.getProtectionDomain().getCodeSource().getLocation();
			File directory = new File(location.getFile());
			Utils.log(directory.getAbsolutePath());
			File www_directory = new File(directory.getAbsolutePath() + File.separator + ".."+ File.separator + "www");
			
			inst = new HtmlTemplate(new File(www_directory, resName));
		} else {
			inst = new HtmlTemplate(new File(HtmlTemplate.class.getClassLoader().getResource(resName).getFile()));
		}
		Utils.log("Putting " + resName + " at: " + cacheKey);
		templateCache.put(cacheKey, inst);
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
							if(secondValue != null) {
								putYieldVar(firstValue, defaultVal);
							}
						}
					}
					
					if("isset".equals(actionName)) {
						String endPhrase = "@endIsset";
						String elsePhrase = "@else";
						
						int sectionPhraseLength = matcher.group().length();
						int sectionOffset = matcher.start();
						int stopPosition = content.indexOf(endPhrase, lineIndex + sectionPhraseLength - 1);
						int substrStart = lineIndex + sectionPhraseLength + sectionOffset;
						String _sVal = content.substring(substrStart, stopPosition);
						Utils.log("znaleziony isset: (match offset: "+sectionOffset+") " + _sVal);
						String ifExists = "", ifNotExists = "";
												
						if(_sVal.contains(elsePhrase)) {
							String[] split = _sVal.split(elsePhrase);
							ifExists = parse(split[0]);
							ifNotExists = parse(split[1]);
						} else {
							ifExists = parse(_sVal);
						}	
						List<String> excluded = (Arrays.asList(_sVal.split(newline)));
					
						Utils.log("IF EXISTS" + ifExists);
						Utils.log("IF NOT EXISTS" + ifNotExists);
						
						if(variablesToReplace.containsKey(firstValue)) {
							newLine += replaceVars(ifExists, variablesToReplace).trim();
						} else {
							newLine += replaceVars(ifNotExists, variablesToReplace).trim();
						}
						excludedLines.addAll(excluded);
					}
					
					if("section".equals(actionName) && secondValue == null) {
						
						String endPhrase = "@stop";
						int sectionOffset = matcher.start();
						int sectionPhraseLength = matcher.group().length();
						int stopPosition = content.indexOf(endPhrase, lineIndex + sectionPhraseLength - 1);
						int substrStart = lineIndex + sectionPhraseLength + sectionOffset;
						String _sVal = content.substring(substrStart, stopPosition);
						List<String> excluded = (Arrays.asList(_sVal.split(newline)));
						Utils.log("Parsing section... (match offset: "+sectionOffset+")");
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
		Utils.log("render");
		
		Utils.log("RENDER IN " + template.getName());
		return _render(content, variablesToReplace);
	}
	
	private String replaceVars(String content, HashMap<String, String> vars) {
		
		for(Entry<String, String> entry : vars.entrySet()) {
			String variableName = entry.getKey();
			String variableValue = entry.getValue();
			Utils.log(variableName + "=>" + variableValue);
			
			String yieldVar = YIELD_VAR_PREFIX + variableName + YIELD_VAR_SUFFIX;
			
			content = content.replaceAll(yieldVar, Matcher.quoteReplacement(variableValue));
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
