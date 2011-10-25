package org.sergeys.library.swing;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class DirTreeModel
extends DefaultTreeModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DirTreeModel(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
		// TODO Auto-generated constructor stub
	}

}
