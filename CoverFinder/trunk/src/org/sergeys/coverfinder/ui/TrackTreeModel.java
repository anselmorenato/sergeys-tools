package org.sergeys.coverfinder.ui;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.sergeys.coverfinder.logic.Album;
import org.sergeys.coverfinder.logic.Artist;
import org.sergeys.coverfinder.logic.Track;
import org.sergeys.library.swing.treetable.DynamicTreeTableModel;
import org.sergeys.library.swing.treetable.TreeTableModel;

public class TrackTreeModel 
extends DynamicTreeTableModel
{
    /**
     * Names of the columns.
     */
    private static final String[] columnNames =
                //{ "Name", "Location", "Last Visited", "Created" };
    	{ Messages.getString("TrackTreeModel.Name"), Messages.getString("TrackTreeModel.FileName"), Messages.getString("TrackTreeModel.HasPicture") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    /**
     * Method names used to access the data to display.
     */
    private static final String[] methodNames =
                //{ "getName", "getLocation", "getLastVisited","getCreated" };
    	{ "getName", "getFilename", "isHasPicture" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    /**
     * Method names used to set the data.
     */
    private static final String[] setterMethodNames =
                //{ "setName", "setLocation", "setLastVisited","setCreated" };
    	null;
    	//{"setName", "setHasPicture"};	// TODO: not expands when null
    	//{"", ""};	
    
    private static final int[] widths = { 4000, 4000, 1000 };
    
    /**
     * Classes presenting the data.
     */    
	private static final Class<?>[] classes =
                //{ TreeTableModel.class, String.class, Date.class, Date.class };
		{ TreeTableModel.class, String.class, Boolean.class };

	public TrackTreeModel(DefaultMutableTreeNode root, Collection<Track> tracks) {
		super(root, columnNames, methodNames, setterMethodNames, classes, widths);								
		
		if(tracks != null){		
			Map<String, Artist> artists = new Hashtable<String, Artist>();
			Map<String, Album> albums = new Hashtable<String, Album>();
			
			for(Track tr: tracks){
															
				if(!artists.containsKey(tr.getArtist())){
					Artist art = new Artist();
					art.setName(tr.getArtist());
					artists.put(tr.getArtist(), art);
					
					root.add(art);
				}
									
				String albumKey = tr.getArtist() + tr.getFilesystemDir() + tr.getAlbumTitle(); 
				if(!albums.containsKey(albumKey)){
					Album alb = new Album();

					alb.setTitle(tr.getAlbumTitle());
					alb.setArtist(tr.getArtist());
					alb.setFilesystemDir(tr.getFilesystemDir());
					albums.put(albumKey, alb);
					
					artists.get(tr.getArtist()).add(alb);
				}
				
				albums.get(albumKey).add(tr);									
			}
		}
	}

}
