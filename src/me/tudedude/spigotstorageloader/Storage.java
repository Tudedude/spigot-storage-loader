package me.tudedude.spigotstorageloader;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.tudedude.spigotstorageloader.interfaces.StorageInterface;
import me.tudedude.spigotstorageloader.interfaces.YAMLInterface;
import me.tudedude.spigotstorageloader.interfaces.StorageInterface.Type;

public class Storage {
	
	HashMap<String, StorageInterface> interfaces;
	JavaPlugin plugin;
	
	private static Type DEFAULT_TYPE = Type.YAML;
	
	public Storage(JavaPlugin pl) {
		interfaces = new HashMap<String, StorageInterface>();
		plugin = pl;
	}
	
	public boolean init(String name) {
		plugin.getLogger().info("Attempting to initialize storage " + name);
		return init(name, DEFAULT_TYPE);
	}
	
	public StorageInterface get(String path) {
		return interfaces.get(path);
	}
	
	public boolean init(String name, Type type) {
		plugin.getLogger().info("Attempting to initialize storage " + name + ";" + type.toString());
		switch(type) {
			case YAML:
				plugin.getLogger().info("Initializing " + name + ";" + interfaces.size());
				String[] path = (name.contains(".") ? name.split(".") : new String[] {name});
				String fileName = path[path.length-1];
//				String filePath = String.join("/", path) + ".yml";
				File f = new File(plugin.getDataFolder(), fileName + ".yml");
				YAMLInterface yi = new YAMLInterface(plugin, name, f);
				if(plugin.getResource(fileName + ".yml") == null) {
					yi.init();
				}else {
					yi.init(plugin.getResource(fileName + ".yml"));
				}
				interfaces.put(name, yi);
				plugin.getLogger().info("Initialized " + name + ";" + interfaces.size());
				return true;
			default:
				plugin.getLogger().info("FELL BACK TO DEFAULT");
				break;
		}
		return false;
	}
	
	public boolean init(String name, File f) {
		plugin.getLogger().info("REACHED STUB INIT");
		return false;
	}

}
