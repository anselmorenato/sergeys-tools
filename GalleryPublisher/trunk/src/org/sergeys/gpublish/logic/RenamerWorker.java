package org.sergeys.gpublish.logic;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.swing.SwingWorker;

import org.sergeys.gpublish.ui.MainWindow;

public class RenamerWorker extends SwingWorker<Void, Integer> {

	private MainWindow mainWindow;
	private StringBuilder sbHtml = new StringBuilder();
	
	public RenamerWorker(MainWindow mainWindow){
		this.mainWindow = mainWindow;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		// this method runs in background thread, cannot update ui controls here
				
		File wpDstFolder = new File(Settings.getInstance().getDstWallpapersFolder());
		if(!wpDstFolder.exists() || !wpDstFolder.isDirectory()){
			Settings.getLogger().error("Folder do not exist or file is not a folder: " + wpDstFolder);
			return null;
		}				
		
		File wpSrcFolder = new File(Settings.getInstance().getSrcWallpapersFolder());
		if(!wpSrcFolder.exists() || !wpSrcFolder.isDirectory()){
			Settings.getLogger().error("Folder do not exist or file is not a folder: " + wpSrcFolder);
		}
		
		String dstFolderName = wpDstFolder.getName().toLowerCase();			
			
		File[] subdirs = wpSrcFolder.listFiles(new FileFilter() {			
			@Override
			public boolean accept(File file) {				
				return file.isDirectory();
			}
		});		

		for(File dir: subdirs){
						
			if(isCancelled()){
				Settings.getLogger().debug("I am cancelled");
				return null;
			}
			
			String dirname = dir.getName().toLowerCase(); 
			
			if(dirname.equals(dstFolderName)){
				Settings.getLogger().debug("Skipping target dir: " + dir.getName());
				continue;
			}
			
			
			String[] dirnametokens = dirname.split("[-xX]");
			if(dirnametokens.length != 3 
					|| !dirnametokens[0].equals("wp") 
					|| !dirnametokens[1].matches("^\\d+$")
					|| !dirnametokens[2].matches("^\\d+$")){
				Settings.getLogger().info("Dir does not match pattern wp-0123x456, skipped: " + dir.getName());
				continue;
			}
			
			Settings.getLogger().info("Found " + dir);
sbHtml.append(dir + "\n");			
			
			File[] files = dir.listFiles(new FileFilter() {				
				@Override
				public boolean accept(File file) {					
					return file.isFile();
				}
			});
			
			for(File file: files){
				String[] filenametokens = file.getName().split("\\.");	// assume only one dot in the name!
				if(filenametokens.length != 2){
					Settings.getLogger().info("File has unexpected name, skipped: " + file.getAbsolutePath());
					continue;
				}
				String targetname = filenametokens[0] + "-" + dirnametokens[1] + "x" + dirnametokens[2] + "." + filenametokens[1]; 						
				File targetfile = new File(wpDstFolder.getAbsolutePath() + File.separator +  targetname);
				copyFile(file, targetfile);
				
				Settings.getLogger().info("Copied " + targetfile.getAbsolutePath());
sbHtml.append(targetfile + "\n");				
			}
			
			
			
			
			
			
			
			
			//Thread.sleep(100);
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
