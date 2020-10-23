package me.tudedude.spigotstorageloader.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Color;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;

public class ConfigurationSection {
	
	
	/*
	 * LOAD CONFIGURATION SECTIONS WITH SNAKEYAML
	 * SAVE TEMPLATE WITH PLACEHOLDERS FOR VARIABLES IN PARALLEL
	 * ON SAVE, PULL VALUES FROM CONFIGSECTIONS AND WRITE
	 * TO PLACEHOLDERS
	 */
	
	private HashMap<String, ConfigurationSection> children;
	private HashMap<String, Object> values;
	
	private String path;
	
	private ConfigurationSection parent, root;
	
	public ConfigurationSection() {
		
	}
	
	public ConfigurationSection(String _path) {
		path = _path;
		children = null;
		parent = root = null;
	}
	
	public ConfigurationSection(ConfigurationSection _parent, String _path) {
		path = _path;
		children = null;
		parent = _parent;
		root = _parent.root;
	}
	
	public ConfigurationSection(String _path, ConfigurationSection _root, ConfigurationSection _parent) {
		path = _path;
		children = null;
		parent = _parent;
		root = _root;
	}
	
	public ConfigurationSection(String _path, HashMap<String, ConfigurationSection> _children) {
		path = _path;
		children = _children;
		parent = root = null;
	}
	
	public ConfigurationSection(String _path, HashMap<String, ConfigurationSection> _children, ConfigurationSection _root, ConfigurationSection _parent) {
		path = _path;
		children = _children;
		root = _root;
		parent = _parent;
	}

	public boolean contains(String path){
		String[] exploded = path.split(".");
		if(exploded.length == 0 || (exploded.length > 1 && children == null))return false;
		ConfigurationSection child = children.get(exploded[0]);
		if(exploded.length == 1) {
			return child != null;
		}
		return child.contains(path.substring(exploded.length));
	}

	public Object get(String path){
		return get(path, null);
	}

	public Object get(String path, Object def){
		String[] exploded = path.split(".");
		if(exploded.length == 0 || (exploded.length > 1 && children == null))return def;
		if(exploded.length == 1)return values.get(path);
		ConfigurationSection child = children.get(exploded[0]);
		return child.get(path.substring(exploded.length), def);
	}

	public boolean getBoolean(String path){
		return getBoolean(path, false);
	}

	public boolean getBoolean(String path, boolean def){
		Object obj = get(path, def);
		if(obj instanceof Boolean)return (Boolean)obj;
		return def;
	}

	public List<Boolean> getBooleanList(String path){
		List<?> list = getList(path);
		if(list == null)return new ArrayList<Boolean>();
		List<Boolean> res = new ArrayList<Boolean>();
		for(Object o : list) {
			if(o instanceof Boolean)res.add((Boolean)o);
			if(o instanceof String) {
				if(((String)o).equalsIgnoreCase("true"))res.add(true);
				else if(((String)o).equalsIgnoreCase("false"))res.add(false);
			}
		}
		return res;
	}

	public List<Byte> getByteList(String path){
		List<?> list = getList(path);
		if(list == null)return new ArrayList<Byte>();
		List<Byte> res = new ArrayList<Byte>();
		for(Object o : list) {
			if(o instanceof Byte)res.add((Byte)o);
			else {
				try {
					res.add(Byte.valueOf(o.toString()));
				}catch(Exception e) {}
			}
		}
		return res;
	}

	public List<Character> getCharacterList(String path){
		List<?> list = getList(path);
		if(list == null)return new ArrayList<Character>();
		List<Character> res = new ArrayList<>();
		for(Object o : list) {
			if(o instanceof Character)res.add((Character)o);
			else if(o instanceof Number) {
				res.add((char)((Number)o).intValue());
			}
		}
		return res;
	}

	/*
	public Color getColor(String path){
		return configuration.getColor(path);
	}

	public Color getColor(String path, Color def){
		return configuration.getColor(path, def);
	}*/

	public ConfigurationSection getConfigurationSection(String path){
		String[] explode = path.split(".");
		if(explode.length == 0)return this;
		ConfigurationSection child = children.get(explode[0]);
		if(explode.length == 1 || child == null) return child;
		return child.getConfigurationSection(path.substring(explode[0].length()));
	}

	public String getCurrentPath(){
		return path;
	}

	public double getDouble(String path){
		return getDouble(null);
	}

	public double getDouble(String path, double def){
		Object obj = get(path, def);
		if(obj instanceof Double)return (Double)obj;
		return def;
	}

	public List<Double> getDoubleList(String path){
		List<?> list = getList(path);
		if(list == null)return new ArrayList<Double>();
		List<Double> res = new ArrayList<>();
		for(Object o : list) {
			if(o instanceof Double)res.add((Double)o);
			else if(o instanceof Number) {
				res.add(((Number) o).doubleValue());
			}else {
				try {
					Double.parseDouble(o.toString());
				}catch(Exception e) {}
			}
		}
		return res;
	}

	public List<Float> getFloatList(String path){
		List<?> list = getList(path);
		if(list == null)return new ArrayList<Float>();
		List<Float> res = new ArrayList<>();
		for(Object o : list) {
			if(o instanceof Float)res.add((Float)o);
			else if(o instanceof Number) {
				res.add(((Number) o).floatValue());
			}else {
				try {
					Float.parseFloat(o.toString());
				}catch(Exception e) {}
			}
		}
		return res;
	}

	public int getInt(String path){
		return getInt(path, 0);
	}

	public int getInt(String path, int def){
		Object o = get(path, def);
		if(o instanceof Integer)return (Integer)o;
		return def;
	}

	public List<Integer> getIntegerList(String path){
		List<?> list = getList(path);
		if(list == null)return new ArrayList<Integer>();
		List<Integer> res = new ArrayList<>();
		for(Object o : list) {
			if(o instanceof Integer)res.add((Integer)o);
			else if(o instanceof Number) {
				res.add(((Number) o).intValue());
			}else {
				try {
					Integer.parseInt(o.toString());
				}catch(Exception e) {}
			}
		}
		return res;
	}

/*	public ItemStack getItemStack(String path){
		return getItemStack(path, null);
	}*/

/*	public ItemStack getItemStack(String path, ItemStack def){
		return configuration.getItemStack(path, def);
	}*/

	public Set<String> getKeys(boolean deep){
		if(!deep)return values.keySet();
		else {
			Set<String> res = new LinkedHashSet<String>();
			
			for(ConfigurationSection child : children.values()) {
				Map<String, Object> vals = child.getValues(true);
				for(String key : vals.keySet()) {
					res.add(key);
				}
			}
			return res;
		}
	}

	public List<?> getList(String path){
		return getList(path, null);
	}

	public List<?> getList(String path, List<?> def){
		Object val = get(path, def);
		if(val instanceof List)return (List<?>)val;
		return def;
	}

	// TODO - Serializable type
/*	public Location getLocation(String path){
		return getLocation(path, null);
	}

	public Location getLocation(String path, Location def){
		return configuration.getLocation(path, def);
	}*/

	public long getLong(String path){
		return getLong(path, 0L);
	}

	public long getLong(String path, long def){
		Object obj = get(path, def);
		if(obj instanceof Long)return (Long)obj;
		return def;
	}

	public List<Long> getLongList(String path){
		List<?> list = getList(path);
		if(list == null)return new ArrayList<Long>();
		List<Long> res = new ArrayList<>();
		for(Object o : list) {
			if(o instanceof Long)res.add((Long)o);
			else if(o instanceof Number) {
				res.add(((Number) o).longValue());
			}else {
				try {
					Long.parseLong(o.toString());
				}catch(Exception e) {}
			}
		}
		return res;
	}

	public List<Map<?,?>> getMapList(String path){
		List<?> list = getList(path);
		if(list == null)return new ArrayList<Map<?, ?>>();
		ArrayList<Map<?, ?>> res = new ArrayList<>();
		for(Object o : list) {
			if(o instanceof Map)res.add((Map<?,?>)o);
		}
		return res;
	}

	public String getName(){
		return path;
	}

	// TODO - Serializable object
/*	public OfflinePlayer getOfflinePlayer(String path){
		return getOfflinePlayer(path, null);
	}

	public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def){
		return configuration.getOfflinePlayer(path, def);
	}*/

	public ConfigurationSection getParent(){
		return parent;
	}

	public ConfigurationSection getRoot(){
		return root;
	}

	public List<Short> getShortList(String path){
		List<?> list = getList(path);
		if(list == null)return new ArrayList<Short>();
		List<Short> res = new ArrayList<>();
		for(Object o : list) {
			if(o instanceof Short)res.add((Short)o);
			else if(o instanceof Number) res.add(((Number) o).shortValue());
			
			else {
				try {
					Short.parseShort(o.toString());
				}catch(Exception e) {}
			}
		}
		return res;
	}

	public String getString(String path){
		return getString(path, null);
	}

	public String getString(String path, String def){
		Object o = get(path, def);
		if(o instanceof String)return (String)o;
		if(o.toString() != null)return o.toString();
		return def;
	}

	public List<String> getStringList(String path){
		List<?> list = getList(path);
		if(list == null)return new ArrayList<String>();
		List<String> res = new ArrayList<>();
		for(Object o : list) {
			if(o instanceof String)res.add((String)o);
			if(o.toString() != null)res.add(o.toString());
		}
		return res;
	}

	public Map<String,Object> getValues(boolean deep){
		if(!deep)return values;
		else {
			HashMap<String, Object> result = new HashMap<String, Object>();
			for(ConfigurationSection child : children.values()) {
				Map<String, Object> vals = child.getValues(true);
				for(String key : vals.keySet()) {
					result.put(key, vals.get(key));
				}
			}
			return result;
		}
	}

	// TODO - Serializable object
/*	public Vector getVector(String path){
		return getVector(path, null);
	}

	public Vector getVector(String path, Vector def){
		return configuration.getVector(path, def);
	}*/

	public boolean isBoolean(String path){
		Object obj = get(path);
		return obj instanceof Boolean;
	}

	public boolean isColor(String path){
		Object obj = get(path);
		return obj instanceof Color;
	}

	public boolean isConfigurationSection(String path){
		ConfigurationSection sec = getConfigurationSection(path);
		return sec == null;
	}

	public boolean isDouble(String path){
		Object o = get(path);
		if(o instanceof Double)return true;
		try {
			return (Doubles.tryParse(o.toString()) == null ? true : false);
		}catch(Exception e) {
			return false;
		}
	}

	public boolean isInt(String path){
		Object o = get(path);
		if(o instanceof Integer)return true;
		try {
			Integer.parseInt(o.toString());
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	// TODO - Serializable object
/*	public boolean isItemStack(String path){
		return configuration.isItemStack(path);
	}*/

	public boolean isList(String path){
		return getList(path) == null ? false : true;
	}

	// TODO - Serializable object
/*	public boolean isLocation(String path){
		return configuration.isLocation(path);
	}*/

	public boolean isLong(String path){
		Object o = get(path);
		if(o instanceof Long || o instanceof Number)return true;
		try {
			Longs.tryParse(o.toString());
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	// TOOO - Serializable object
/*	public boolean isOfflinePlayer(String path){
		return configuration.isOfflinePlayer(path);
	}*/

	public boolean isSet(String path){
		Object o = get(path);
		if(o instanceof Set<?>)return true;
		else return false;
	}

	public boolean isString(String path){
		return get(path) instanceof String;
	}

	// TODO - Serializable object
/*	public boolean isVector(String path){
		return configuration.isVector(path);
	}*/

	public void set(String path, Object value){
		ConfigurationSection sec = this;
		int leadIndex, trailIndex;
		trailIndex = 0;
		leadIndex = path.indexOf(".");
		while(leadIndex != -1) {
			String token = path.substring(trailIndex, leadIndex);
			ConfigurationSection cs = sec.getConfigurationSection(token);
			if(cs == null) {
				if(value == null)return;
				sec = sec.createSection(token);
			}sec = cs;
			trailIndex = leadIndex + 1;
			leadIndex = path.indexOf(".", trailIndex);
		}
		
		String key = path.substring(trailIndex);
		if(sec == this) {
			if(value == null) {
				values.remove(key);
			}
			else {
				values.put(key, value);
			}
		}else {
			sec.set(key, value);
		}
	}
	
	public ConfigurationSection createSection(String name) {
		ConfigurationSection sec = this;
		int leadIndex, trailIndex;
		trailIndex = 0;
		leadIndex = path.indexOf(".");
		while(leadIndex != -1) {
			String token = path.substring(trailIndex, leadIndex);
			ConfigurationSection cs = sec.getConfigurationSection(token);
			if(cs == null) {
				sec = sec.createSection(token);
			}else{
				sec = cs;
			}
			trailIndex = leadIndex + 1;
			leadIndex = path.indexOf(".", trailIndex);
		}
		
		String key = path.substring(trailIndex);
		if(sec == this) {
			ConfigurationSection res = new ConfigurationSection(this, key);
			values.put(key, res);
			return res;
		}
		return sec.createSection(name);
	}

}
