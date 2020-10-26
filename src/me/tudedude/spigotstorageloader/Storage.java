package me.tudedude.spigotstorageloader;

import java.io.File;
import java.util.HashMap;

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
		return init(name, DEFAULT_TYPE);
	}
	
	public boolean init(String name, Type type) {
		switch(type) {
			case YAML:
				String[] path = name.split(".");
				String fileName = path[path.length-1];
				File f = new File(plugin.getDataFolder(), fileName);
				YAMLInterface yi = new YAMLInterface(plugin, name, f);
				if(plugin.getResource(name + ".yml") == null) {
					yi.init();
				}else {
					yi.init(plugin.getResource(name + ".yml"));
				}
				break;
			default:
				
		}
		return false;
	}
	
	public boolean init(String name, File f) {
		
		return false;
	}

}
