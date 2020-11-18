package me.tudedude.spigotstorageloader.interfaces;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.tudedude.spigotstorageloader.yaml.ConfigurationSection;
import me.tudedude.spigotstorageloader.yaml.YamlStorage;

public class YAMLInterface implements StorageInterface {

	private String name;
	private JavaPlugin plugin;
	private File file;
	private YamlStorage configuration;
	
	// YAMLInterface config = new YAMLInterface(this, "config", new File(getDataFolder(), "config.yml"));
	
	public YAMLInterface(JavaPlugin pl, String n, File f) {
		name = n;
		plugin = pl;
		file = f;
		configuration = new YamlStorage(pl);
	}
	
	public YAMLInterface(JavaPlugin pl, String n, File f, File defaultFile) {
		name = n;
		file = f;
		configuration = new YamlStorage(pl);
		
	}

	@Override
	public void init() {
		try {
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				writeToFile("");
			}
			configuration.load(file);
		}catch(Exception e) {
			plugin.getLogger().severe("{SpigotStorageLoader} Could not load file " + file.getAbsolutePath());
			e.printStackTrace();
		}
	}

	public void init(File defaultFile) {
		try {
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				if(defaultFile == null)writeToFile("");
				else writeToFile(defaultFile);
			}
			configuration.load(file);
		}catch(Exception e) {
			plugin.getLogger().severe("{SpigotStorageLoader} Could not load file " + file.getAbsolutePath() + " from File");
			e.printStackTrace();
		}
	}
	
	public void init(String defaultPath) {
		try {
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				if(defaultPath == null)writeToFile("");
				else plugin.saveResource(defaultPath, false);
			}
			configuration.load(file);
		}catch(Exception e) {
			plugin.getLogger().severe("{SpigotStorageLoader} Could not load file " + file.getAbsolutePath() + " from String path");
			e.printStackTrace();
		}
	}
	
	public void init(InputStream defaultStream) {
		try {
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				if(defaultStream == null)writeToFile("");
				else writeToFile(defaultStream);
			}
			configuration.load(file);
		}catch(Exception e) {
			plugin.getLogger().severe("{SpigotStorageLoader} Could not load file " + file.getAbsolutePath() + " from FileInputStream");
			e.printStackTrace();
		}
	}
	
	private void writeToFile(String content) throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(content);
		writer.close();
	}
	
	private void writeToFile(InputStream is) throws Exception{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		String line = reader.readLine();
		do {
			Bukkit.getServer().broadcastMessage("WRITING " + line);
			writer.write(line + "\n");
			writer.newLine();
			line = reader.readLine();
		}while(line != null);
		reader.close();
		writer.close();
	}
	
	private void writeToFile(File content) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(content));
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			String line = reader.readLine();
			do {
				Bukkit.getServer().broadcastMessage("WRITING " + line);
				writer.write(line + "\n");
				writer.newLine();
				line = reader.readLine();
			}while(line != null);
			reader.close();
			writer.close();
		}catch(Exception e) {
			plugin.getLogger().severe("{SpigotStorageLoader} Could not write to file, printing stack trace:");
			e.printStackTrace();
			plugin.getLogger().severe("File writing to: " + file.getAbsolutePath() + "; File writing from: " + content.getAbsolutePath());
		}
	}

	public boolean contains(String path){
		return configuration.contains(path);
	}

	public ConfigurationSection createSection(String path){
		return configuration.createSection(path);
	}

	public Object get(String path){
		return configuration.get(path);
	}

	public Object get(String path, Object def){
		return configuration.get(path, def);
	}

	public boolean getBoolean(String path){
		return configuration.getBoolean(path);
	}

	public boolean getBoolean(String path, boolean def){
		return configuration.getBoolean(path, def);
	}

	public List<Boolean> getBooleanList(String path){
		return configuration.getBooleanList(path);
	}

	public List<Byte> getByteList(String path){
		return configuration.getByteList(path);
	}

	public List<Character> getCharacterList(String path){
		return configuration.getCharacterList(path);
	}

	public ConfigurationSection getConfigurationSection(String path){
		return configuration.getConfigurationSection(path);
	}

	public String getCurrentPath(){
		return configuration.getCurrentPath();
	}

	public double getDouble(String path){
		return configuration.getDouble(path);
	}

	public double getDouble(String path, double def){
		return configuration.getDouble(path, def);
	}

	public List<Double> getDoubleList(String path){
		return configuration.getDoubleList(path);
	}

	public List<Float> getFloatList(String path){
		return configuration.getFloatList(path);
	}

	public int getInt(String path){
		return configuration.getInt(path);
	}

	public int getInt(String path, int def){
		return configuration.getInt(path, def);
	}

	public List<Integer> getIntegerList(String path){
		return configuration.getIntegerList(path);
	}

	public Set<String> getKeys(boolean deep){
		return configuration.getKeys(deep);
	}

	public List<?> getList(String path){
		return configuration.getList(path);
	}

	public List<?> getList(String path, List<?> def){
		return configuration.getList(path, def);
	}

	public long getLong(String path){
		return configuration.getLong(path);
	}

	public long getLong(String path, long def){
		return configuration.getLong(path, def);
	}

	public List<Long> getLongList(String path){
		return configuration.getLongList(path);
	}

	public List<Map<?,?>> getMapList(String path){
		return configuration.getMapList(path);
	}

	public String getName(){
		return this.name;
	}

	public ConfigurationSection getParent(){
		return configuration.getParent();
	}

	public YamlStorage getRoot(){
		return configuration.getRoot();
	}

	public List<Short> getShortList(String path){
		return configuration.getShortList(path);
	}

	public String getString(String path){
		return configuration.getString(path);
	}

	public String getString(String path, String def){
		return configuration.getString(path, def);
	}

	public List<String> getStringList(String path){
		return configuration.getStringList(path);
	}

	public Map<String,Object> getValues(boolean deep){
		return configuration.getValues(deep);
	}

	public boolean isBoolean(String path){
		return configuration.isBoolean(path);
	}

	public boolean isColor(String path){
		return configuration.isColor(path);
	}

	public boolean isConfigurationSection(String path){
		return configuration.isConfigurationSection(path);
	}

	public boolean isDouble(String path){
		return configuration.isDouble(path);
	}

	public boolean isInt(String path){
		return configuration.isInt(path);
	}

	public boolean isList(String path){
		return configuration.isList(path);
	}

	public boolean isLong(String path){
		return configuration.isLong(path);
	}

	public boolean isSet(String path){
		return configuration.isSet(path);
	}

	public boolean isString(String path){
		return configuration.isString(path);
	}

	public void set(String path, Object value){
		configuration.set(path, value);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
