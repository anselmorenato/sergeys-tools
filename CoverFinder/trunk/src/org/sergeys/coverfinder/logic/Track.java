package org.sergeys.coverfinder.logic;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import org.sergeys.coverfinder.logic.Settings.DetectFilesMethod;

public class Track
extends DefaultMutableTreeNode
{
	public static final String UNKNOWN_ARTIST = "<unknown artist>";
	public static final String UNKNOWN_ALBUM = "<unknown album>";
	public static final String UNKNOWN_TRACK = "<unknown track>";
	
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
	private String albumTitle;
	private String filesystemDir;
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

	public String getAlbumTitle() {
		return albumTitle;
	}

	public void setAlbumTitle(String album) {
		this.albumTitle = album;
	}

	public boolean isHasPicture() {
		return hasPicture;
	}

	public void setHasPicture(boolean hasPicture) {
		this.hasPicture = hasPicture;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		setUserObject(title);
	}

	public String getFilesystemDir() {
		return filesystemDir;
	}

	public void setFilesystemDir(String filesystemDir) {
		this.filesystemDir = filesystemDir;
	}

}
