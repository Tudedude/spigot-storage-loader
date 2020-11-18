package me.tudedude.spigotstorageloader.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import me.tudedude.spigotstorageloader.yaml.ConfigurationSection;
import me.tudedude.spigotstorageloader.yaml.YamlStorage;

public interface StorageInterface {
	
	public enum Type{
		YAML,
		H2,
		SQLITE,
		JSON,
		SQL,
		MONGO
	}
	
	abstract String getName();
	
	abstract void setName(String name);
	
	abstract void init();
	
	abstract boolean contains(String path);
	abstract ConfigurationSection createSection(String path);
	abstract Object get(String path);
	abstract Object get(String path, Object def);
	abstract boolean getBoolean(String path);
	abstract boolean getBoolean(String path, boolean def);
	abstract List<Boolean> getBooleanList(String path);
	abstract List<Byte> getByteList(String path);
	abstract List<Character> getCharacterList(String path);
	abstract ConfigurationSection getConfigurationSection(String path);
	abstract String getCurrentPath();
	abstract double getDouble(String path);
	abstract double getDouble(String path, double def);
	abstract List<Double> getDoubleList(String path);
	abstract List<Float> getFloatList(String path);
	abstract int getInt(String path);
	abstract int getInt(String path, int def);
	abstract List<Integer> getIntegerList(String path);
	abstract Set<String> getKeys(boolean deep);
	abstract List<?> getList(String path);
	abstract List<?> getList(String path, List<?> def);
	abstract long getLong(String path);
	abstract long getLong(String path, long def);
	abstract List<Long> getLongList(String path);
	abstract List<Map<?,?>> getMapList(String path);
	abstract ConfigurationSection getParent();
	abstract YamlStorage getRoot();
	abstract List<Short> getShortList(String path);
	abstract String getString(String path);
	abstract String getString(String path, String def);
	abstract List<String> getStringList(String path);
	abstract Map<String,Object> getValues(boolean deep);
	abstract boolean isBoolean(String path);
	abstract boolean isConfigurationSection(String path);
	abstract boolean isDouble(String path);
	abstract boolean isInt(String path);
	abstract boolean isList(String path);
	abstract boolean isLong(String path);
	abstract boolean isSet(String path);
	abstract boolean isString(String path);
	abstract void set(String path, Object value);

}
