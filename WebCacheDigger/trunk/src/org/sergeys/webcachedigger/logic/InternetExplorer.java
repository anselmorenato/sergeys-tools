package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * IE on Windows
 * 
 * @author sergeys
 * 
 */
public class InternetExplorer extends AbstractBrowser {

	private List<File> listFilesRecursive(File directory){
		ArrayList<File> allFiles = new ArrayList<File>();
		
		
		if(directory.isDirectory()){
						
			// collect regular files
			List<File> files = Arrays.asList(directory
					.listFiles(new FileFilter() {
						public boolean accept(File file) {
							return (!file.isDirectory() && 
									!file.getName().toLowerCase().equals("index.dat") && 
									!file.getName().toLowerCase().equals("desktop.ini"));
						}
					}));
			
			allFiles.addAll(files);
			
			// process subdirs
			List<File> subdirs = Arrays.asList(directory
					.listFiles(new FileFilter() {
						public boolean accept(File file) {
							return (file.isDirectory() && 
									!file.getName().toLowerCase().equals("antiphishing"));	// ie9 on win7
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
	protected List<String> collectCachePaths() throws Exception {
		ArrayList<String> paths = new ArrayList<String>();

		// see IE settings for path
		if(System.getenv("LOCALAPPDATA") != null){
			String path = System.getenv("LOCALAPPDATA") + File.separator +
					"Microsoft" + File.separator + "Windows" + File.separator + "Temporary Internet Files";	// win7 ie9
			
			paths.add(path);
		}

		return paths;
	}

	@Override
	public List<CachedFile> collectCachedFiles()
			throws Exception {
		ArrayList<CachedFile> files = new ArrayList<CachedFile>();

		int minFileSize = settings.getIntProperty(Settings.MIN_FILE_SIZE_BYTES);

		for (String path : this.getCachePaths()) {
			File directory = new File(path);

			if (directory.isDirectory()) {

				List<File> dirFiles = listFilesRecursive(directory);
				
				for (File file : dirFiles) {
					if (file.length() > minFileSize) {
						files.add(new CachedFile(file.getAbsolutePath()));
					}
				}

			} else {
				// TODO: log warning
				throw new Exception(String.format("'%s' is not a directory.",
						path));
			}

		}

		return files;
	}

	@Override
	public String getName() {
		return "Internet Explorer";
	}	
}
