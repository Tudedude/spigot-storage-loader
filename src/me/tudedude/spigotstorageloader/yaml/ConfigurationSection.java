package me.tudedude.spigotstorageloader.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	
	protected final Map<String, Object> values = new LinkedHashMap<String, Object>();
	
	private final YamlStorage root;
	private final ConfigurationSection parent;
	
	private String path;
	
	public ConfigurationSection() {
		if(!(this instanceof YamlStorage)) {
			throw new IllegalStateException("Cannot construct a root ConfigurationSection while not a YamlStorage");
		}
		
		this.path = "";
		this.parent = null;
		this.root = (YamlStorage)this;
	}
	
	public ConfigurationSection(String _path) {
		path = _path;
		parent = root = null;
	}
	
	public ConfigurationSection(ConfigurationSection _parent, String _path) {
		path = _path;
		parent = _parent;
		root = _parent.root;
	}
	
	public ConfigurationSection(String _path, YamlStorage _root, ConfigurationSection _parent) {
		path = _path;
		parent = _parent;
		root = _root;
	}

	public boolean contains(String path){
		return (get(path) != null);
	}

	public Object get(String path){
		return get(path, null);
	}

	public Object get(String path, Object def){
		if(path.length() == 0)return this;
		YamlStorage storage = root;
		if(storage == null) {
			throw new IllegalStateException("Cannot access section without a root");
		}
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        ConfigurationSection section = this;
        while ((i1 = path.indexOf(".", i2 = i1 + 1)) != -1) {
            section = section.getConfigurationSection(path.substring(i2, i1));
            if (section == null) {
                return def;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            Object result = values.get(key);
            return (result == null) ? def : result;
        }
        return section.get(key, def);
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
		Object obj = get(path);
		return (obj instanceof ConfigurationSection ? (ConfigurationSection)obj : null);
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
			
			for(Entry<String, Object> childSet : values.entrySet()) {
				Object child = childSet.getValue();
				if(child instanceof ConfigurationSection) {
					Set<String> keys = ((ConfigurationSection)child).getKeys(true);
					for(String key : keys) {
						res.add(createPath((ConfigurationSection)child, key, this));
					}
				}
				res.add(createPath(this, childSet.getKey(), this));
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

	public YamlStorage getRoot(){
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
		if(o == null)return null;
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

	/*
	 * 
		if(!deep)return values.keySet();
		else {
			Set<String> res = new LinkedHashSet<String>();
			
			for(Entry<String, Object> childSet : values.entrySet()) {
				Object child = childSet.getValue();
				if(child instanceof ConfigurationSection) {
					Set<String> keys = ((ConfigurationSection)child).getKeys(true);
					for(String key : keys) {
						res.add(createPath((ConfigurationSection)child, key, this));
					}
				}
				res.add(createPath(this, childSet.getKey(), this));
			}
			return res;
		}
	 */
	
	public Map<String,Object> getValues(boolean deep){
		if(!deep)return values;
		else {
			HashMap<String, Object> result = new HashMap<String, Object>();
			for(Entry<String, Object> childSet : values.entrySet()) {
				Object child = childSet.getValue();
				if(child instanceof ConfigurationSection) {
					Set<String> keys = ((ConfigurationSection)child).getKeys(true);
					for(String key : keys) {
						result.put(createPath((ConfigurationSection)child, key, this), ((ConfigurationSection)child).get(key));
					}
				}
				result.put(createPath(this, childSet.getKey(), this), childSet.getValue());
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
	
	public ConfigurationSection createSection(String path) {
		int i1 = -1, i2;
        ConfigurationSection section = this;
        while ((i1 = path.indexOf(".", i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            ConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }
		
		String key = path.substring(i2);
		if(section == this) {
			ConfigurationSection res = new ConfigurationSection(this, key);
			values.put(key, res);
			return res;
		}
		return section.createSection(path);
	}
	
	public static String createPath(ConfigurationSection section, String key, ConfigurationSection relativeTo) {
        YamlStorage root = section.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create path without a root");
        }

        StringBuilder builder = new StringBuilder();
        if (section != null) {
            for (ConfigurationSection parent = section; (parent != null) && (parent != relativeTo); parent = parent.getParent()) {
                if (builder.length() > 0) {
                    builder.insert(0, ".");
                }

                builder.insert(0, parent.getName());
            }
        }

        if ((key != null) && (key.length() > 0)) {
            if (builder.length() > 0) {
                builder.append(".");
            }

            builder.append(key);
        }

        return builder.toString();
    }

}
