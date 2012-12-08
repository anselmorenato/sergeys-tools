package org.sergeys.webcachedigger.logic;

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
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.ServiceLoader;

import org.sergeys.library.OsUtils;

public class Settings
extends Properties
{
    public enum FileType { Image, Audio, Video, Other };
    public enum CompareFilesType { Fast, Full };

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

    // Settings
    private String saveToPath;
    private long minFileSizeBytes = 500000;
    private String externalPlayerCommand;
    private String language; // language code for Locale class
    private boolean renameMp3byTags = true;
    private boolean excludeSavedAndIgnored = false;
    private String mp3tagsLanguage;
    private CompareFilesType compareFilesMethod = CompareFilesType.Fast;
    private HashSet<String> activeBrowsers = new HashSet<String>();    // browser names
    private EnumSet<FileType> activeFileTypes = EnumSet.noneOf(FileType.class);
    private boolean firstRun = true;
    private Properties resources = new Properties();
    private Date savedVersion = new Date(0);
    private String libVlc;
    private String lookAndFeel;

    private static Settings instance = new Settings();

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


    // Singleton must have private constructor
    // public constructor required for XML serialization, do not use it
    public Settings(){

    }

    public static synchronized Settings getInstance(){
        return instance;
    }


    // http://java.sys-con.com/node/37550
    // http://www.java2s.com/Code/Java/JDK-6/MarshalJavaobjecttoxmlandoutputtoconsole.htm

    public static void save() throws FileNotFoundException{

        instance.savedVersion = instance.getCurrentVersion();

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

    /**
     * Replaces instance
     */
    public static void load() {

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

        InputStream is = Settings.class.getResourceAsStream("/resources/settings.properties");
        try {
            instance.resources.load(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            try {
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * If forceAll, then all reset to default values.
     * If not, only illegal values are set to defaults.
     *
     * @param forceAll
     */
    private void setDefaults(){

        //minFileSizeBytes = 500000;
        language = Locale.getDefault().getLanguage();
        activeFileTypes.clear();
        activeFileTypes.add(FileType.Audio);
        activeFileTypes.add(FileType.Video);

        //renameMp3byTags = true;
        //excludeAlreadySaved = false;


        //compareFilesMethod = CompareFilesType.Fast;

        for(IBrowser b: getSupportedBrowsers()){
            activeBrowsers.add(b.getName());
        }

        if(OsUtils.isWindows()){
            externalPlayerCommand = "\"" + System.getenv("ProgramFiles") +
                    File.separator + "Windows Media Player" + File.separator +"wmplayer.exe\" " + Settings.EXT_PLAYER_FILEPATH;
        }
        else if(OsUtils.isMacOSX()){
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

//    public static boolean isOSWindows(){
//        return System.getenv("OS") != null && System.getenv("OS").equals("Windows_NT");
//    }
//
//    public static boolean isOSMacOSX(){
//        //return System.getenv("OSTYPE") != null && System.getenv("OSTYPE").startsWith("darwin");
//        return new File("/Applications/System preferences.app").exists();
//    }

    public String getLanguage() {
//        if(language == null){    // causes XMLEncoder to skip this value??
//            language = Locale.getDefault().getLanguage();
//        }

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

    public boolean isExcludeSavedAndIgnored() {
        return excludeSavedAndIgnored;
    }

    public void setExcludeSavedAndIgnored(boolean excludeAlreadySaved) {
        this.excludeSavedAndIgnored = excludeAlreadySaved;
    }

    public String getMp3tagsLanguage() {
        return mp3tagsLanguage;
    }

    public void setMp3tagsLanguage(String mp3tagsLanguage) {
        this.mp3tagsLanguage = mp3tagsLanguage;
    }

    public CompareFilesType getCompareFilesMethod() {
        return compareFilesMethod;
    }

    public void setCompareFilesMethod(CompareFilesType compareFilesMethod) {
        this.compareFilesMethod = compareFilesMethod;
    }


    public String getVersionDisplay(){
        return resources.getProperty("version.display", "");
    }

    public Date getCurrentVersion(){

        String ver = resources.getProperty("version", "");

        String[] tokens = ver.split("-");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(Integer.valueOf(tokens[0]),
                Integer.valueOf(tokens[1]) - 1,    // month is 0 bazed
                Integer.valueOf(tokens[2]),
                Integer.valueOf(tokens[3]),
                Integer.valueOf(tokens[4]));
        Date date = cal.getTime();

        return date;
    }

    public Date getSavedVersion() {
        return savedVersion;
    }

    public void setSavedVersion(Date savedVersion) {
        this.savedVersion = savedVersion;
    }

	public String getLibVlc() {
		return libVlc;
	}

	public void setLibVlc(String libVlc) {
		this.libVlc = libVlc;
	}

	public String getLookAndFeel() {
		return lookAndFeel;
	}

	public void setLookAndFeel(String lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}
}



