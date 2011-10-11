package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * Google Chrome Windows
 * 
 * @author sergeys
 *
 */
public class Chrome extends AbstractBrowser {


	@Override
	public String getName() {
		return "Google Chrome";
	}

	@Override
	protected List<File> collectExistingCachePaths() throws Exception {		
		ArrayList<File> existingPaths = new ArrayList<File>();
		
		String path;
		File f;
		
		// windows
		if(System.getenv("LOCALAPPDATA") != null){
			// 7
			path = System.getenv("LOCALAPPDATA") + File.separator +
					"Google" + File.separator + "Chrome" + File.separator + "User Data" + File.separator +
					"Default" + File.separator + "Cache" + File.separator;
			
			System.out.println("chrome path to search: " + path);
			f = new File(path); 
			if(f.isDirectory()){
				existingPaths.add(f);
			}
		}
		else if(System.getenv("USERPROFILE") != null){
			// xp
			path = System.getenv("USERPROFILE") + File.separator +
					"Local Settings" + File.separator + "Application Data" + File.separator + 
					"Google" + File.separator + "Chrome" + File.separator + "User Data" + File.separator +
					"Default" + File.separator + "Cache" + File.separator;
			
			System.out.println("chrome path to search: " + path);
			f = new File(path); 
			if(f.isDirectory()){
				existingPaths.add(f);
			}
		}
		
		// macos		
		path = System.getProperty("user.home") + File.separator + 
				"Library" + File.separator + "Caches" + File.separator + "Google" + File.separator + 
				"Chrome" + File.separator + "Default" + File.separator + "Cache";
		
		System.out.println("chrome path to search: " + path);
		f = new File(path);
		if(f.isDirectory()){
			existingPaths.add(f);
		}
		
		// linux
		path = System.getProperty("user.home") + File.separator + 
				".cache" + File.separator + "google-chrome" + File.separator + "Default" + File.separator + "Cache";
		System.out.println("chrome path to search: " + path);
		f = new File(path);
		if(f.isDirectory()){
			existingPaths.add(f);
		} 
		
		return existingPaths;
	}

	@Override
	public List<CachedFile> collectCachedFiles(IProgressWatcher watcher) throws Exception {
		ArrayList<CachedFile> files = new ArrayList<CachedFile>();
		
		// 1. count files
		ArrayList<File> allFiles = new ArrayList<File>();
		
		for(File cacheDir: getExistingCachePaths()){
			listFilesRecursive(cacheDir, new FileFilter(){

				@Override
				public boolean accept(File pathname) {
					// TODO Auto-generated method stub
					return (!pathname.getName().equals("index") && !pathname.getName().startsWith("data_"));
				}}, allFiles);
		}
		
		// 2. filter
		int minFileSize = settings.getIntProperty(Settings.MIN_FILE_SIZE_BYTES);
		for(File file: allFiles){			
			if(file.length() > minFileSize){				
				files.add(new CachedFile(file.getAbsolutePath()));
				watcher.progressStep();
			}						
		}		
		
		return files;
	}

	private ImageIcon icon = null;
	
	@Override
	public ImageIcon getIcon() {
		if(icon == null){
			icon = new ImageIcon(this.getClass().getResource("/images/chrome.png")); 
		}
		return icon;
	}

	@Override
	public String getScreenName() {		
		return "Chrome";
	}

}
