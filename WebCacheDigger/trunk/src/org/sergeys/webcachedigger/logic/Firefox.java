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

import javax.swing.ImageIcon;

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
							
							// windows
							if(System.getenv("LOCALAPPDATA") != null){	
								// 7
								path = System.getenv("LOCALAPPDATA") + File.separator +
										"Mozilla" + File.separator + "Firefox" + File.separator + 										
										relPath + File.separator + "Cache";
								paths.add(path);
								System.out.println("path to search (win): " + path);
							}
							else if(System.getenv("USERPROFILE") != null){	
								// xp
								path = System.getenv("USERPROFILE") + File.separator +
										"Local Settings" + File.separator + "Application Data" + File.separator +
										"Mozilla" + File.separator + "Firefox" + File.separator + 										
										relPath + File.separator + "Cache";
								paths.add(path);
								System.out.println("path to search (win): " + path);
							}
							
							// macos
							path = System.getProperty("user.home") + File.separator + 
									"Library" + File.separator + "Caches" + File.separator + "Firefox" + File.separator + 
									relPath + File.separator + "Cache";
							paths.add(path);
							System.out.println("path to search (macos): " + path);
							
							// linux relative to .ini
							path = profilesIniPath + File.separator + 
									relPath + File.separator + "Cache";
							paths.add(path);
							System.out.println("path to search (linux): " + path);
							
						}
						else{
							// absolute
							path = path + File.separator + "Cache";
							paths.add(path);
							System.out.println("path to search (absolute): " + path);
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
	protected List<File> collectExistingCachePaths() throws Exception {

		ArrayList<String> possiblePaths = new ArrayList<String>();		
		List<File> existingPaths = new ArrayList<File>();
		
		// http://kb.mozillazine.org/Profile_folder
		String profilesiniPath = "";
		
		// windows
		if(System.getenv("APPDATA") != null){
			profilesiniPath = System.getenv("APPDATA") + File.separator +
					"Mozilla" + File.separator + "Firefox";
			possiblePaths.addAll(possibleCachePaths(profilesiniPath));
		}
		
		// macos		
		profilesiniPath = System.getProperty("user.home") + File.separator + 
				"Library" + File.separator + "Mozilla" + File.separator + "Firefox";
		possiblePaths.addAll(possibleCachePaths(profilesiniPath));
		profilesiniPath = System.getProperty("user.home") + File.separator + 
				"Library" + File.separator + "Application Support" + File.separator + "Firefox";
		possiblePaths.addAll(possibleCachePaths(profilesiniPath));
		
		// linux
		profilesiniPath = System.getProperty("user.home") + File.separator + ".mozilla" + File.separator + "firefox";
		possiblePaths.addAll(possibleCachePaths(profilesiniPath));
		
		for(String path: possiblePaths){
			File f = new File(path); 
			if(f.isDirectory()){
				existingPaths.add(f);
				System.out.println("Actual path to search: " + path);
			}
		}
				
		return existingPaths;		
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
	public List<CachedFile> collectCachedFiles(IProgressWatcher watcher) throws Exception {

		ArrayList<CachedFile> files = new ArrayList<CachedFile>();
		long minFileSize = settings.getMinFileSizeBytes();

		// 1. count files
		ArrayList<File> allFiles = new ArrayList<File>();

		for (File directory : getExistingCachePaths()) {

			if(!watcher.isAllowedToContinue()){
				return files;
			}				

			if (directory.isDirectory()) {												
				List<File> dirFiles = listFilesRecursive(directory);
				allFiles.addAll(dirFiles);			
			} else {
				SimpleLogger.logMessage(String.format("'%s' is not a directory", directory.getPath()));
			}
			
		}		
		
		// 2. collect every matching file
		for(File file: allFiles){
			
			if(!watcher.isAllowedToContinue()){
				return files;
			}				
			
			if(file.length() > minFileSize){				
				files.add(new CachedFile(file.getAbsolutePath()));
				watcher.progressStep();
			}						
		}		

		return files;
	}

	@Override
	public String getName() {
		return "Mozilla Firefox";
	}

	private ImageIcon icon = null;
	
	@Override
	public ImageIcon getIcon() {
		if(icon == null){
			icon = new ImageIcon(this.getClass().getResource("/images/firefox.png")); 
		}
		return icon;
	}

	@Override
	public String getScreenName() {
		return "Firefox";
	}

	

}
