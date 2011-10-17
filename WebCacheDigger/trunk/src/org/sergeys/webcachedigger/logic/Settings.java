package org.sergeys.webcachedigger.logic;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.ServiceLoader;

public class Settings 
extends Properties 
{
	public enum FileType { Image, Audio, Video, Other };
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// where to save
	public static final String SETTINGS_PATH = ".WebCacheDigger";
	//public static final String SETTINGS_FILE = "settings.properties";
	public static final String SETTINGS_FILE = "settings.xml";
	
	// action names for events
	public static final String COMMAND_SAVE_SETTINGS = "COMMAND_SAVE_SETTINGS"; 
	
	
	/**
	 * Placeholder for absolute file path for external player command line
	 */
	public static final String EXT_PLAYER_FILEPATH = "%f";
	
	// property keys: various internal values
	public static final String WINDOW_X = "WINDOW_X";
	public static final String WINDOW_Y = "WINDOW_Y";
	public static final String WINDOW_W = "WINDOW_W";
	public static final String WINDOW_H = "WINDOW_H";
	public static final String SPLITTER_POS = "SPLITTER_POS";
		
	private static String settingsDirPath;
	private static String settingsFilePath; 	

	// Settings edited by user, as get/set
	private String saveToPath;
	private long minFileSizeBytes;
	private String externalPlayerCommand;
	private String language; // language code for Locale class
	private boolean renameMp3byTags;
	private boolean excludeAlreadySaved;
	private String mp3tagsLanguage;
	
	private HashSet<String> activeBrowsers = new HashSet<String>();	// browser names
	
	private EnumSet<FileType> activeFileTypes = EnumSet.noneOf(FileType.class); 
	
	private boolean firstRun = true;
	
	static{
		settingsDirPath = System.getProperty("user.home") + File.separator + SETTINGS_PATH;
		settingsFilePath = settingsDirPath + File.separator + SETTINGS_FILE;
		
		load();
	}
	
	/**
	 * @return the settingsFilePath
	 */
	public static String getSettingsFilePath() {
		return settingsFilePath;
	}

	public static String getSettingsDirPath() {
		return settingsDirPath;
	}
	
	
	
	private static Settings instance;
	
	// singleton
	// public constructor required for XML serialization, do not use it
	public Settings(){		
	}
	
	public static synchronized Settings getInstance(){
		return instance;
	}


	// http://java.sys-con.com/node/37550
	// http://www.java2s.com/Code/Java/JDK-6/MarshalJavaobjecttoxmlandoutputtoconsole.htm
	
	//public void save() throws FileNotFoundException{	// fails to save lang??
	public static void save() throws FileNotFoundException{	
		
		
//System.out.println("saving lang " + instance.getLanguage());

		File dir = new File(settingsDirPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		XMLEncoder e;
				
		synchronized (instance) {
			e = new XMLEncoder(
			        new BufferedOutputStream(
			            new FileOutputStream(settingsFilePath)));
			e.writeObject(instance);
			e.close();			
		}
		
	}
	
	public static void load() {
		instance = new Settings();
					
		if(new File(settingsFilePath).exists()){
		
			FileInputStream is = null;
			try {
				is = new FileInputStream(settingsFilePath);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			XMLDecoder decoder = new XMLDecoder(is);
			instance = (Settings)decoder.readObject();
			decoder.close(); 
			
			instance.firstRun = false;					
			
//System.out.println("read lang " + instance.getLanguage());			
		}
		else{			
			instance.setDefaults();				
		}
	}
	
	private void setDefaults(){
		minFileSizeBytes = 500000;		
		language = Locale.getDefault().getLanguage();
		activeFileTypes = EnumSet.allOf(FileType.class);
		renameMp3byTags = true;
		excludeAlreadySaved = false;
		language = Locale.getDefault().getLanguage();
		
		for(IBrowser b: getSupportedBrowsers()){
			activeBrowsers.add(b.getName());
		}
		
		if(isOSWindows()){
			externalPlayerCommand = "\"" + System.getenv("ProgramFiles") +
					File.separator + "Windows Media Player" + File.separator +"wmplayer.exe\" " + Settings.EXT_PLAYER_FILEPATH;
		}		
		else if(isOSMacOSX()){
			// TODO: somehow launch itunes on macos?
			externalPlayerCommand = "";
		}
		else{
			externalPlayerCommand = "vlc " + Settings.EXT_PLAYER_FILEPATH;
		}		
	}
	
	public boolean isFirstRun(){
		return firstRun;
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

	public synchronized HashSet<String> getActiveBrowsers() {
		return activeBrowsers;
	}

	public synchronized void setActiveBrowsers(HashSet<String> activeBrowsers) {
		this.activeBrowsers = activeBrowsers;
	}

	public synchronized EnumSet<FileType> getActiveFileTypes() {
		return activeFileTypes;
	}

	public synchronized void setActiveFileTypes(EnumSet<FileType> activeFileTypes) {
		this.activeFileTypes = activeFileTypes;
	}

	public String getSaveToPath() {
		return saveToPath;
	}

	public void setSaveToPath(String saveToPath) {
		this.saveToPath = saveToPath;
	}

	public long getMinFileSizeBytes() {
		return minFileSizeBytes;
	}

	public void setMinFileSizeBytes(long minFileSizeBytes) {
		this.minFileSizeBytes = minFileSizeBytes;
	}

	public String getExternalPlayerCommand() {
		return externalPlayerCommand;
	}

	public void setExternalPlayerCommand(String externalPlayerCommand) {
		this.externalPlayerCommand = externalPlayerCommand;
	}
	
	public boolean isExternalPlayerConfigured(){
		boolean result = (getExternalPlayerCommand() != null && !getExternalPlayerCommand().isEmpty()); 
		return result;
	}
	
	public static boolean isOSWindows(){
		return System.getenv("OS") != null && System.getenv("OS").equals("Windows_NT");
	}

	public static boolean isOSMacOSX(){
		//return System.getenv("OSTYPE") != null && System.getenv("OSTYPE").startsWith("darwin");
		return new File("/Applications/System preferences.app").exists();
	}
	
	public String getLanguage() {
//		if(language == null){	// causes XMLEncoder to skip this value??
//			language = Locale.getDefault().getLanguage();
//		}
		
//System.out.println("gettings lang " + language);		
		
		return language;
	}

	public void setLanguage(String language) {
//System.out.println("settings lang " + language);		
		this.language = language;
	}
	
	
	public static synchronized LinkedHashSet<IBrowser> getSupportedBrowsers(){
		
		LinkedHashSet<IBrowser> existingBrowsers = new LinkedHashSet<IBrowser>();
		
		ServiceLoader<IBrowser> ldr = ServiceLoader.load(IBrowser.class);
		for(IBrowser browser : ldr){
			SimpleLogger.logMessage("Can handle " + browser.getName());  			
			existingBrowsers.add(browser);							
		}					
		
		return existingBrowsers;
	}

	public boolean isRenameMp3byTags() {
		return renameMp3byTags;
	}

	public void setRenameMp3byTags(boolean renameMp3byTags) {
		this.renameMp3byTags = renameMp3byTags;
	}

	public boolean isExcludeAlreadySaved() {
		return excludeAlreadySaved;
	}

	public void setExcludeAlreadySaved(boolean excludeAlreadySaved) {
		this.excludeAlreadySaved = excludeAlreadySaved;
	}

	public String getMp3tagsLanguage() {
		return mp3tagsLanguage;
	}

	public void setMp3tagsLanguage(String mp3tagsLanguage) {
		this.mp3tagsLanguage = mp3tagsLanguage;
	}

}


