package org.sergeys.coverfinder.logic;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
	
	Integer count = 0;
	
	@Override
	protected Collection<ImageSearchResult> doInBackground() throws Exception {
		Collection<ImageSearchResult> results;
		
		results = searchMore ? engine.searchMore() : engine.search(req); 
		
		// download actual images in separate threads
		Executor executor = Executors.newCachedThreadPool();
		
		for(final ImageSearchResult isr: results){
			synchronized (count) {
				count++;
			}
			
			executor.execute(new Runnable(){
				@Override
				public void run() {
					System.out.println("downloading " + isr.getImageUrl());
					isr.retrieveImages();
					synchronized (count) {
						count--;
					}
					
				}});
		}
		
		while(count > 0){
			Thread.sleep(100);
			//System.out.println("still running " + count);
		}
										
		return results;
	}
	
}
