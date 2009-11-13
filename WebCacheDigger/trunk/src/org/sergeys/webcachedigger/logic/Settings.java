package org.sergeys.webcachedigger.logic;

import java.util.Hashtable;

public class Settings {
	public static final String SAVE_SETTINGS_COMMAND = "SAVE_SETTINGS_COMMAND"; 
	
	private Hashtable<String, String> settings = new Hashtable<String, String>();
	
	public String getValue(String key){
		return settings.get(key);
	}
	
	public void setValue(String key, String value){
		settings.put(key, value);
	}
}
