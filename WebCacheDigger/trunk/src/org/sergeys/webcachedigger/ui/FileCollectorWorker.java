package org.sergeys.webcachedigger.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.SimpleLogger;

public class FileCollectorWorker 
extends SwingWorker<ArrayList<CachedFile>, Integer> 
//implements IBrowserProgressListener
{
	private List<IBrowser> browsers;
//	private int progress = 0;
	private WebCacheDigger digger;

	public FileCollectorWorker(List<IBrowser> browsers, WebCacheDigger digger) {
		this.browsers = browsers;
		this.digger = digger;
	}
		
	@Override
	protected ArrayList<CachedFile> doInBackground() throws Exception {
		ArrayList<CachedFile> cachedFiles = new ArrayList<CachedFile>();

		for (IBrowser browser : browsers) {
//			browser.addBrowserProgressListener(this);
			cachedFiles.addAll(browser.collectCachedFiles());
		}		
		
		return cachedFiles;
	}
/*
	@Override
	public void fileFound(FileFoundEvent event) {
		// TODO Auto-generated method stub
		publish(progress++);
		//firePropertyChange("progress", 0, progress);
	}

	@Override
	public void fileSearchCount(int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileSearchComplete() {
		// TODO Auto-generated method stub
		
	}
*/		
	@Override
	protected void process(List<Integer> chunks) {
		// TODO Auto-generated method stub
		super.process(chunks);
		
		int val = chunks.isEmpty() ? 0 : chunks.get(chunks.size() - 1);
		firePropertyChange("progress", 0, val);
	}
	
	@Override	
	protected void done() {
		// TODO Auto-generated method stub
		super.done();
		
		try {
			digger.updateCachedFiles(get());			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			SimpleLogger.logMessage("cannot update files, interrupted");
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			SimpleLogger.logMessage("cannot update files: " + e);
		}		
	}
}
