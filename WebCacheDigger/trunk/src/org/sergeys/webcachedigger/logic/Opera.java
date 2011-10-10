package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Opera extends AbstractBrowser {

	@Override
	public String getName() {		
		return "Opera";
	}

	@Override
	public String getScreenName() {
		return "Opera";
	}

	private ImageIcon icon;
	
	@Override
	public ImageIcon getIcon() {
		
		if(icon == null){
			icon = new ImageIcon(this.getClass().getResource("/images/opera.png")); 
		}
		return icon;
	}

	@Override
	public List<CachedFile> collectCachedFiles(IProgressWatcher watcher) throws Exception {
		ArrayList<CachedFile> files = new ArrayList<CachedFile>(); 
		return files;
	}

	@Override
	protected List<File> collectExistingCachePaths() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
