package org.sergeys.library.swing;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileFilter;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class DirTreePanel extends JPanel implements TreeExpansionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DirTreePanel(){
        this(new File[]{}, null, "Computer", "My Home Folder");
    }

    JTree tree;

    /**
     * Create the panel.
     */
    public DirTreePanel(File[] roots, FileFilter filter, String rootNodeName, String homeFolderName) {
        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        DefaultMutableTreeNode root;
        if(roots.length > 1){
            root = new DefaultMutableTreeNode(rootNodeName);

            FileTreeNode ftn = new FileTreeNode(new File(System.getProperty("user.home")), homeFolderName);
            ftn.addSubdirs(0);
            root.add(ftn);

            for(File file: roots){
                ftn = new FileTreeNode(file);
                ftn.addSubdirs(0);
                root.add(ftn);
            }
        }
        else{
            //root = new FileTreeNode(roots[0]);
            //((FileTreeNode)root).addSubdirs(1);

            root = new DefaultMutableTreeNode(rootNodeName);

            FileTreeNode ftn = new FileTreeNode(new File(System.getProperty("user.home")), homeFolderName);
            ftn.addSubdirs(0);
            root.add(ftn);

            ftn = new FileTreeNode(roots[0]);
            ftn.addSubdirs(0);
            root.add(ftn);
        }

        tree = new JTree(root);
        scrollPane.setViewportView(tree);

        tree.addTreeExpansionListener(this);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // make leafs look like folders
        DefaultTreeCellRenderer dtcr = new DefaultTreeCellRenderer();
        dtcr.setLeafIcon(dtcr.getDefaultClosedIcon());
        tree.setCellRenderer(dtcr);

    }

    @Override
    public void treeExpanded(TreeExpansionEvent e) {

        TreePath tp = e.getPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tp.getLastPathComponent();

        for(int i = 0; i < node.getChildCount(); i++){
            TreeNode child = node.getChildAt(i);
            if(child instanceof FileTreeNode){
                ((FileTreeNode)child).addSubdirs(0);
            }
        }
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent arg0) {
    }

    public File getSelectedDirectory(){
        Object o = tree.getLastSelectedPathComponent();
        if(o != null && o instanceof FileTreeNode){
            return ((FileTreeNode)o).getFile();
        }
        else{
            return null;
        }

    }
}
