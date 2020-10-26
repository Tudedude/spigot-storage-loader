/**
 * 
 */

package me.tudedude.spigotstorageloader.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

public class YamlInterpreter {
	
	// StorageLoader sl = new StorageLoader();
	// sl.initYaml("config", getResource("config.yml"));
	// sl.initYaml("storage", getResource("storage.yml"));
	// sl.initJson("logins");
	//
	// sl.getString("config.messages.prefix");
	// sl.set("config.hhh.test", "h");
	
	/*private final static String BOOL_REGEX = "( *)([a-zA-Z0-9_\\-]+):[ ]*(?i)(true|false)";
	private final static String SEC_REGEX = "( *)([a-zA-Z0-9_\\-]+): ?";*/
	
	public static void main(String[] args) {
/*		String file = "##########\n" + 
					  "## CFIG ##\n" +
					  "##########\n" +
					  "testVal: true\n" +
					  "testList:\n" +
					  "- 'Val1'\n" +
					  "- 'Val2'\n" +
					  "# this is a comment\n" +
					  "doesItWork: false\n" +
					  "testSection:\n" + 
					  "  testSubvalue: \"hello!\"";*/
		
		String file = "sec1:\n"+
					  "  sec2:\n"+
					  "    var1: 'test'\n" +
					  "    var2: 'test2'\n" +
					  "  sec3:\n"+
					  "    var3: 'test3'\n" +
					  "    var4: 'test4'\n";
		
		YamlStorage s = new YamlStorage();
		s.loadFromString(file);
		System.out.println("--[BEGIN TESTS]--");
		System.out.println(s.get("sec1.sec2.var1") + ";" + (s.get("sec1.sec2.var1") instanceof String));
		System.out.println(s.getKeys(true));
		System.out.println("--[END TESTS]--");
		
		try {
			Yaml y = new Yaml();
			Map<?, ?> map = y.load(file);
			for(Object obj : map.keySet()) {
				System.out.println(obj.toString() + ": " + map.get(obj).toString());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		FileConfiguration fc = new YamlConfiguration();
		try {
			fc.loadFromString(file);
		}catch(Exception e) {
			System.out.println("Invalid yaml!");
			return;
		}
		
		System.out.println("BEGINNING YAML PROCESSING");
		System.out.println(file);
		
		// split file into lines
		String[] lines = file.split("\n");
		
		// track lines that have been processed and had variables replaced
		List<String> processed = new ArrayList<String>();
		
		// track encountered variables
		HashMap<String, Object> vars = new HashMap<String, Object>();
		
		String path = "";
		int indentLevel = 0;
		int indentDepth = 0;
		
		// store the currently modified variable for lists and such
		Object currentVariable = null;
		
		// go line by line and process
		for(int i = 0; i < lines.length; i++){
			
			String line = lines[i];			
			
			Matcher m;
			String varName;
			
			String comment = escapeChars(line);
			int lineIndent = getIndent(line);
			
			System.out.println("indentLevel: " + indentLevel);
			System.out.println("lineIndent: " + lineIndent);
			System.out.println("comment: " + comment);
			System.out.println("path: " + path);
			
			if(indentDepth == 0 && lineIndent > indentDepth)indentDepth = lineIndent;
			if(lineIndent < indentLevel) {
				int regression = ((indentLevel/indentDepth) - (lineIndent/indentDepth));
				System.out.println("REGRESSION: " + regression);
			}
			
			System.out.println("Processing line: \"" + line + "\" (indent: " + lineIndent + ")");
			
			// if line starts with a hashmark, its a comment, no processing needed
			if(line.matches("^( *)#(.+)")){
				processed.add(line);
				System.out.println("COMMENT");
				continue;
			}
			
			// starts a new section
			String secRegex = "^( *)([a-zA-Z0-9_\\-]+):([ ]*)$";
			if(line.matches(secRegex)) {
				m = Pattern.compile(secRegex).matcher(line);
				m.find();
				varName = m.group(2);

				// search for next line to see if section is a list or a new level
				System.out.println("\n-=[STARTING SEARCH]=-");
				for(int _i = i; _i < lines.length; _i++) {
					String _line = lines[_i];
					int _indent = getIndent(_line);
					if(_indent > lineIndent) {
						// new section
						break;
					}
					System.out.println("searching line: \"" + _line + "\" (indent: " + _indent + ";lineIndent:" + lineIndent + ")");
					if(_line.matches("^( *)#(.+)"))continue;
					if(_line.matches("^( *)-( +)(.+)")) {
					}
					System.out.println("\n--=[NEXT LINE SEARCH]=--");
				}
				
				System.out.println("NEW SECTION");
				continue;
			}
			
			// detect booleans
			String boolRegex = "( *)([a-zA-Z0-9_\\-]+):[ ]*(?i)(true|false)";
			if(line.matches(boolRegex)) {
				m = Pattern.compile(boolRegex).matcher(line);
				m.find();
				varName = m.group(2);
				boolean value = (m.group(3) != null && m.group(3).equalsIgnoreCase("true"));
				processed.add(m.group(1) + m.group(2) + ": " + m.group(3));
				System.out.println("BOOLEAN|" + varName + ":" + value);
			}
			
			System.out.println("\n\n----==[MOVING TO NEXT LINE]==----\n\n");
			
		}
		System.out.println("-- DONE --");
		System.out.println("----------");
		for(String l : processed) {
			System.out.println(l);
		}
	}
	
	public static String escapeChars(String line) {
		while(line.contains("\\")) {
			for(int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				if(c == '\\') {
					String escaped = "{$%" + ((int)line.charAt(i+1)) + "%$}";
					line = line.substring(0, i) + escaped + line.substring(i+2);
				}
			}
		}
		return line;
	}
	
	public static String unEscapeChars(String line) {
		Pattern p = Pattern.compile("\\{\\$\\%([0-9]+)\\%\\$\\}");
		Matcher m = p.matcher(line);
		boolean found = m.find();
		do {
			line = line.replaceAll("\\{\\$\\%" + m.group(1) + "\\%\\$\\}", "\\\\" + ((char)Integer.parseInt(m.group(1))));
			m = p.matcher(line);
			found = m.find();
		}while(found);
		return line;
	}
	
	public static String remComment(String line) {
		if(!line.contains("#"))return "";
		String _line = line;
		while(_line.contains("\"")) {
			for(int i = 0; i < _line.length(); i++) {
				char c = _line.charAt(i);
				if(c == '\\') {
					String escaped = "{$%" + ((int)_line.charAt(i+1)) + "%$}";
					_line = _line.substring(0, i) + escaped + _line.substring(i+2);
					System.out.println(_line);
					break;
				}
			}
			break;
		}
		return "";
	}
	
	public static int getIndent(String line) {
		Pattern p = Pattern.compile("^( +)");
		Matcher m = p.matcher(line);
		if(!m.find())return 0;
		return m.group(1).length();
	}

}
