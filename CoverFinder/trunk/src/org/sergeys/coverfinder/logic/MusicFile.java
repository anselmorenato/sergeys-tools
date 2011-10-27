package org.sergeys.coverfinder.logic;

import java.io.File;

import org.sergeys.coverfinder.logic.Settings.DetectFilesMethod;

import com.mpatric.mp3agic.Mp3File;

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
	private String artist;
	private String album;
	private boolean hasPicture;
	
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
