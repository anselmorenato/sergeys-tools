package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// where to save
	public static final String SETTINGS_PATH = ".WebCacheDigger";
	public static final String SETTINGS_FILE = "settings.properties";
	
	// action names for events
	public static final String COMMAND_SAVE_SETTINGS = "COMMAND_SAVE_SETTINGS"; 
	
	// property keys
	public static final String SAVE_TO_PATH = "SAVE_TO_PATH";
	public static final String MIN_FILE_SIZE_BYTES = "MIN_FILE_SIZE_BYTES";
	public static final String WINDOW_X = "WINDOW_X";
	public static final String WINDOW_Y = "WINDOW_Y";
	public static final String WINDOW_W = "WINDOW_W";
	public static final String WINDOW_H = "WINDOW_H";
	public static final String SPLITTER_POS = "SPLITTER_POS";
	
//	private Hashtable<String, String> settings = new Hashtable<String, String>();
//	
//	public String getValue(String key){
//		return settings.get(key);
//	}
//	
//	public void setValue(String key, String value){
//		settings.put(key, value);
//	}
	
	private static String settingsDirPath;
	private static String settingsFilePath; 	
	
	static{
		settingsDirPath = System.getProperty("user.home") + File.separator 
			+ SETTINGS_PATH;
		settingsFilePath = settingsDirPath + File.separator + SETTINGS_FILE;

	}
	
	/**
	 * @return the settingsFilePath
	 */
	public static String getSettingsFilePath() {
		return settingsFilePath;
	}

	private Settings(){		
	}
	
	public void save() throws IOException{
		
		File dir = new File(settingsDirPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		FileOutputStream fos = new FileOutputStream(settingsFilePath);
		this.store(fos, null);
	}
	
	public static Settings load() throws IOException{
		Settings settings = new Settings();
		
		try {
			settings.load(new FileInputStream(settingsFilePath));
		} catch (FileNotFoundException e) {
			// no file, OK
			//e.printStackTrace();
		}
		
		return settings;
	}
	
	public int getIntProperty(String key){		
		//String value = getProperty(key);
		//return (value == null) ? null : Integer.parseInt(value);				
		return Integer.parseInt(getProperty(key));
	}

	public int getIntProperty(String key, int defaultValue){		
		if(!this.containsKey(key)){
			this.setIntProperty(key, defaultValue);			
		}
		return Integer.parseInt(getProperty(key));
	}
	
	public void setIntProperty(String key, int value){
		setProperty(key, String.valueOf(value));
	}
	
	public void setIntProperty(String key, String value){
		Integer.parseInt(value); // check for valid int
		setProperty(key, value);
	}
	
}

