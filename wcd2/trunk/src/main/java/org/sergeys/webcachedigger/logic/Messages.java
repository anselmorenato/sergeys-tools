package org.sergeys.webcachedigger.logic;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "resources.lang.messages"; //$NON-NLS-1$

	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);
	
	private Messages() {
	}
	
	//private static String currentLang = "?";
	
	public static void setLocale(Locale l){
		//currentLang = l.getLanguage();
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, l);		
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
