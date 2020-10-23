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

import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

public class YamlInterpreter {
	
	private final static String BOOL_REGEX = "( *)([a-zA-Z0-9_\\-]+):[ ]*(?i)(true|false)";
	private final static String SEC_REGEX = "( *)([a-zA-Z0-9_\\-]+): ?";
	
	// BOOLEAN, BOOL_LIST, BYTE_LIST, CHAR_LIST, COLOR, DOUBLE, FLOAT, FLOAT_LIST, INT, INT_LIST, OBJ_LIST, 
	// LOCATION, LONG, LONG_LIST, MAP_LIST, OFFLINE_PLAYER, SHORT_LIST, STRING, STRING_LIST, VECTOR
	private enum VarType{
		BOOLEAN(Boolean.class),
		COLOR(Color.class),
		DOUBLE(Double.class),
		FLOAT(Float.class),
		STRING(String.class),
		INT(Integer.class),

		BOOL_LIST(List.class, Boolean.class),
		BYTE_LIST(List.class, Byte.class),
		CHAR_LIST(List.class, Character.class),
		FLOAT_LIST(List.class, Float.class),
		INT_LIST(List.class, Integer.class),
		OBJ_LIST(List.class, Object.class),
		LONG_LIST(List.class, Long.class),
		MAP_LIST(List.class, Map.class, Object.class),
		SHORT_LIST(List.class, Short.class),
		STRING_LIST(List.class, String.class);
		
		private Object clazz;
		
		private Object innerType, innerInnerType;
		
		private VarType(Object cl){
			this.clazz = cl;
		}
		
		private VarType(Object cl, Object iT) {
			this.clazz = cl;
			this.innerType = iT;
		}
		
		private VarType(Object cl, Object iT, Object iIT) {
			this.clazz = cl;
			this.innerType = iT;
			this.innerInnerType = iIT;
		}
		
		public Object getType() {
			return this.clazz;
		}
		
		public Object getInnerType() {
			return this.innerType;
		}
		
		public Object getInnerInnerType() {
			return this.innerInnerType;
		}
	}
	
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
		
		// store the active variable type
		VarType type = null;
		
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
				type = null;

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
						type = VarType.OBJ_LIST;
					}
					System.out.println("\n--=[NEXT LINE SEARCH]=--");
				}
				
				if(type == null) {
					// this is a new section
					path = path + (path.length() == 0 ? "" : ".") + varName;
				}else {
					// this is a new list
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
