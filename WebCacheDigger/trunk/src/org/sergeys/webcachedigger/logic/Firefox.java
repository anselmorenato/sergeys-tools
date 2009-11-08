package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Firefox implements IBrowser {
	
	private List<String> paths;

	public Firefox() throws Exception{
		paths = getDefaultCachePaths();
	}
	
	private ArrayList<String> getDefaultCachePaths() throws Exception {
		ArrayList<String> paths = new ArrayList<String>();
		
		//String userName = System.getProperty("user.name");
		String userHome = System.getProperty("user.home");
		//String userDir = System.getProperty("user.dir");
		
		//paths.add("username: " + userName);
		//paths.add("userhome: " + userHome);
		//paths.add("userdir: " + userDir);
				
		// TODO: Windows specific path
		String profilesDirPath = userHome + File.separator + "Local Settings\\Application Data\\Mozilla\\Firefox\\Profiles"; 
		File profilesDir = new File(profilesDirPath);
		if(!profilesDir.isDirectory()){
			throw new Exception(String.format("'%s' is not a directory.", profilesDirPath));
		}
		
		List<File> profiles = Arrays.asList(profilesDir.listFiles(new FileFilter(){
			public boolean accept(File file) {
				return file.isDirectory();
			}
		}));
		for(File profile : profiles){
			paths.add(String.format("%s\\Cache", profile.getAbsolutePath()));
		}
		
		return paths;
	}
	
	@Override
	public List<File> collect() throws Exception {
		
		ArrayList<File> files = new ArrayList<File>();
		
		for(String path : paths){
			
			File directory = new File(path);
			
			if(directory.isDirectory()){
				
				List<File> dirFiles = Arrays.asList(directory.listFiles(new FileFilter(){
					public boolean accept(File file) {
						return (!file.isDirectory()) && !file.getName().toLowerCase().startsWith("_cache_");
					}
				}));
				
				files.addAll(dirFiles);
			}
			else{
				// TODO: log warning
				throw new Exception(String.format("'%s' is not a directory.", path));
			}
			
		}
		
		return files; //sb.toString();		
	}

}


