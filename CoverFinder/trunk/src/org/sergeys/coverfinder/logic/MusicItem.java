package org.sergeys.coverfinder.logic;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Parent for Album and Track
 * 
 * @author sergeys
 *
 */
public class MusicItem
extends DefaultMutableTreeNode
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private String artist;
	private String filesystemDir;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
		setUserObject(title);
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getFilesystemDir() {
		return filesystemDir;
	}

	public void setFilesystemDir(String filesystemDir) {
		this.filesystemDir = filesystemDir;
	}
}
