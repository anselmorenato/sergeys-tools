package org.sergeys.coverfinder.logic;

import javax.swing.tree.DefaultMutableTreeNode;

public class Album 
extends DefaultMutableTreeNode
{
	public static enum HasCover { AllTracks, SomeTracks, NoTracks };
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String artist;

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		setUserObject(title);
	}


}
