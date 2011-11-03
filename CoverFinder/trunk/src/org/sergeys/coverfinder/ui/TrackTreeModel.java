package org.sergeys.coverfinder.ui;


import java.util.Collection;
import java.util.Hashtable;

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
		//DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		super(root, columnNames, methodNames, setterMethodNames, classes, widths);
		
		Hashtable<String, Artist> artists = new Hashtable<String, Artist>();
		Hashtable<String, Album> albums = new Hashtable<String, Album>();
		
		for(Track tr: tracks){
			if(!artists.containsKey(tr.getArtist())){
				Artist art = new Artist();
				art.setName(tr.getArtist());
				artists.put(tr.getArtist(), art);
				
				root.add(art);
			}
			
			if(!albums.containsKey(tr.getAlbumDir()+tr.getAlbum())){
				Album alb = new Album();
				alb.setName(tr.getAlbum());
				
				albums.put(tr.getAlbumDir()+tr.getAlbum(), alb);
				
				artists.get(tr.getArtist()).add(alb);
			}
			
			albums.get(tr.getAlbumDir()+tr.getAlbum()).add(tr);
		}
	}

}
