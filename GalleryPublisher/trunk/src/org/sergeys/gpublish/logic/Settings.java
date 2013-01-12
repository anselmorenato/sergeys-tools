package org.sergeys.gpublish.logic;

import java.awt.Dimension;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Settings {

    public static final String SETTINGS_PATH = ".GalleryPublisher";
    public static final String SETTINGS_FILE = "settings.xml";
    public static final String LOG_FILE = "log.txt";
    public static final String HTMLTEMPLATES_FILE = "htmltemplates.utf8.txt";

    private static String settingsDirPath;
    private static String settingsFilePath;
    private Properties resources = new Properties();
    private Dimension winPosition = new Dimension();
    private Dimension winSize = new Dimension();

    private String srcPostImagesDir = "";
    private String srcWallpapersDir = "";
    private String dstWallpapersDir = "";
    private String webPrefixPostImages = "";
    private String webPrefixWallpapers = "";

    private static Settings instance = new Settings();

    private static Logger logger;

    private Properties htmltemplates = new Properties();

    static {
        settingsDirPath = System.getProperty("user.home") + File.separator + SETTINGS_PATH;
        settingsFilePath = settingsDirPath + File.separator + SETTINGS_FILE;

        File dir = new File(settingsDirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // extract settings for log4j

        try {
            String logproperties = settingsDirPath + File.separator + "log4j.properties";
            if (!new File(logproperties).exists()) {

                InputStream is = Settings.class.getResourceAsStream("/resources/log4j.properties");
                if (is != null) {
                    byte[] buf = new byte[20480];
                    FileOutputStream fos = new FileOutputStream(logproperties);
                    int count = 0;
                    while ((count = is.read(buf)) > 0) {
                        fos.write(buf, 0, count);
                    }
                    fos.close();
                    is.close();
                }
            }

            // System.setProperty("log4j.debug", "true");
            File confFile = new File(logproperties);
            if(confFile.exists()){
                System.setProperty("log4j.configuration", confFile.toURI().toString());
                System.setProperty("log4j.log.file", settingsDirPath + File.separator + LOG_FILE);
            }
            else{
                System.err.println("log4j config file not found at " + logproperties);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // extract settings for java logging

        // looks like impossible to have java logging configurable under web start
        // http://lopica.sourceforge.net/faq.html#config-logger
/*
        try {
            String logproperties = settingsDirPath + File.separator + "logging.properties";
            if (!new File(logproperties).exists()) {

                InputStream is = Settings.class.getResourceAsStream("/resources/logging.properties");
                if (is != null) {
                    byte[] buf = new byte[20480];
                    FileOutputStream fos = new FileOutputStream(logproperties);
                    int count = 0;
                    while ((count = is.read(buf)) > 0) {
                        fos.write(buf, 0, count);
                    }
                    fos.close();
                    is.close();
                }
            }

            File confFile = new File(logproperties);
            if(confFile.exists()){
                // TODO webstart refuses to read this, try stream from url?
                System.setProperty("java.util.logging.config.file", confFile.getAbsolutePath());

                FileInputStream fis = new FileInputStream(confFile);
                LogManager.getLogManager().readConfiguration(fis);
                fis.close();
            }
            else{
                System.err.println("logger config file not found at " + logproperties);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
*/

        // slf4j logger

        logger = LoggerFactory.getLogger("gallerypublisher");

        // extract html templates
        try{
            String templatesfile = settingsDirPath + File.separator + HTMLTEMPLATES_FILE;
            if (!new File(templatesfile).exists()) {
                InputStream is = Settings.class.getResourceAsStream("/resources/" + HTMLTEMPLATES_FILE);
                if (is != null) {
                    byte[] buf = new byte[20480];
                    FileOutputStream fos = new FileOutputStream(templatesfile);
                    int count = 0;
                    while ((count = is.read(buf)) > 0) {
                        fos.write(buf, 0, count);
                    }
                    fos.close();
                    is.close();
                }
            }
        }
        catch(Exception ex){
            logger.error("failed to extract html templates", ex);
        }

        load();
    }

    // Singleton must have private constructor
    // however public constructor required for XML serialization
    public Settings() {
    }

    public static synchronized Settings getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static String getSettingsDirPath() {
        return settingsDirPath;
    }

    public static void setSettingsDirPath(String settingsDirPath) {
        Settings.settingsDirPath = settingsDirPath;
    }

    private void setDefaults() {
        // TODO screen center
        winSize.setSize(800.0, 600.0);
        winPosition.setSize(50.0, 50.0);

        srcPostImagesDir = "E:\\Untitled Export\\web";
        srcWallpapersDir = "E:\\Untitled Export\\wp";
        dstWallpapersDir = "E:\\Untitled Export\\wp\\wp-ready";
        webPrefixPostImages = "http://russos.ru/img";
        webPrefixWallpapers = "http://russos.ru/wp";
    }

    /**
     * Replaces instance
     */
    public static void load() {

        if (new File(settingsFilePath).exists()) {

            FileInputStream is = null;
            try {
                is = new FileInputStream(settingsFilePath);
            } catch (FileNotFoundException e) {
                Settings.getLogger().error("", e);
            }

            XMLDecoder decoder = new XMLDecoder(is);
            instance = (Settings) decoder.readObject();
            decoder.close();
            try {
                is.close();
            } catch (IOException e) {
                Settings.getLogger().error("", e);
            }
        } else {
            instance.setDefaults();
        }

        InputStream is = Settings.class.getResourceAsStream("/resources/settings.properties");
        try {
            instance.resources.load(is);
        } catch (Exception e) {
            Settings.getLogger().error("failed to load properties, exit", e);
//			System.exit(1);	// this will close java console, comment for now
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                Settings.getLogger().error("", e);
            }
        }

        // read html templates
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(settingsDirPath + File.separator + HTMLTEMPLATES_FILE),
                            //StandardCharsets.UTF_8));	// since 1.7!
                            Charset.forName("utf-8")));
            String line;
            while((line = br.readLine()) != null){
                if(!line.isEmpty() && !line.startsWith("#")){
                    String[] tokens = line.split("==");
                    if(tokens.length == 2){
                        String noescapes = tokens[1].replace("\\n", "\n");
                        noescapes = noescapes.replace("\\\"", "\"");
                        instance.htmltemplates.put(tokens[0], noescapes);
                    }
                    else if(tokens.length == 2){
                        instance.htmltemplates.put(tokens[0], "");
                    }
                    else{
                        logger.warn("invalid html template line, ignored: " + line);
                    }
                }
            }

            br.close();
        } catch (IOException e) {
            logger.error("failed to load html templates", e);
        }
    }

    public static void save() throws FileNotFoundException {
        XMLEncoder e;

        synchronized (instance) {
            e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(
                    settingsFilePath)));
            e.writeObject(instance);
            e.close();
        }
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

    public String getHtmlTemplate(String key){
        return htmltemplates.containsKey(key) ? htmltemplates.getProperty(key) : "[[[ template not found for " + key + " ]]]";
    }

    public Properties getResources(){
        return resources;
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

    public String getSrcPostImagesDir() {
        return srcPostImagesDir;
    }

    public void setSrcPostImagesDir(String srcPostImagesDir) {
        this.srcPostImagesDir = srcPostImagesDir;
    }

    public String getSrcWallpapersDir() {
        return srcWallpapersDir;
    }

    public void setSrcWallpapersDir(String srcWallpapersDir) {
        this.srcWallpapersDir = srcWallpapersDir;
    }

    public String getDstWallpapersDir() {
        return dstWallpapersDir;
    }

    public void setDstWallpapersDir(String dstWallpapersDir) {
        this.dstWallpapersDir = dstWallpapersDir;
    }

    public String getWebPrefixPostImages() {
        return webPrefixPostImages;
    }

    public void setWebPrefixPostImages(String webPrefixPostImages) {
        this.webPrefixPostImages = webPrefixPostImages;
    }

    public String getWebPrefixWallpapers() {
        return webPrefixWallpapers;
    }

    public void setWebPrefixWallpapers(String webPrefixWallpapers) {
        this.webPrefixWallpapers = webPrefixWallpapers;
    }
}
