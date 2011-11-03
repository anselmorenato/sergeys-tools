package org.sergeys.coverfinder.logic;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import org.sergeys.coverfinder.logic.Settings.DetectFilesMethod;

public class Track
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
	private String title;
	private String artist;
	private String album;
	private String albumDir;
	private boolean hasPicture;
	
	public Track(File file) {
		
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

	public String getAlbumDir() {
		return albumDir;
	}

	public void setAlbumDir(String albumDir) {
		this.albumDir = albumDir;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		setUserObject(title);
	}

}
