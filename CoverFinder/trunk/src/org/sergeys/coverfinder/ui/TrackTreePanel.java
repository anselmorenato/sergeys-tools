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

	/**
	 * Create the panel.
	 */
	public TrackTreePanel() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		Collection<Track> tracks;
		try {
			tracks = Database.getInstance().selectTracks(HasCover.AllTracks);
			TrackTreeModel model = new TrackTreeModel(root, tracks);
			
			JTreeTable treeTable = new JTreeTable(model);
			scrollPane.setViewportView(treeTable);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
