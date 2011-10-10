package org.sergeys.webcachedigger.logic;

import java.util.List;

import javax.swing.ImageIcon;

public interface IBrowser 
//extends Runnable 
{
	public String getName();
	public String getScreenName();
	public ImageIcon getIcon();
	public boolean isPresent();
	public void setSettings(Settings settings);
	public List<CachedFile> collectCachedFiles(IProgressWatcher watcher) throws Exception;
}
