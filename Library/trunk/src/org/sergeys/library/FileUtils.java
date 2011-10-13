package org.sergeys.library;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

public abstract class FileUtils {
	
//	protected FileUtils(){
//		
//	}
	
	public static List<File> listFilesRecursive(File directory, final FileFilter fileFilter, List<File> allFiles){
		
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

	// http://www.rgagnon.com/javadetails/java-0064.html
	// TODO: note limit of 64MB
	public static void copyFile(File in, File out) throws IOException {
		copyFile(in.getAbsolutePath(), out.getAbsolutePath());
	}

	public static void copyFile(String in, String out) throws IOException {
		FileChannel inChannel = new FileInputStream(in).getChannel();
		FileChannel outChannel = new FileOutputStream(out).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			throw e;
		} finally {
			if (inChannel != null){
				inChannel.close();
			}
			if (outChannel != null){
				outChannel.close();
			}
		}
	}

}
