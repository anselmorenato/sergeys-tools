package org.sergeys.library.swing;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class DirTreePanel extends JPanel implements TreeExpansionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DirTreePanel(){
		this(new File[]{}, null);
	}
	
	/**
	 * Create the panel.
	 */
	public DirTreePanel(File[] roots, FileFilter filter) {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		
		JTree tree = new JTree(root);
		scrollPane.setViewportView(tree);
				
		tree.addTreeExpansionListener(this);
		
		for(File file: roots){
			//root.add(collectChildren(file, 1));						
		}
	}
	
	protected ArrayList<DefaultMutableTreeNode> collectChildren(File dir, int depth){
		
		ArrayList<DefaultMutableTreeNode> children = new ArrayList<DefaultMutableTreeNode>();
						
		if(depth == 0){
			return children;
		}

		File[] subdirs = dir.listFiles(new FileFilter(){

			@Override
			public boolean accept(File file) {
				
				return file.isDirectory();
			}});
		
		if(subdirs != null){
			for(File subdir: subdirs){
				//dirNode.add(collectChildren(subdir, depth - 1));
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(subdir, true);
				//child.
			}
		}
		
		
		DefaultMutableTreeNode dirNode = new DefaultMutableTreeNode(dir.getName().isEmpty() ? dir.getPath() : dir.getName(), true);
		dirNode.setUserObject(dir);
		
		
		
		return children;
	}

	@Override
	public void treeCollapsed(TreeExpansionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeExpanded(TreeExpansionEvent e) {
		// TODO Auto-generated method stub
		TreePath tp = e.getPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tp.getLastPathComponent();
		if(node.isLeaf()){
			
		}
		
		//Object src = e.getSource();
		
	}

}
