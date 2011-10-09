package org.sergeys.webcachedigger.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.IProgressWatcher;
import org.sergeys.webcachedigger.logic.SimpleLogger;

public class FileCollectorWorker 
extends SwingWorker<ArrayList<CachedFile>, Long> 
implements IProgressWatcher
//implements IBrowserProgressListener
{
	private List<IBrowser> browsers;
	private long totalCount = 0;
	private FileSearchProgressDialog pd;
	private int stage;

	public FileCollectorWorker(List<IBrowser> browsers, FileSearchProgressDialog pd) {
		this.browsers = browsers;
		this.pd = pd;
	}
		
	@Override
	protected ArrayList<CachedFile> doInBackground() throws Exception {
		ArrayList<CachedFile> cachedFiles = new ArrayList<CachedFile>();

		// collect raw files
		stage = 0;
		
		for (IBrowser browser : browsers) {
//			browser.addBrowserProgressListener(this);
			cachedFiles.addAll(browser.collectCachedFiles(this));
		}		
		
		// determine file types
		stage = 1;
		totalCount = 0;
		for(CachedFile file: cachedFiles){
			file.getFileType();	// TODO: refactor method
			totalCount++;
			publish(totalCount);
		}
		
		return cachedFiles;
	}
	
	@Override
	protected void process(List<Long> chunks) {
		// TODO Auto-generated method stub
		super.process(chunks);
		pd.updateProgress(chunks.isEmpty() ? 0 : chunks.get(chunks.size() - 1), stage);		
	}
	
	@Override	
	protected void done() {
		// TODO Auto-generated method stub
		super.done();
		
		try {
			pd.searchComplete(get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			digger.updateCachedFiles(get());			
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			SimpleLogger.logMessage("cannot update files, interrupted");
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			SimpleLogger.logMessage("cannot update files: " + e);
//		}		
	}

	@Override
	public synchronized void progressStep() {
		totalCount++;
		publish(totalCount);		
	}

}
