package org.sergeys.coverfinder.logic;

import java.io.File;

import org.sergeys.coverfinder.logic.Settings.DetectFilesMethod;
import org.sergeys.coverfinder.ui.Messages;

public class Track
extends MusicItem
{
	public static final String UNKNOWN_ARTIST = Messages.getString("Track.UnknownArtist"); //$NON-NLS-1$
	public static final String UNKNOWN_ALBUM = Messages.getString("Track.UnknownAlbum"); //$NON-NLS-1$
	public static final String UNKNOWN_TRACK = Messages.getString("Track.UnknownTrack"); //$NON-NLS-1$
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private File file;
	
	private DetectFilesMethod detectFilesMethod;
	private String hash;
	private String mimeType = null;
	
	private String albumTitle;
	
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

	public String getFilename(){
		return file.getName();
	}
}
