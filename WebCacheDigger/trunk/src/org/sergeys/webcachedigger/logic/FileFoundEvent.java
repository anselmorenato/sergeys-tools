package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.util.EventObject;

public class FileFoundEvent extends EventObject {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	public FileFoundEvent(Object source) {
//		super(source);		
//	}
	
	public FileFoundEvent(File file) {
		super(file);	// CachedFile is the source
	}

}
