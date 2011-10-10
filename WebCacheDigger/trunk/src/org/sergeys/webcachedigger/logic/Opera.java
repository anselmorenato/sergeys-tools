package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Opera extends AbstractBrowser {

	@Override
	public String getName() {		
		return "Opera";
	}

	@Override
	public String getScreenName() {
		return "Opera";
	}

	private ImageIcon icon;
	
	@Override
	public ImageIcon getIcon() {
		
		if(icon == null){
			icon = new ImageIcon(this.getClass().getResource("/images/opera.png")); 
		}
		return icon;
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
					return (!pathname.getName().equals("revocation") && 
							!pathname.getName().equals("sesn") &&
							!pathname.getName().equals("CACHEDIR.TAG") &&
							!pathname.getName().equals("dcache4.url") 
							);
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

	@Override
	protected List<File> collectExistingCachePaths() throws Exception {
		ArrayList<File> existingPaths = new ArrayList<File>();
		
		String path;
		File f;
		
//		// windows
//		if(System.getenv("LOCALAPPDATA") != null){
//			path = System.getenv("LOCALAPPDATA") + File.separator +
//					"Google" + File.separator + "Chrome" + File.separator + "User Data" + File.separator +
//					"Default" + File.separator + "Cache" + File.separator;
//			
//			System.out.println("chrome path to search: " + path);
//			f = new File(path); 
//			if(f.isDirectory()){
//				existingPaths.add(f);
//			}
//		}
		
		// macos		
		path = System.getProperty("user.home") + File.separator + 
				"Library" + File.separator + "Caches" + File.separator + "Opera" + File.separator + "cache";
		
		System.out.println("opera path to search: " + path);
		f = new File(path);
		if(f.isDirectory()){
			existingPaths.add(f);
		}
		
		// linux
		path = System.getProperty("user.home") + File.separator + 
				".opera" + File.separator + "cache";
		System.out.println("chrome path to search: " + path);
		f = new File(path);
		if(f.isDirectory()){
			existingPaths.add(f);
		} 
		
		return existingPaths;
	}

}
