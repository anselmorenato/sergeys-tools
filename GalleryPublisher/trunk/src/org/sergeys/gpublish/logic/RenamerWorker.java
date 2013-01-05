package org.sergeys.gpublish.logic;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.SwingWorker;

import org.sergeys.gpublish.ui.MainWindow;

public class RenamerWorker extends SwingWorker<Void, Integer> {

	private MainWindow mainWindow;
	private StringBuilder sbHtml = new StringBuilder();
	
	// filename without ext -> list of resolutions; "sokol-05" -> ["1024x768", "1280x1024"]
	private HashMap<String, ArrayList<String>> wallpaperMap = new HashMap<String, ArrayList<String>>();
	
	public RenamerWorker(MainWindow mainWindow){
		this.mainWindow = mainWindow;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		// this method runs in background thread, cannot update ui controls here
				
		// copy wallpapers
		
		// check that folders actually exist
		File wpDstFolder = new File(Settings.getInstance().getDstWallpapersFolder());
		if(!wpDstFolder.exists() || !wpDstFolder.isDirectory()){
			Settings.getLogger().error("Dir does not exist or file is not a dir: " + wpDstFolder);
			return null;
		}				
		
		File wpSrcFolder = new File(Settings.getInstance().getSrcWallpapersFolder());
		if(!wpSrcFolder.exists() || !wpSrcFolder.isDirectory()){
			Settings.getLogger().error("Dir does not exist or file is not a dir: " + wpSrcFolder);
			return null;
		}
		
		File postImagesDir = new File(Settings.getInstance().getSrcPostImagesFolder());
		if(!postImagesDir.exists() || !postImagesDir.isDirectory()){
			Settings.getLogger().error("Dir does not exist or file is not a dir: " + wpSrcFolder);
			return null;
		}		
		
		String dstFolderName = wpDstFolder.getName().toLowerCase();			
		
		// find and process all wallpaper subdirs
		
		File[] subdirs = wpSrcFolder.listFiles(new FileFilter() {			
			@Override
			public boolean accept(File file) {				
				return file.isDirectory();
			}
		});		

		
		for(File dir: subdirs){
						
			// need to check this periodically and terminate when requested
			if(isCancelled()){
				Settings.getLogger().debug("I am cancelled");
				return null;
			}
			
			String dirname = dir.getName().toLowerCase(); 
			
			if(dirname.equals(dstFolderName)){
				Settings.getLogger().debug("Target dir found as subdir, skipped: " + dir.getName());
				continue;
			}
			
			
			String[] dirnametokens = dirname.split("[-xX]");	// regexp: any of these chars is a separator
			if(dirnametokens.length != 3 
					|| !dirnametokens[0].equals("wp") 
					|| !dirnametokens[1].matches("^\\d+$")	// regexp: one or more digits
					|| !dirnametokens[2].matches("^\\d+$")){
				Settings.getLogger().info("Dir does not match pattern wp-0123x456, skipped: " + dir.getName());
				continue;
			}
			
			Settings.getLogger().info("Found " + dir);
sbHtml.append("found dir " + dir + "\n");			
			
			// find and copy all wallpaper files

			File[] files = dir.listFiles(new FileFilter() {				
				@Override
				public boolean accept(File file) {					
					return file.isFile();
				}
			});
			
			for(File file: files){
				
				// need to check this periodically and terminate when requested
				if(isCancelled()){
					Settings.getLogger().debug("I am cancelled");
					return null;
				}
				
				String[] filenametokens = file.getName().split("\\.");	
				if(filenametokens.length != 2){ // assume exactly one dot allowed in the name!
					Settings.getLogger().info("File must have only one dot in the name, skipped: " + file.getAbsolutePath());
					continue;
				}
				String targetname = filenametokens[0] + "-" + dirnametokens[1] + "x" + dirnametokens[2] + "." + filenametokens[1]; 						
				File targetfile = new File(wpDstFolder.getAbsolutePath() + File.separator +  targetname);
				copyFile(file, targetfile);
				
				Settings.getLogger().info("Copied " + targetfile.getAbsolutePath());
sbHtml.append("copied " + targetfile + "\n");				
			}
			
			//Thread.sleep(100);
		}
		
		// post image files and html
		File[] files = postImagesDir.listFiles(new FileFilter() {				
			@Override
			public boolean accept(File file) {					
				return file.isFile();
			}
		});
		
		for(File file: files){
			String filename = file.getName();
			String[] filenametokens = filename.split("\\.");
			if(filenametokens.length != 2){
				Settings.getLogger().info("File must have only one dot in the name, skipped: " + file.getAbsolutePath());
				continue;
			}
			
			Settings.getLogger().info("Found " + file.getAbsolutePath());
sbHtml.append("found " + file + "\n");				
			
		}
						
		return null;
	}

	@Override
	protected void done() {
		// this method runs in GUI thread, can update ui controls
		super.done();
		
		mainWindow.getTextPaneHtml().setText(sbHtml.toString());
	}
	
	private void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
}
