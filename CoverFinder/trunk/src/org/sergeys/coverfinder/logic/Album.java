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
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setUserObject(name);
	}

}
