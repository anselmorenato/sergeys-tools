package org.sergeys.library.swing;

import java.io.File;
import java.io.FileFilter;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileTreeNode extends DefaultMutableTreeNode {

	private File file;
	private boolean expanded;
	
	public FileTreeNode(File file){
		this.file = file;		
		this.setAllowsChildren(file.isDirectory());		
	}
	
	public File getFile(){
		return file;
	}
	
	@Override
	public String toString() {		
		return (file == null) ? "null" : (file.getName().isEmpty() ? file.getPath() : file.getName());
	}

//	@Override
//	public Object getUserObject() {
//		return (file == null) ? "null" : file.getName();
//	}

//	@Override
//	public Object[] getUserObjectPath() {
//		// TODO Auto-generated method stub
//		return super.getUserObjectPath();
//	}

	public void addSubdirs(){
		if(!expanded){
		
			File[] subdirs = this.file.listFiles(new FileFilter(){	
				@Override
				public boolean accept(File file) {					
					return file.isDirectory();
				}});
			
			if(subdirs != null){
				for(File subdir: subdirs){
					FileTreeNode child = new FileTreeNode(subdir);
					this.add(child);
				}
			}
			
			expanded = true;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
