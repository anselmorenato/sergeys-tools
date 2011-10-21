package org.sergeys.coverfinder.logic;

public class Settings {
	
	private static Settings instance;
	
	// singleton
	private Settings(){
		
	}

	public static Settings getInstance(){
		return instance;		
	}
}
