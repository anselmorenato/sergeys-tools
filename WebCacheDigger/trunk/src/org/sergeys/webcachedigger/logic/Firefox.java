package org.sergeys.webcachedigger.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.xml.stream.util.StreamReaderDelegate;

/**
 * FF 3.5 on Windows XP
 * 
 * @author sergeys
 *
 */
public class Firefox extends AbstractBrowser {

	private List<String> possibleCachePaths(String profilesIniPath){
		ArrayList<String> paths = new ArrayList<String>();
		
		BufferedReader rdr = null;
		
		String profilesini = profilesIniPath + File.separator + "profiles.ini";
		File f = new File(profilesini);
		if(!f.isFile()){
			System.out.println(profilesini + " is not a file");
			return paths;	// empty
		}
		
		try {
			rdr = new BufferedReader(new FileReader(profilesini));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();									
		}
		
		// read all profile locations
		
		String line = null;
		boolean inProfileSection = false;
		String path = "";
		boolean isRelative = false;
		String[] tokens;
		
		try {
			while((line = rdr.readLine()) != null){
				if(line.toLowerCase().startsWith("[profile")){
					inProfileSection = true;					
				}
				else if(line.toLowerCase().startsWith("path") && inProfileSection){
					tokens = line.split("=");
					path = tokens[1];
				}
				else if(line.toLowerCase().startsWith("isrelative") && inProfileSection){
					tokens = line.split("=");
					isRelative = tokens[1].equals("1");
				}
				else if(line.isEmpty() && inProfileSection){	// end of profile section
					inProfileSection = false;					
					
					if(!path.isEmpty()){
						String relPath = path.replace('/', File.separatorChar);
						
						if(isRelative){
														
//							String regexp = File.separator;
//							if(regexp.equals("\\")){
//								regexp = "\\\\";	// workaround backslash on Windows
//							}
//							//tokens = path.split(File.separator);	// TODO: fails!
//							tokens = relPath.split(regexp);
							
							if(System.getenv("LOCALAPPDATA") != null){	
								// windows
								path = System.getenv("LOCALAPPDATA") + File.separator +
										"Mozilla" + File.separator + "Firefox" + File.separator + 
										//"Profiles" + File.separator + tokens[tokens.length - 1] + File.separator + "Cache";
										relPath + File.separator + "Cache";
								paths.add(path);
								System.out.println("path to search: " + path);
							}
							
							// macos
							path = System.getProperty("user.home") + File.separator + 
									"Library" + File.separator + "Caches" + File.separator + "Firefox" + File.separator + 
									relPath + File.separator + "Cache";
							
							// *nixrelative to .ini
							path = profilesIniPath + File.separator + 
									relPath + File.separator + "Cache";
							paths.add(path);
							System.out.println("path to search: " + path);
							
						}
						else{
							// absolute
							path = path + File.separator + "Cache";
							paths.add(path);
							System.out.println("path to search: " + path);
						}												
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return paths;
	}

	@Override
	protected List<String> collectCachePaths() throws Exception {

		ArrayList<String> paths = new ArrayList<String>();


		// TODO: read profile paths from %APPDATA%\Mozilla\Firefox\profiles.ini
		
		
		// http://kb.mozillazine.org/Profile_folder
		String profilesiniPath = "";
		
		// windows
		if(System.getenv("APPDATA") != null){
			profilesiniPath = System.getenv("APPDATA") + File.separator +
					"Mozilla" + File.separator + "Firefox";
			paths.addAll(possibleCachePaths(profilesiniPath));
		}
		
		// macos		
		profilesiniPath = System.getProperty("user.home") + File.separator + 
				"Library" + File.separator + "Mozilla" + File.separator + "Firefox";
		paths.addAll(possibleCachePaths(profilesiniPath));
		profilesiniPath = System.getProperty("user.home") + File.separator + 
				"Library" + File.separator + "Application Support" + File.separator + "Firefox";
		paths.addAll(possibleCachePaths(profilesiniPath));
		
		// linux
		profilesiniPath = System.getProperty("user.home") + File.separator + ".mozilla" + File.separator + "firefox";
		paths.addAll(possibleCachePaths(profilesiniPath));
		
		return paths;
		
/*		
		// TODO: Windows specific path
		// Firefox 3 at XP, Windows 7 OK
		String profilesDirPath = userHome
				+ File.separator
				+ "Local Settings\\Application Data\\Mozilla\\Firefox\\Profiles";
		File profilesDir = new File(profilesDirPath);
		if (!profilesDir.isDirectory()) {
			throw new Exception(String.format("'%s' is not a directory.",
					profilesDirPath));
		}

		List<File> profiles = Arrays.asList(profilesDir
				.listFiles(new FileFilter() {
					public boolean accept(File file) {
						return file.isDirectory();
					}
				}));
		for (File profile : profiles) {
			paths.add(String.format("%s\\Cache", profile.getAbsolutePath()));
		}

		return paths;
*/		
	}

	private List<File> listFilesRecursive(File directory){
		ArrayList<File> allFiles = new ArrayList<File>();
		
		
		if(directory.isDirectory()){
						
			// collect regular files
			List<File> files = Arrays.asList(directory
					.listFiles(new FileFilter() {
						public boolean accept(File file) {
							return (!file.isDirectory() && !file.getName().toLowerCase().startsWith("_cache_"));
						}
					}));
			
			allFiles.addAll(files);
			
			// process subdirs
			List<File> subdirs = Arrays.asList(directory
					.listFiles(new FileFilter() {
						public boolean accept(File file) {
							return (file.isDirectory());
						}
					}));
			
			for(File subdir: subdirs){
				files = listFilesRecursive(subdir);
				allFiles.addAll(files);				
			}
			
//			SimpleLogger.logMessage("collected files in " + directory);
		}
		
		return allFiles;
	}
	
	@Override
	public List<CachedFile> collectCachedFiles() throws Exception {

		ArrayList<CachedFile> files = new ArrayList<CachedFile>();
		int minFileSize = settings.getIntProperty(Settings.MIN_FILE_SIZE_BYTES);

		// 1. count files
		ArrayList<File> allFiles = new ArrayList<File>();
		int totalFiles = 0;
		for (String path : getCachePaths()) {

			File directory = new File(path);

			if (directory.isDirectory()) {
								
//				List<File> dirFiles = Arrays.asList(directory
//						.listFiles(new FileFilter() {
//							public boolean accept(File file) {
//								return (!file.isDirectory())
//										&& !file.getName().toLowerCase()
//												.startsWith("_cache_");
//							}
//						}));
				
				List<File> dirFiles = listFilesRecursive(directory);
				allFiles.addAll(dirFiles);
				totalFiles += dirFiles.size();			
			} else {
				SimpleLogger.logMessage(String.format("'%s' is not a directory", path));
			}
			
//			notifyListenersOnFileCount(totalFiles);
		}		
		
		// 2. collect every matching file
		for(File file: allFiles){			
			if(file.length() > minFileSize){				
				files.add(new CachedFile(file.getAbsolutePath()));
			}
			
//			notifyListenersOnFileFound(new FileFoundEvent(file));			
		}
		
//		notifyListenersOnSearchComplete();

		return files;
	}

	@Override
	public String getName() {
		return "Firefox";
	}

	

}
