package org.sergeys.privoxytool.logic;

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
import java.util.Locale;
import java.util.Properties;

public class Settings {
	
    public static final String SETTINGS_PATH = ".PrivoxyTool";	// relative subdir    
    public static final String SETTINGS_FILE = "settings.xml";

    private static String settingsDirPath;
    private static String settingsFilePath;
    
    private String language; // language code for Locale class
    private Properties resources = new Properties();
    private Date savedVersion = new Date(0);    
    private boolean firstRun = true;
    
    private static Settings instance = new Settings();
    
    static{
        settingsDirPath = System.getProperty("user.home") + File.separator + SETTINGS_PATH;
        settingsFilePath = settingsDirPath + File.separator + SETTINGS_FILE;

        load();
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

    
    private void setDefaults(){

        //minFileSizeBytes = 500000;
        language = Locale.getDefault().getLanguage();

//        if(OsUtils.isWindows()){
//            externalPlayerCommand = "\"" + System.getenv("ProgramFiles") +
//                    File.separator + "Windows Media Player" + File.separator +"wmplayer.exe\" " + Settings.EXT_PLAYER_FILEPATH;
//        }
//        else if(OsUtils.isMacOSX()){
//            // TODO: somehow launch itunes on macos?
//            externalPlayerCommand = "";
//        }
//        else{
//            externalPlayerCommand = "vlc " + Settings.EXT_PLAYER_FILEPATH;
//        }
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isFirstRun() {
		return firstRun;
	}

	
    
}
