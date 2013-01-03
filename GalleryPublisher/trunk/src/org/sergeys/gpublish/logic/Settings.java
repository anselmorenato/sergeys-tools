package org.sergeys.gpublish.logic;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Settings {

	public static final String SETTINGS_PATH = ".GalleryPublisher";
	public static final String SETTINGS_FILE = "settings.xml";

	private static String settingsDirPath;
	private static String settingsFilePath;
	private Properties resources = new Properties();
	private Dimension winPosition = new Dimension();
	private Dimension winSize = new Dimension();

	private String lastFilechooserLocation = "";

	private static Settings instance = new Settings();

	private static Logger logger;

	static {
		settingsDirPath = System.getProperty("user.home") + File.separator
				+ SETTINGS_PATH;
		settingsFilePath = settingsDirPath + File.separator + SETTINGS_FILE;

		File dir = new File(settingsDirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// log4j
		try {
			String logproperties = settingsDirPath + File.separator
					+ "log4j.properties";
			if (!new File(logproperties).exists()) {

				InputStream is = Settings.class
						.getResourceAsStream("/resources/log4j.properties");
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
			String conf = settingsDirPath + File.separator + "log4j.properties";
			File confFile = new File(conf);
			System.setProperty("log4j.configuration", confFile.toURI()
					.toString());
			System.setProperty("log4j.log.dir", settingsDirPath);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// slf4j logging
		logger = LoggerFactory.getLogger("gallerypublisher");

		load();

	}

	// Singleton must have private constructor
	// however public constructor required for XML serialization
	public Settings() {
	}

	public static synchronized Settings getInstance() {
		return instance;
	}

	public static String getSettingsDirPath() {
		return settingsDirPath;
	}

	public static void setSettingsDirPath(String settingsDirPath) {
		Settings.settingsDirPath = settingsDirPath;
	}

	public static Logger getLogger() {
		return logger;
	}

	private void setDefaults() {
		// TODO screen center
		winSize.setSize(800.0, 600.0);
		winPosition.setSize(50.0, 50.0);
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
		} else {
			instance.setDefaults();
		}

		InputStream is = Settings.class
				.getResourceAsStream("/resources/settings.properties");
		try {
			instance.resources.load(is);
		} catch (Exception e) {
			Settings.getLogger().error("failed to load properties, exit", e);
			System.exit(1);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				Settings.getLogger().error("", e);
			}
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

	public String getLastFilechooserLocation() {
		return lastFilechooserLocation;
	}

	public void setLastFilechooserLocation(String lastFilechooserLocation) {
		this.lastFilechooserLocation = lastFilechooserLocation;
	}

}