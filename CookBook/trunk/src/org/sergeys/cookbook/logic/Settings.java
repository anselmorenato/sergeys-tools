package org.sergeys.cookbook.logic;

import java.awt.Dimension;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class Settings {
	
    public static final String SETTINGS_PATH = ".CookBook";    
    public static final String SETTINGS_FILE = "settings.xml";
    
    public static final String RECIPES_SUBDIR = "recipes";

    private static String settingsDirPath;
    private static String settingsFilePath;

    private Properties resources = new Properties();
    private Dimension winPosition = new Dimension();
    private Dimension winSize = new Dimension();
    private double winDividerPosition = 0;
    private String lastFilechooserLocation = "";
    
    private static Settings instance = new Settings();

    static{
        settingsDirPath = System.getProperty("user.home") + File.separator + SETTINGS_PATH;
        settingsFilePath = settingsDirPath + File.separator + SETTINGS_FILE;

        load();
    }

    // Singleton must have private constructor
    // public constructor required for XML serialization, do not use it
    public Settings(){
    }

    public static synchronized Settings getInstance(){
        return instance;
    }

	public static String getSettingsDirPath() {
		return settingsDirPath;
	}

	public static void setSettingsDirPath(String settingsDirPath) {
		Settings.settingsDirPath = settingsDirPath;
	}
	
    public static void save() throws FileNotFoundException{

//        instance.savedVersion = instance.getCurrentVersion();

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

//            instance.firstRun = false;

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

	private void setDefaults() {
		Rectangle2D bounds = Screen.getPrimary().getBounds();
		
		winSize.setSize(bounds.getWidth() / 2, bounds.getHeight() / 2);
		winPosition.setSize(bounds.getWidth() / 4, bounds.getHeight() / 4);
	}

	public Dimension getWinPosition() {
		return winPosition;
	}

	public void setWinPosition(Dimension winPosition) {
		this.winPosition = winPosition;
	}

	public Dimension getWinSize() {
		return winSize;
	}

	public void setWinSize(Dimension winSize) {
		this.winSize = winSize;
	}

	public double getWinDividerPosition() {
		return winDividerPosition;
	}

	public void setWinDividerPosition(double winDividerPosition) {
		this.winDividerPosition = winDividerPosition;
	}

	public String getLastFilechooserLocation() {
		return lastFilechooserLocation;
	}

	public void setLastFilechooserLocation(String lastFilechooserLocation) {
		this.lastFilechooserLocation = lastFilechooserLocation;
	}


}
