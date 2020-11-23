package me.tudedude.spigotstorageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import me.tudedude.spigotstorageloader.interfaces.StorageInterface;
import me.tudedude.spigotstorageloader.interfaces.YAMLInterface;
import me.tudedude.spigotstorageloader.interfaces.StorageInterface.Type;

public class Storage {
	
	HashMap<String, StorageInterface> interfaces;
	JavaPlugin plugin;
	
	private static Type DEFAULT_TYPE = Type.YAML;
	
	private InputStream nextDefault = null;
	
	public Storage(JavaPlugin pl) {
		interfaces = new HashMap<String, StorageInterface>();
		plugin = pl;
	}
	
	public boolean init(String name) {
		return init(name, "", DEFAULT_TYPE);
	}
	
	public boolean init(String name, String path) {
		return init(name, path, DEFAULT_TYPE);
	}
	
	public StorageInterface get(String path) {
		return interfaces.get(path);
	}
	
	public boolean init(String name, Type type) {
		return false;
	}
	
	public Storage with(File f) {
		try {
			this.nextDefault = new FileInputStream(f);
		}catch(Exception e) {
			this.nextDefault = null;
			plugin.getLogger().warning("Could not find default file supplied!");
		}
		return this;
	}
	
	public Storage with(InputStream s) {
		this.nextDefault = s;
		return this;
	}
	
	public Storage with(String s) {
		InputStream def = plugin.getResource(s);
		this.nextDefault = def;
		return this;
	}
	
	public boolean init(String name, String path, Type type) {
		switch(type) {
			case YAML:
				String[] _path = (name.contains(".") ? name.split(".") : new String[] {name});
				String fileName = _path[_path.length-1];
				
				File f = new File(plugin.getDataFolder(), fileName + ".yml");
				YAMLInterface yi = new YAMLInterface(plugin, name, f);
				if(nextDefault != null) {
					yi.init(nextDefault);
					nextDefault = null;
				}else {
					if(plugin.getResource(fileName + ".yml") == null) {
						yi.init();
					}else {
						yi.init(plugin.getResource(fileName + ".yml"));
					}
				}
				interfaces.put(name, yi);
				return true;
			default:
				return false;
		}
	}

}
