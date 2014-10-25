package org.sergeys.library.swing;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileTreeNode extends DefaultMutableTreeNode {

    private File file;
    private boolean expanded;
    private String nodeName;

    public FileTreeNode(File file){
        this.file = file;
        this.setAllowsChildren(file.isDirectory());
    }

    public FileTreeNode(File file, String nodeName) {
        this(file);
        this.nodeName = nodeName;
    }

    public File getFile(){
        return file;
    }

    @Override
    public String toString() {

        if(nodeName != null){
            return nodeName;
        }
        else
        {
            return (file == null) ? "null" : (file.getName().isEmpty() ? file.getPath() : file.getName());
        }
    }

    public void addSubdirs(int depth){
        if(!expanded){

            File[] subdirs = this.file.listFiles(new FileFilter(){
                @Override
                public boolean accept(File file) {
                    return file.isDirectory()
                            && !file.isHidden()
                            && !file.getName().startsWith(".")
                            && !file.getName().equalsIgnoreCase("$recycle.bin");
                }});

            if(subdirs != null){
                Arrays.sort(subdirs);
                for(File subdir: subdirs){
                    FileTreeNode child = new FileTreeNode(subdir);
                    this.add(child);
                }
            }

            if(depth > 0 && children != null){
                for(Object child: children){
                    if(child instanceof FileTreeNode){
                        ((FileTreeNode)child).addSubdirs(depth - 1);
                    }
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
