package org.sergeys.coverfinder.logic;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.sergeys.coverfinder.logic.IProgressWatcher.Stage;

public class ImageSearchWorker
extends SwingWorker<Collection<ImageSearchResult>, Long>
{
	IImageSearchEngine engine;
	IProgressWatcher<ImageSearchResult> watcher;
	ImageSearchRequest req;
	boolean searchMore;
		
	public ImageSearchWorker(IImageSearchEngine engine, ImageSearchRequest req, 
			boolean searchMore, IProgressWatcher<ImageSearchResult> watcher){
		this.engine = engine;
		this.watcher = watcher;
		this.req = req;
		this.searchMore = searchMore;
	}
	
	@Override
	protected Collection<ImageSearchResult> doInBackground() throws Exception {
		Collection<ImageSearchResult> results;
		
		results = searchMore ? engine.searchMore() : engine.search(req); 
		
		return results;
	}

	@Override
	protected void done() {
		
		super.done();
		
		try {
			watcher.progressComplete(get(), Stage.Finish);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//watcher.reportException(e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			watcher.reportException(e);
		}
	}
	
}
