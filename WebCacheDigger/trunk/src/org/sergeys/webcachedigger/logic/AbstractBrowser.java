package org.sergeys.webcachedigger.logic;

import java.util.List;

public abstract class AbstractBrowser implements IBrowser {

	protected List<String> cachePaths;
	/*
	public AbstractBrowser() throws Exception{
		
	}
	*/
	protected abstract List<String> collectDefaultCachePaths() throws Exception;
	
	protected List<String> getCachePaths() throws Exception{
		if(cachePaths == null){
			cachePaths = collectDefaultCachePaths();
		}
		
		return cachePaths;
	}
				
	@Override
	public abstract List<CachedFile> collectCachedFiles() throws Exception;

}
