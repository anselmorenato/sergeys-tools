package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.net.URI;

import javax.activation.FileDataSource;

public class CachedFile extends File {

	private static final long serialVersionUID = 1L;

	private String hash;
	private String fileType = null;
	
	public CachedFile(String pathname) {
		super(pathname);
	}
			
	public String getHash(){
		if(hash == null){
			hash = "";
		}
		
		return hash;
	}
	
	public String getFileType(){
		if(fileType == null){
			FileDataSource fds = new FileDataSource(this);
			fileType = fds.getContentType();
		}
		
		return fileType;
	}
	
	
}
