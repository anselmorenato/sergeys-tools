package org.sergeys.coverfinder.logic;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import org.sergeys.coverfinder.logic.Settings.DetectFilesMethod;

public class Track
//extends File
extends DefaultMutableTreeNode
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private File file;
	
	private DetectFilesMethod detectFilesMethod;
	private String hash;
	private String mimeType = null;
	private String artist;
	private String album;
	private boolean hasPicture;
	
	public Track(File file) {
		//super(file.getAbsolutePath());
		this.file = file;
	}

	public File getFile(){
		return file;
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

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public boolean isHasPicture() {
		return hasPicture;
	}

	public void setHasPicture(boolean hasPicture) {
		this.hasPicture = hasPicture;
	}
}
