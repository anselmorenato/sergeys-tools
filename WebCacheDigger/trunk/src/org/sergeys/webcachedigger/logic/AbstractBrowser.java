package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractBrowser implements IBrowser {

	protected Settings settings;
	protected List<File> cachePaths;
		
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
				
	public boolean isPresent() {
		boolean present = false;
						
		try {
			getExistingCachePaths();
			present = (cachePaths != null) ? (getExistingCachePaths().size() > 0) : false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return present;
	}

	protected abstract List<File> collectExistingCachePaths() throws Exception;
	
	protected List<File> getExistingCachePaths() throws Exception{
		if(cachePaths == null){
			cachePaths = collectExistingCachePaths();
		}
		
		return cachePaths;
	}	
	
	protected List<File> listFilesRecursive(File directory, final FileFilter fileFilter, List<File> allFiles){
						
		if(directory.isDirectory()){
						
			// collect regular files
			List<File> files = Arrays.asList(directory
					.listFiles(new FileFilter() {
						public boolean accept(File file) {
							return (!file.isDirectory() && fileFilter.accept(file));
						}
					}));
			
			allFiles.addAll(files);
			
			// process subdirs
			List<File> subdirs = Arrays.asList(directory
					.listFiles(new FileFilter() {
						public boolean accept(File file) {
							return (file.isDirectory() && fileFilter.accept(file));
						}
					}));
			
			for(File subdir: subdirs){
				listFilesRecursive(subdir, fileFilter, allFiles);
			}			
		}
		
		return allFiles;
	}
	
}
