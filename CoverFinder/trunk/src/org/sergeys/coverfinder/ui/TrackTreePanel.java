package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.sergeys.coverfinder.logic.Album;
import org.sergeys.coverfinder.logic.Album.HasCover;
import org.sergeys.coverfinder.logic.Database;
import org.sergeys.coverfinder.logic.Settings;
import org.sergeys.coverfinder.logic.Track;
import org.sergeys.library.swing.treetable.JTreeTable;

public class TrackTreePanel 
extends JPanel  
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String MENU_IDENTIFY_TRACK = "MENU_IDENTIFY_TRACK";
	public static final String MENU_SEARCH_COVER = "MENU_SEARCH_COVER";
	
	JScrollPane scrollPane;
	DefaultMutableTreeNode root;
	JTreeTable treeTable;
	HashSet<TreeSelectionListener> listeners = new HashSet<TreeSelectionListener>();
		
	private final JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem mntmIdentifyTrack;
	private JMenuItem mntmSearchCover;
	
	
	/**
	 * Create the panel.
	 */
	public TrackTreePanel(ActionListener menuActionListener) {
		setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		root = new DefaultMutableTreeNode("Artists/Albums/Tracks");
		treeTable = new JTreeTable(new TrackTreeModel(root, null));
		
		setupTreeTable(treeTable);
		
		// TODO: uncomment this line to get panel components in ui designer,
		// comment for production.		
//		add(popupMenu, BorderLayout.EAST);
		
		mntmIdentifyTrack = new JMenuItem("Identify track");
		mntmIdentifyTrack.addActionListener(menuActionListener);
		mntmIdentifyTrack.setName(MENU_IDENTIFY_TRACK);
		popupMenu.add(mntmIdentifyTrack);
				
		mntmSearchCover = new JMenuItem("Search and set album cover");
		mntmSearchCover.addActionListener(menuActionListener);
		mntmSearchCover.setName(MENU_SEARCH_COVER);
		popupMenu.add(mntmSearchCover);
	}

	private void setupTreeTable(JTreeTable tt){
		scrollPane.setViewportView(tt);
		
		for(TreeSelectionListener l: listeners){
			tt.getTree().getSelectionModel().addTreeSelectionListener(l);
		}
		
		tt.addMouseListener(new MouseAdapter(){
			// handle both pressed and released for popup
			
			@Override
			public void mousePressed(MouseEvent e) {
				doPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				doPopupMenu(e);
			}});		
		
		tt.getTree().addMouseListener(new MouseAdapter(){
			// handle both pressed and released for popup
			
			@Override
			public void mousePressed(MouseEvent e) {
				doPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				doPopupMenu(e);
			}});	
	}
	
	private void enableMemuItems(){
		Object selected = getSelectedItem();
		
		mntmIdentifyTrack.setEnabled(selected instanceof Track);
		mntmSearchCover.setEnabled(selected instanceof Track || selected instanceof Album);
	}
	
	protected void doPopupMenu(MouseEvent e) {
		if(e.isPopupTrigger()){
			//JTreeTable source = (JTreeTable)e.getSource();
			JTreeTable source = treeTable;
            int row = source.rowAtPoint( e.getPoint() );
            int column = source.columnAtPoint( e.getPoint() );

            if (!source.isRowSelected(row)){
                source.changeSelection(row, column, false, false);
            }

            enableMemuItems();
            //popupMenu.show(e.getComponent(), e.getX(), e.getY());
            popupMenu.show(treeTable, e.getX(), e.getY());
		}				
	}

	public void update(){
				
		try {
			Collection<Track> tracks = Database.getInstance().selectTracks(HasCover.AllTracks, "ru", Settings.getInstance().getLibraryPaths());
			root = new DefaultMutableTreeNode("Artists/Albums/Tracks");
			TrackTreeModel model = new TrackTreeModel(root, tracks);
			treeTable = new JTreeTable(model);
			
			setupTreeTable(treeTable);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @return selected Track, Album, Artist object or null
	 */
	public Object getSelectedItem(){
		Object o = treeTable.getTree().getLastSelectedPathComponent(); 
		return o;		
	}
	
	public void addTreeSelectionListener(TreeSelectionListener listener){
		listeners.add(listener);
		if(treeTable != null){
			for(TreeSelectionListener l: listeners){
				treeTable.getTree().getSelectionModel().addTreeSelectionListener(l);
			}
		}
	}
}


