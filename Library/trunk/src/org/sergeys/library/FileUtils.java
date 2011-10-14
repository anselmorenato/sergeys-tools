package org.sergeys.library;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

	public static String md5hash(File file) throws NoSuchAlgorithmException, IOException{
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		return md5hash(is);
	}
	
	public static String md5hash(InputStream is) throws NoSuchAlgorithmException, IOException {
		
		MessageDigest md5 = MessageDigest.getInstance("MD5");
				
		is = new DigestInputStream(is, md5);
		while(is.read() != -1){}
		
		byte[] bytes = md5.digest();
		// http://stackoverflow.com/questions/304268/using-java-to-get-a-files-md5-checksum
		//System.out.println(String.format("%032x", new BigInteger(1, bytes)));
		
		String hash = String.format("%032x", new BigInteger(1, bytes));
		return hash;		
	}

}
