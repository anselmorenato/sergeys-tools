package org.sergeys.coverfinder.logic;

import javax.swing.tree.DefaultMutableTreeNode;

public class Artist 
extends DefaultMutableTreeNode
{

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
