package org.sergeys.coverfinder.ui;


import java.util.Collection;
import java.util.Collections;
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
    	{ "Name", "Has picture" };
    /**
     * Method names used to access the data to display.
     */
    private static final String[] methodNames =
                //{ "getName", "getLocation", "getLastVisited","getCreated" };
    	{ "getName", "isHasPicture" };
    /**
     * Method names used to set the data.
     */
    private static final String[] setterMethodNames =
                //{ "setName", "setLocation", "setLastVisited","setCreated" };
    	//null;
    	{"setName", "setHasPicture"};	// TODO: not expands when null
    
    private static final int[] widths = { 4000, 1000 };
    
    /**
     * Classes presenting the data.
     */
    @SuppressWarnings("rawtypes")
	private static final Class[] classes =
                //{ TreeTableModel.class, String.class, Date.class, Date.class };
		{ TreeTableModel.class, Boolean.class };

	public TrackTreeModel(DefaultMutableTreeNode root, Collection<Track> tracks) {
		super(root, columnNames, methodNames, setterMethodNames, classes, widths);
		
		if(tracks != null){		
//			Map<String, Artist> artists = Collections.synchronizedMap(new Hashtable<String, Artist>());
//			Map<String, Album> albums = Collections.synchronizedMap(new Hashtable<String, Album>());
			Map<String, Artist> artists = new Hashtable<String, Artist>();
			Map<String, Album> albums = new Hashtable<String, Album>();
			
			for(Track tr: tracks){
															
				if(!artists.containsKey(tr.getArtist())){
					Artist art = new Artist();
					art.setName(tr.getArtist());
					artists.put(tr.getArtist(), art);
					
					root.add(art);
				}
									
				String albumKey = tr.getArtist() + tr.getAlbumDir() + tr.getAlbum(); 
				if(!albums.containsKey(albumKey)){
					Album alb = new Album();
					alb.setName(tr.getAlbum());
					
					albums.put(albumKey, alb);
					
					artists.get(tr.getArtist()).add(alb);
				}
				
				albums.get(albumKey).add(tr);									
			}
		}
	}

}
