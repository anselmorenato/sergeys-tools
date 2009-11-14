package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Settings extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SETTINGS_PATH = ".WebCacheDigger";
	public static final String SETTINGS_FILE = "settings.properties";
	
	// actions
	public static final String SAVE_SETTINGS_COMMAND = "SAVE_SETTINGS_COMMAND"; 
	
	// keys
	public static final String SAVE_TO_PATH = "SAVE_TO_PATH";
	
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
}
