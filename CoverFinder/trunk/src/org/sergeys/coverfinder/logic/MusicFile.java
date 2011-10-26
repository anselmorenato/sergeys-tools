package org.sergeys.coverfinder.logic;

import java.io.File;

import org.sergeys.coverfinder.logic.Settings.DetectFilesMethod;

public class MusicFile
extends File
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DetectFilesMethod detectFilesMethod;
	private String hash;
	private String mimeType = null;
	
	public MusicFile(File file) {
		super(file.getAbsolutePath());
	}

	public DetectFilesMethod getDetectFilesMethod() {
		return detectFilesMethod;
	}

	public void setDetectFilesMethod(DetectFilesMethod detectFilesMethod) {
		this.detectFilesMethod = detectFilesMethod;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
