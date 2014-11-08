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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sergeys.library.OsUtils;

public class Settings
extends Properties
{
    public enum FileType { Image, Audio, Video, Other };
    public enum CompareFilesType { Fast, Full };
    public enum MediaPlayerType { Vlc, External };

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // where to save
    public static final String SETTINGS_PATH = ".WebCacheDigger";
    public static final String SETTINGS_FILE = "settings.xml";
    public static final String LOG_FILE = "log.txt";

    // action names for events
    public static final String COMMAND_SAVE_SETTINGS = "COMMAND_SAVE_SETTINGS";

    public static final String LOCK_FILE = "webcachedigger.applock";

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
    private Date savedVersionTimestamp = new Date(0);
    private String libVlc;
    private String lookAndFeel;
    private MediaPlayerType mediaPlayerType = MediaPlayerType.External;

    private static Settings instance = new Settings();

    private static Logger logger;

    static{
        settingsDirPath = System.getProperty("user.home") + File.separator + SETTINGS_PATH;
        settingsFilePath = settingsDirPath + File.separator + SETTINGS_FILE;

        File dir = new File(settingsDirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        // settings for log4j
//        extractResource("log4j.properties", false);
//        System.setProperty("log4j.configuration", new File(settingsDirPath + File.separator + "log4j.properties").toURI().toString());
//        System.setProperty("log4j.log.file", settingsDirPath + File.separator + LOG_FILE);

        // settings for log4j 2
        extractResource("log4j2.xml", false);
        System.setProperty("log4j.configurationFile", new File(settingsDirPath + File.separator + "log4j2.xml").toURI().toString());
        System.setProperty("log4j.log.file", settingsDirPath + File.separator + LOG_FILE);

        // slf4j logger
        logger = LoggerFactory.getLogger("webcachedigger");

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

    /**
     * Extracts file to the settings directory
     *
     * @param filename
     * @param overwrite
     */
    private static void extractResource(String filename, boolean overwrite){

        String targetfile = settingsDirPath + File.separator + filename;

        try{

            if(!overwrite && new File(targetfile).exists()){
                return;
            }

            InputStream is = Settings.class.getResourceAsStream("/resources/" + filename);
            if (is != null) {
                byte[] buf = new byte[20480];
                FileOutputStream fos = new FileOutputStream(targetfile);
                int count = 0;
                while ((count = is.read(buf)) > 0) {
                    fos.write(buf, 0, count);
                }
                fos.close();
                is.close();
            }
        }
        catch(IOException ex){
            if(logger != null){
                logger.error("Failed to extract data to " + targetfile, ex);
            }
            else{
                ex.printStackTrace(System.err);
            }
        }
    }


    // Singleton must have private constructor
    // public constructor required for XML serialization, do not use it
    public Settings(){

    }

    public static synchronized Settings getInstance(){
        return instance;
    }

    public static Logger getLogger() {
        return logger;
    }

    // http://java.sys-con.com/node/37550
    // http://www.java2s.com/Code/Java/JDK-6/MarshalJavaobjecttoxmlandoutputtoconsole.htm

    public static void save() throws FileNotFoundException {

        instance.savedVersionTimestamp = instance.getCurrentVersionTimestamp();

        File dir = new File(settingsDirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        XMLEncoder e;

        synchronized (instance) {
            try {
                e = new XMLEncoder(
                        new BufferedOutputStream(
                            new FileOutputStream(settingsFilePath)));

                e.writeObject(instance);
                e.close();
            } catch (FileNotFoundException e1) {
                if(logger != null){
                    logger.error("failed to save " + settingsFilePath, e1);
                }

                throw e1;
            }
        }
    }

    /**
     * Replaces instance
     */
    public static void load() {

        if(new File(settingsFilePath).exists()){
            try{
                FileInputStream is = new FileInputStream(settingsFilePath);

                XMLDecoder decoder = new XMLDecoder(is);
                instance = (Settings)decoder.readObject();
                decoder.close();

                instance.firstRun = false;
            }
            catch(FileNotFoundException e){
                if(logger != null){
                    logger.error("failed to open " + settingsFilePath, e);
                }
                else{
                    e.printStackTrace();
                }

                instance.setDefaults();
            }
        }
        else{
            instance.setDefaults();
        }

        InputStream is = Settings.class.getResourceAsStream("/resources/settings.properties");
        try {
            instance.resources.load(is);
        } catch (IOException e) {
            if(logger != null){
                logger.error("failed to load resources", e);
            }
            else{
                e.printStackTrace();
            }
        }
        finally{
            try {
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public static synchronized LinkedHashSet<IBrowser> getSupportedBrowsers(){

        LinkedHashSet<IBrowser> existingBrowsers = new LinkedHashSet<IBrowser>();

        ServiceLoader<IBrowser> ldr = ServiceLoader.load(IBrowser.class);
        for(IBrowser browser : ldr){
            logger.info("Can handle " + browser.getName());
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

    public Date getCurrentVersionTimestamp(){

        String ver = resources.getProperty("version.timestamp", "");

        String[] tokens = ver.split("-");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        try{
            cal.set(Integer.valueOf(tokens[0]),
                    Integer.valueOf(tokens[1]) - 1,    // month is 0 bazed
                    Integer.valueOf(tokens[2]),
                    Integer.valueOf(tokens[3]),
                    Integer.valueOf(tokens[4]));
        }
        catch(NumberFormatException ex){
            if(logger != null){
                logger.error("failed to parse timestamp", ex);
            }
            else{
                ex.printStackTrace();
            }

            cal = Calendar.getInstance();
        }
        Date date = cal.getTime();

        return date;
    }

    public Date getSavedVersionTimestamp() {
        return savedVersionTimestamp;
    }

    public void setSavedVersionTimestamp(Date savedVersion) {
        this.savedVersionTimestamp = savedVersion;
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

    public MediaPlayerType getMediaPlayerType() {
        return mediaPlayerType;
    }

    public void setMediaPlayerType(MediaPlayerType mediaPlayerType) {
        this.mediaPlayerType = mediaPlayerType;
    }
}



