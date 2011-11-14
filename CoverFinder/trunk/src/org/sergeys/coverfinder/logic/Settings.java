package org.sergeys.coverfinder.logic;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;

public class Settings {
	
	public enum DetectFilesMethod { Extension, MimeMagic/*, Mp3File*/ };
	
	public static final String SETTINGS_PATH = ".CoverFinder";	
	public static final String SETTINGS_FILE = "settings.xml";
	
	// action names for events
	public static final String COMMAND_SAVE_SETTINGS = "COMMAND_SAVE_SETTINGS";
	
	private static String settingsDirPath;
	private static String settingsFilePath; 	
	
	private static Settings instance = new Settings();
	
	static{
		settingsDirPath = System.getProperty("user.home") + File.separator + SETTINGS_PATH;
		settingsFilePath = settingsDirPath + File.separator + SETTINGS_FILE;
				
		load();
	}
	
	private Point windowLocation = null;
	private Dimension windowSize = null;
	private String language; // language code for Locale class
	//private CompareFilesMethod compareFilesMethod = CompareFilesMethod.Fast;
	private DetectFilesMethod detectFilesMethod = DetectFilesMethod.Extension;
	//private Collection<File> libraryPaths = Collections.synchronizedCollection(new ArrayList<File>());
	//private Collection<String> libraryPaths = new ArrayList<String>();
	private Set<String> libraryPaths = new HashSet<String>();
	private String searchEngineName;
	private boolean confirmFileEdit = true;
	private boolean backupFileOnSave = true;
	
	private Properties properties = new Properties();
	private Date savedVersion = new Date(0);
	
	
	
	// singleton
	// public constructor for XMLEncoder
	public Settings(){			
	}

	public static Settings getInstance(){
		return instance;		
	}

	
	public static void load() {
		
		if(new File(settingsFilePath).exists()){
		
			FileInputStream is = null;
			try {
				is = new FileInputStream(settingsFilePath);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try{
				XMLDecoder decoder = new XMLDecoder(is);
				instance = (Settings)decoder.readObject();
				decoder.close();												
			}
			catch(Exception ex){
				ex.printStackTrace();
				instance.setDefaults();
			}
			
//			instance.firstRun = false;					
		}
		else{			
			instance.setDefaults();				
		}
		
		InputStream is = Settings.class.getResourceAsStream("/resources/settings.properties");
		try {
			instance.properties.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// upgrade things if needed
		if(instance.getCurrentVersion().after(instance.getSavedVersion())){
			AcoustIdUtil.getInstance().checkFingerprintUtility(true);
		}
	}
	
	public static void checkDirectory(){
		File dir = new File(settingsDirPath);
		if(!dir.exists()){
			dir.mkdirs();
		}		
	}
	
	public static void save() throws FileNotFoundException{	
		
		instance.savedVersion = instance.getCurrentVersion();

		checkDirectory();
		
		XMLEncoder e;
				
		synchronized (instance) {
			e = new XMLEncoder(
			        new BufferedOutputStream(
			            new FileOutputStream(settingsFilePath)));
			e.writeObject(instance);
			e.close();			
		}
		
	}

	private Date currentVersion = null;
	private Date getCurrentVersion() {
		if(currentVersion == null){
			String ver = properties.getProperty("version", "");
			
			String[] tokens = ver.split("-");
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(Integer.valueOf(tokens[0]), 
					Integer.valueOf(tokens[1]) - 1,	// month is 0 bazed 
					Integer.valueOf(tokens[2]), 
					Integer.valueOf(tokens[3]),
					Integer.valueOf(tokens[4]));
			currentVersion = cal.getTime();
		}
		
		return currentVersion;
	}

	private void setDefaults() {
		setLanguage(Locale.getDefault().getLanguage());
		
		// set 1st defined image search
		ServiceLoader<IImageSearchEngine> ldr = ServiceLoader.load(IImageSearchEngine.class);
		setSearchEngineName(ldr.iterator().next().getName());		
	}

	public IImageSearchEngine getImageSearchEngine(){
		ServiceLoader<IImageSearchEngine> ldr = ServiceLoader.load(IImageSearchEngine.class);
		for(IImageSearchEngine engine: ldr){				
			if(engine.getName().equals(getSearchEngineName())){
				return engine;
			}
		}
		
		return null;
	}
	
	public Point getWindowLocation() {
		return windowLocation;
	}

	public void setWindowLocation(Point windowLocation) {
		this.windowLocation = windowLocation;
	}

	public Dimension getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(Dimension windowSize) {
		this.windowSize = windowSize;
	}

	public Date getSavedVersion() {
		return savedVersion;
	}

	public void setSavedVersion(Date savedVersion) {
		this.savedVersion = savedVersion;
	}

	public static String getSettingsDirPath() {		
		return settingsDirPath;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public DetectFilesMethod getDetectFilesMethod() {
		return detectFilesMethod;
	}

	public void setDetectFilesMethod(DetectFilesMethod detectFilesMethod) {
		this.detectFilesMethod = detectFilesMethod;
	}

	public Set<String> getLibraryPaths() {
		return libraryPaths;
	}

	public void setLibraryPaths(Set<String> libraryPaths) {
		this.libraryPaths = libraryPaths;
	}

	public String getSearchEngineName() {
		return searchEngineName;
	}

	public void setSearchEngineName(String searchEngineName) {
		this.searchEngineName = searchEngineName;
	}

	public boolean isConfirmFileEdit() {
		return confirmFileEdit;
	}

	public void setConfirmFileEdit(boolean confirmFileEdit) {
		this.confirmFileEdit = confirmFileEdit;
	}

	public boolean isBackupFileOnSave() {
		return backupFileOnSave;
	}

	public void setBackupFileOnSave(boolean backupFileOnSave) {
		this.backupFileOnSave = backupFileOnSave;
	}

}
