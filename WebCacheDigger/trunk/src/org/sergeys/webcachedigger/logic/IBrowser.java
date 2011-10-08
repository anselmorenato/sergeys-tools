package org.sergeys.webcachedigger.logic;

import java.util.List;

public interface IBrowser extends Runnable {
	public String getName();
	public void setSettings(Settings settings);
	public List<CachedFile> collectCachedFiles() throws Exception;
//	public void addBrowserProgressListener(IBrowserProgressListener listener);
}
