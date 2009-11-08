package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class FileCollector {
	
	private ArrayList<String> paths;
	
	public FileCollector(ArrayList<String> paths){
		this.paths = paths;
	}
	
	public String collect() throws Exception{
		
		StringBuilder sb = new StringBuilder();
		
		for(Iterator<String> i = paths.iterator(); i.hasNext(); ){
			String path = i.next();
			File directory = new File(path);
			/*
			if(!directory.isDirectory()){
				//throw new Exception(String.format("'%1$s' is not a directory.", this.path));
				throw new Exception(String.format("'%s' is not a directory.", path));
			}
			*/
			sb.append(String.format("'%s'\n", directory.getAbsolutePath()));
		}
		
		return sb.toString();		
	}
}
