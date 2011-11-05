package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.sergeys.coverfinder.logic.Album.HasCover;
import org.sergeys.coverfinder.logic.Database;
import org.sergeys.coverfinder.logic.Track;
import org.sergeys.library.swing.treetable.JTreeTable;

public class TrackTreePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JScrollPane scrollPane;
	DefaultMutableTreeNode root;
	JTreeTable treeTable;
	
	/**
	 * Create the panel.
	 */
	public TrackTreePanel() {
		setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		root = new DefaultMutableTreeNode("Artists/Albums/Tracks");
		treeTable = new JTreeTable(new TrackTreeModel(root, null));
		
		scrollPane.setViewportView(treeTable);
	}

	public void update(){
				
		try {
			Collection<Track> tracks = Database.getInstance().selectTracks(HasCover.AllTracks, "ru");
			root = new DefaultMutableTreeNode("Artists/Albums/Tracks");
			TrackTreeModel model = new TrackTreeModel(root, tracks);
			treeTable = new JTreeTable(model);
			scrollPane.setViewportView(treeTable);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @return selected Track, Album, Artist or null
	 */
	public Object getSelectedItem(){
		Object o = treeTable.getTree().getLastSelectedPathComponent(); 
		return o;		
	}
}


