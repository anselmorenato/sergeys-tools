package org.sergeys.webcachedigger.logic;

import java.util.List;

import javax.swing.ImageIcon;

public interface IBrowser 
{
	public String getName();
	public String getScreenName();
	public ImageIcon getIcon();
	public boolean isPresent();
//	public void setSettings(Settings settings);

	//public List<CachedFile> collectCachedFiles(IProgressWatcher watcher, @SuppressWarnings("rawtypes") SwingWorker swingWorker) throws Exception;
	public List<CachedFile> collectCachedFiles(IProgressWatcher watcher) throws Exception;
}
