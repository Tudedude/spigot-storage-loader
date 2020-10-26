package me.tudedude.spigotstorageloader.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.bukkit.Bukkit;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

public class YamlStorage extends ConfigurationSection{
	
	private final DumperOptions yamlOptions = new DumperOptions();
	private final LoaderOptions loaderOptions = new LoaderOptions();
	private final Representer yamlRepresenter = new YamlRepresenter();
	Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions, loaderOptions);
	
	public YamlStorage() {
		
	}
	
	public void load(File f) {
		Map<?, ?> in;
		try {
			loaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
			in = (Map<?, ?>)yaml.load(new FileInputStream(f));
		}catch(Exception e) {
			Bukkit.getLogger().severe("Could not load config from " + f.getAbsolutePath());
			e.printStackTrace();
			return;
		}
		
		if(in != null) {
			convertMapsToSections(in, this);
		}
	}
	
	public void loadFromString(String s) {
		Map<?, ?> in;
		try {
			loaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
			in = (Map<?, ?>)yaml.load(s);
		}catch(Exception e) {
			Bukkit.getLogger().severe("Could not load config from String");
			e.printStackTrace();
			return;
		}
		
		if(in != null) {
			convertMapsToSections(in, this);
		}
	}
	
	private void convertMapsToSections(Map<?, ?> input, ConfigurationSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();

            if (value instanceof Map) {
                convertMapsToSections((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
	}

}
