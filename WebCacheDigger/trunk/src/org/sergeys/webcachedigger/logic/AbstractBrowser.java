package org.sergeys.webcachedigger.logic;

import java.util.List;

public abstract class AbstractBrowser implements IBrowser {

	protected Settings settings;
	protected List<String> cachePaths;
		
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	protected abstract List<String> collectCachePaths() throws Exception;
	
	protected List<String> getCachePaths() throws Exception{
		if(cachePaths == null){
			cachePaths = collectCachePaths();
		}
		
		return cachePaths;
	}	

	@Override	
	public void run() {		
		try {
			collectCachedFiles();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
