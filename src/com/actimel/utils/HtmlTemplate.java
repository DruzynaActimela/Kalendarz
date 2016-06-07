package com.actimel.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.actimel.calendar.Const;

/**
 * Klasa odpowiedzialna za parsowanie templatek HTML.
 * @author ActimelTeam
 *
 */
public class HtmlTemplate {
	/**
	 * Cache templatek.
	 */
	private static HashMap<String, HtmlTemplate> templateCache = new HashMap<String, HtmlTemplate>();
	
	/**
	 * Wartosc okreœlaj¹ca prefix zmiennej,  która zostanie zamieniona na odpowiadaj¹c¹ jej wartosc.
	 * @uml.property  name="yieldVarPrefix"
	 */	
	private final String yieldVarPrefix = "__YIELD_START__";
	
	/**
	 * Wartosc okreœlaj¹ca suffix zmiennej,  która zostanie zamieniona na odpowiadaj¹c¹ jej wartosc.
	 * @uml.property  name="yieldVarSuffix"
	 */	
	private final String yieldVarSuffix = "__YIELD_END__";
	
	/**
	 * Zmienna przechowuj¹ca plik, z którego pobrana jest zawartosc HTML.
	 * @uml.property  name="template"
	 */
	private File template;
	
	/**
	 * Wyra¿enie regularne, które wyszukuje dyrektyw templatek.
	 * @uml.property  name="tVarRegex"
	 */
	private final Pattern tVarRegex = Pattern.compile("@([a-zA-Z0-9]+)(\\([\\'](.*?[^,])[\\'](,\\s?[\\'](.*?)[\\'])?\\))?",
												Pattern.CASE_INSENSITIVE
											   );
	// non-Java: @([a-zA-Z0-9]+)(\([\'](.*?[^,])[\'](,\s?[\'](.*?)[\'])?\))?
	
	/**
	 * Zmienna przechowuj¹ca wartosci,  na które zostan¹ zamienione odpowiadaj¹ce im klucze.
	 * @uml.property  name="variablesToReplace"
	 * @uml.associationEnd  qualifier="name:java.lang.String java.lang.String"
	 */
	private final HashMap<String, String> variablesToReplace = new HashMap<String, String>(); // varName => defaultValue
	
	/**
	 * Zawartosc HTML templatki.
	 * @uml.property  name="content"
	 */
	private String content;
	
	/**
	 * Templatka nadrzêdna do aktualnej (master).
	 * @uml.property  name="masterTemplate"
	 * @uml.associationEnd  
	 */
	private HtmlTemplate masterTemplate = null;
	
	/**
	 * Zmienna okreœlaj¹ca, czy templatka zosta³a ju¿ przeparsowana.
	 * @uml.property  name="parsed"
	 */
	private boolean parsed = false;
	
	
	/**
	 * Konstruktor.
	 * @param ttemplate Plik, z którego zostanie pobrana tresc HTML.
	 */
	public HtmlTemplate(final File ttemplate) {
		this.template = ttemplate;

	}
	
	/**
	 * Statyczna metoda odpowiedzialna za ³adowanie templatek z JARa aplikacji.
	 * @param resName Nazwa pliku HTML
	 * @return Instancja templatki, 
	 * jeœli plik zosta³ znaleziony, null jeœli nie.
	 */
	public static HtmlTemplate loadFromResource(final String resName) {
		String uniqCacheKey = "";
		String resourceName = resName;
		
		if (resourceName != null && resourceName.contains("@")) {
			int pos = resourceName.indexOf('@');
			uniqCacheKey = resourceName.substring(pos + 1);
			resourceName = resourceName.substring(0, pos);
			Utils.log("Got uniqueCacheKey: " + uniqCacheKey);
		}
		return loadFromResource(resourceName, true, uniqCacheKey);
	}
	
	/**
	 * Statyczna metoda odpowiedzialna za ³adowanie templatek z JARa aplikacji.
	 * @param resName Nazwa pliku HTML
	 * @param cachedIfExists Czy dozwolone jest za³adowanie templatki z cache.
	 * @return Instancja templatki, 
	 * jeœli plik zosta³ znaleziony, null jeœli nie.
	 */
	public static HtmlTemplate loadFromResource(final String resName, final boolean cachedIfExists) {
		return loadFromResource(resName, cachedIfExists, "");
	}
	
	/**
	 * Statyczna metoda odpowiedzialna za ³adowanie templatek z JARa aplikacji.
	 * @param resName Nazwa pliku HTML
	 * @param cachedIfExists Czy dozwolone jest za³adowanie templatki z cache.
	 * @param uniqCacheKey Unikalny klucz do cache.
	 * @return Instancja templatki, 
	 * jeœli plik zosta³ znaleziony, null jeœli nie.
	 */
	
	@SuppressWarnings("unused")
	public static HtmlTemplate loadFromResource(final String resName, final boolean cachedIfExists, final String uniqCacheKey) {
		String cacheKey = resName + "_" + uniqCacheKey;
		if (cachedIfExists && !Const.FORCE_DISABLE_CACHE) {
			if (templateCache.containsKey(cacheKey)) {
				return templateCache.get(cacheKey);
			}
		}
		
		HtmlTemplate inst = null; 
		if (Const.LOAD_FROM_DISK) {
			URL location = HtmlTemplate.class.getProtectionDomain().getCodeSource().getLocation();
			File directory = new File(location.getFile());
			Utils.log(directory.getAbsolutePath());
			File wwwDirectory = new File(directory.getAbsolutePath() + File.separator + ".." + File.separator + "www");
			
			inst = new HtmlTemplate(new File(wwwDirectory, resName));
		} else {
			inst = new HtmlTemplate(new File(HtmlTemplate.class.getClassLoader().getResource(resName).getFile()));
		}
		Utils.log("Putting " + resName + " at: " + cacheKey);
		templateCache.put(cacheKey, inst);
		return inst;
		
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca dodanie wartosci, 
	 * która zostanie zamieniona na klucz.
	 * @param name Klucz
	 * @param val Wartosc
	 */
	public final void putYieldVar(final String name, final String val) {
		variablesToReplace.put(name, val);
	}
	
	/**
	 * Metoda umo¿liwiaj¹ca dodanie kilku wartosci, 
	 * która zostana zamienione na odpowiadajace im klucze.
	 * @param vars HashMap z kluczami i wartosciami
	 */
	public final void putYieldVars(final HashMap<String, String> vars) {
		if (vars == null) {
			return;
		}
		variablesToReplace.putAll(vars);
	}
	
	/**
	 * Metoda sprawdzaj¹ca, czy dla podanej listy, 
	 * znajduje siê w niej podana linia tekstu.
	 * @param line Linia tekstu do znalezienia
	 * @param excluded Lista linii tekstu, która zostanie przeszukana.
	 * @return Wartosc true/false, jeœli linia zostanie znaleziona lub nie.
	 */
	private boolean isExcludedLine(final String line, final List<String> excluded) {
		for (String el : excluded) {
			if (line.equals(el)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Metoda odpowiedzialna za zainicjalizowanie parsowania templatki.
	 */
	private void parseTemplate() {
		if (parsed) {
			return;
		}
		
		setTemplateContent(Utils.readFile(template));
		setTemplateContent(parse(content));
		
		parsed = true;
	}
	
	/**
	 * Wewnêtrzna metoda odpowiedzialna za przeparsowanie templatki.
	 * @param rawContent Surowa tresc HTML 
	 * z tagami specyficznymi dla tej implementacji templatek.
	 * @return Przeparsowany HTML
	 */
	private String parse(final String rawContent) {
		String newline = "\\r?\\n";
		String[] lines = rawContent.split(newline);
		StringBuilder builder = new StringBuilder();
		List<String> excludedLines = new ArrayList<String>();	
		for (String line : lines) {
			
			int lineIndex = rawContent.indexOf(line);
			Utils.log("[" + template.getName() + "] " + line);
			Matcher matcher = tVarRegex.matcher(line);
			
			if (line.matches("(.*)" + tVarRegex.pattern() + "(.*)") && !isExcludedLine(line, excludedLines)) {
				String newLine = "";
				Utils.log("--while begin--");
				
				while (matcher.find()) {
					int matchCount = 0;
					
					
					for (int m = 0; m < matcher.groupCount(); m++) {
						if (matcher.group(m) != null) {
							Utils.log(m + ". " + matcher.group(m));
							matchCount = m + 1;
						}
					}
					Utils.log("Match count: " + matchCount);
					
					String actionName = matcher.group(1);
					String firstValue = null;
					String secondValue = null;
					String sectionValue = "";
					
					final int matchCountFour = 4;
					final int matchCountThree = 3;
					final int matchCountFive = 5;
					
					if (matchCount == matchCountFour) {
						firstValue = matcher.group(matchCountThree);
					} else if (matchCount == matchCountFive) {
						firstValue = matcher.group(matchCountThree);
						secondValue = matcher.group(matchCountFour);
						if (secondValue != null) {
							secondValue = secondValue.replaceAll("'|,", "").trim();							
						}
					}
					
					Utils.log(actionName + ", " + firstValue + ", " + secondValue);
					
					if ("extends".equals(actionName) && masterTemplate == null) {
						masterTemplate = HtmlTemplate.loadFromResource(firstValue, false);
						masterTemplate.putYieldVars(variablesToReplace);
						masterTemplate.parseTemplate();
						Utils.log("Got master template: " + firstValue);
					}
					
					if ("yield".equals(actionName)) {
						int startPos = matcher.start();
						int endPos = startPos + matcher.group().length();
						StringBuffer strbuf = new StringBuffer(line);
						String replacement = yieldVarPrefix + firstValue + yieldVarSuffix;
						strbuf = strbuf.replace(startPos, endPos, replacement);
						Utils.log("REPLACING [" + line.substring(startPos, endPos) + "] to [" + replacement + "]");
						newLine += strbuf.toString();
						if (!variablesToReplace.containsKey(firstValue)) {
							String defaultVal = (secondValue != null) ? secondValue : "";
							if (secondValue != null) {
								putYieldVar(firstValue, defaultVal);
							}
						}
					}
					
					if ("isset".equals(actionName)) {
						String endPhrase = "@endIsset";
						String elsePhrase = "@else";
						
						int sectionPhraseLength = matcher.group().length();
						int sectionOffset = matcher.start();
						int stopPosition = rawContent.indexOf(endPhrase, lineIndex + sectionPhraseLength - 1);
						int substrStart = lineIndex + sectionPhraseLength + sectionOffset;
						String tmpsVal = rawContent.substring(substrStart, stopPosition);
						Utils.log("znaleziony isset: (match offset: " + sectionOffset + ") " + tmpsVal);
						String ifExists = "", ifNotExists = "";
												
						if (tmpsVal.contains(elsePhrase)) {
							String[] split = tmpsVal.split(elsePhrase);
							ifExists = parse(split[0]);
							ifNotExists = parse(split[1]);
						} else {
							ifExists = parse(tmpsVal);
						}	
						List<String> excluded = (Arrays.asList(tmpsVal.split(newline)));
					
						Utils.log("IF EXISTS" + ifExists);
						Utils.log("IF NOT EXISTS" + ifNotExists);
						
						if (variablesToReplace.containsKey(firstValue)) {
							newLine += replaceVars(ifExists, variablesToReplace).trim();
						} else {
							newLine += replaceVars(ifNotExists, variablesToReplace).trim();
						}
						excludedLines.addAll(excluded);
					}
					
					if ("section".equals(actionName) && secondValue == null) {
						
						String endPhrase = "@stop";
						int sectionOffset = matcher.start();
						int sectionPhraseLength = matcher.group().length();
						int stopPosition = rawContent.indexOf(endPhrase, lineIndex + sectionPhraseLength - 1);
						int substrStart = lineIndex + sectionPhraseLength + sectionOffset;
						String tmpsVal = rawContent.substring(substrStart, stopPosition);
						List<String> excluded = (Arrays.asList(tmpsVal.split(newline)));
						Utils.log("Parsing section... (match offset: " + sectionOffset + ")");
						String parsedSection = parse(tmpsVal);
						Utils.log("PARSED SECTION" + parsedSection);
						sectionValue = replaceVars(parsedSection, variablesToReplace);						
						excludedLines.addAll(excluded);

					} else if (secondValue != null) {
						sectionValue = secondValue;
					}
					if (sectionValue.length() > 0) {
						Utils.log("section of the content is: [" + sectionValue + "]");
					}
					
					
					if ("section".equals(actionName)) {
						if (masterTemplate != null) {
							masterTemplate.putYieldVar(firstValue, sectionValue);
						}						
					}
				}
				Utils.log("--while end--");
				builder.append(newLine + "\n");
			} else {
				if (!isExcludedLine(line, excludedLines)) {
					builder.append(line + "\n");
				}			
				
			}

		}
		
		Utils.log("-- post parse -- ");
		
		Utils.log(builder.toString());
		
		//content = builder.toString();
		return builder.toString();
	}
	
	/**
	 * Metoda zwracaj¹ca wyrenderowany HTML.
	 * @return przeparsowany HTML
	 */
	public final String render() {
		if (!parsed) {
			parseTemplate();
		}
		Utils.log("render");
		
		Utils.log("RENDER IN " + template.getName());
		return recursiveRender(content, variablesToReplace);
	}
	
	/**
	 * Wewnêtrzna metoda zamieniaj¹ca wartosci 
	 * na odpowiadaj¹ce im klucze w zmiennej rawContent.
	 * @param rawContent Surowa tresc HTML z dyrektywami templatki
	 * @param vars Lista kluczy i wartosci
	 * @return Przeparsowana tresc HTML
	 */
	private String replaceVars(final String rawContent, final HashMap<String, String> vars) {
		String tmpContent = rawContent;
		for (Entry<String, String> entry : vars.entrySet()) {
			String variableName = entry.getKey();
			String variableValue = entry.getValue();
			Utils.log(variableName + "=>" + variableValue);
			
			String yieldVar = yieldVarPrefix + variableName + yieldVarSuffix;
			
			tmpContent = tmpContent.replaceAll(yieldVar, Matcher.quoteReplacement(variableValue));
		}
		return tmpContent;
	}
	/**
	 * Wewnêtrzna metoda zamieniaj¹ca wartosci 
	 * na odpowiadaj¹ce im klucze w zmiennej rawContent, 
	 * rekursywnie razem z treœciami templatek nadrzêdnych.
	 * @param rContent Surowa tresc HTML z dyrektywami templatki
	 * @param vars Lista kluczy i wartosci
	 * @return Przeparsowana tresc HTML
	 */
	private String recursiveRender(final String rContent, final HashMap<String, String> vars) {
		
		String render = "";
		String parsedContent = rContent;
		if (masterTemplate != null) {
			render += masterTemplate.render();
		}
		

		parsedContent = replaceVars(rContent, vars);
		
		
		render += parsedContent;
		
		return render.trim();
	}

	/**
	 * Metoda umo¿liwiaj¹ca ustawienie nowej wartosci templatki.
	 * @param c Nowa wartosc do ustawienia
	 */
	private void setTemplateContent(final String c) {
		content = c;
	}
}
