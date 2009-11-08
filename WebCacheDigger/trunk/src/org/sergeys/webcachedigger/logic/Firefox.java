package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class Firefox implements IBrowser {

	@Override
	public ArrayList<String> getDefaultCachePaths() throws Exception {
		ArrayList<String> paths = new ArrayList<String>();
		
		String userName = System.getProperty("user.name");
		String userHome = System.getProperty("user.home");
		String userDir = System.getProperty("user.dir");
		
		//paths.add("username: " + userName);
		//paths.add("userhome: " + userHome);
		//paths.add("userdir: " + userDir);
				
		// TODO: Windows specific path
		String profilesDirPath = userHome + File.separator + "Local Settings\\Application Data\\Mozilla\\Firefox\\Profiles"; 
		File profilesDir = new File(profilesDirPath);
		if(!profilesDir.isDirectory()){
			throw new Exception(String.format("'%s' is not a directory.", profilesDirPath));
		}
		
		File[] profiles = profilesDir.listFiles(new DirectoryFileFilter());
		for(int i = 0; i < profiles.length; i++){
			paths.add(String.format("%s\\Cache", profiles[i].getAbsolutePath()));
		}
		
		return paths;
	}

}


