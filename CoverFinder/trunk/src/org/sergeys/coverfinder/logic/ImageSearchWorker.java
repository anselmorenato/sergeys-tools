package org.sergeys.coverfinder.logic;

import java.util.Collection;

public class ImageSearchWorker
extends AbstractWorker<ImageSearchResult>
{
	IImageSearchEngine engine;
	ImageSearchRequest req;
	boolean searchMore;
		
	public ImageSearchWorker(IImageSearchEngine engine, ImageSearchRequest req, 
			boolean searchMore, IProgressWatcher<ImageSearchResult> watcher){
		super(watcher);
		
		this.engine = engine;
		this.req = req;
		this.searchMore = searchMore;
	}
	
	@Override
	protected Collection<ImageSearchResult> doInBackground() throws Exception {
		Collection<ImageSearchResult> results;
		
		results = searchMore ? engine.searchMore() : engine.search(req); 
		
		return results;
	}
	
}
