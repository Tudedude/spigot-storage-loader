package me.tudedude.spigotstorageloader.interfaces;

public class StorageInterface {
	
	public enum Type{
		YAML,
		H2,
		SQLITE,
		JSON,
		SQL,
		MONGO
	}
	
	private String name;

	public StorageInterface(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void init() {}

}
