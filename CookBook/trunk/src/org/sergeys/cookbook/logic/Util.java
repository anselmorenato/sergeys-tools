package org.sergeys.cookbook.logic;

import java.io.File;

public abstract class Util {
	
	public static void deleteRecursively(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File f: files){
				deleteRecursively(f);
			}
		}
		
		System.out.println("deleting " + file);
		if(!file.delete()){
			System.out.println("not deleted " + file);
			file.deleteOnExit();
		}
	}
}
