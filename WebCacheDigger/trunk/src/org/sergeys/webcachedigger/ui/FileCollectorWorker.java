package org.sergeys.webcachedigger.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.Database;
import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.IProgressWatcher;
import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;
import org.sergeys.webcachedigger.logic.SimpleLogger;

public class FileCollectorWorker 
extends SwingWorker<ArrayList<CachedFile>, Long> 
implements IProgressWatcher
{
	private List<IBrowser> browsers;
	private long totalCount = 0;
	private ProgressDialog pd;
	private int stage;
	
	public FileCollectorWorker(List<IBrowser> browsers, ProgressDialog pd) {
		this.browsers = browsers;
		this.pd = pd;	
	}	
	
	@Override
	protected ArrayList<CachedFile> doInBackground() throws Exception {
		ArrayList<CachedFile> cacheFiles = new ArrayList<CachedFile>();
		

		// collect raw files
		stage = ProgressDialog.STAGE_COLLECT;
		totalCount = 0;
		publish(totalCount);
		
		for (IBrowser browser : browsers) {
			cacheFiles.addAll(browser.collectCachedFiles(this));
			if(isCancelled()){
				return null;
			}				
		}		
		
		SimpleLogger.logMessage("collected files: " + cacheFiles.size()); //$NON-NLS-1$
				
		if(Settings.getInstance().isExcludeSavedAndIgnored()){
			// exclude saved files with same absolute path and timestamp
			cacheFiles = Database.getInstance().filterSavedAndIgnoredByFilesystem(cacheFiles);
			SimpleLogger.logMessage("not yet copied and not ignored files: " + cacheFiles.size());
		}
				
		stage = ProgressDialog.STAGE_FILTER_TYPE;
		totalCount = 0;
		publish(totalCount);
		
		ArrayList<CachedFile> knownMimeFiles = new ArrayList<CachedFile>();
		Database.getInstance().preloadMetadata(cacheFiles, knownMimeFiles);	// mime set, hash possibly set
		
		// filter by mime
		CachedFile.detectMimeTypes(cacheFiles, this);
		if(isCancelled()){
			return null;
		}
		Database.getInstance().insertUpdateMimeTypes(cacheFiles);
						
		cacheFiles = CachedFile.filter(cacheFiles, null);
		knownMimeFiles = CachedFile.filter(knownMimeFiles, null);		
		
		if(Settings.getInstance().isExcludeSavedAndIgnored()){
			stage = ProgressDialog.STAGE_FILTER_HASH;
			totalCount = 0;
			publish(totalCount);
			
			ArrayList<CachedFile> hasHash = new ArrayList<CachedFile>();
			
			for(CachedFile f: knownMimeFiles){
				if(f.getHash() != null){
					hasHash.add(f);
				}
				else{
					cacheFiles.add(f);
				}
			}
			CachedFile.detectHashes(cacheFiles, this);
			if(isCancelled()){
				return null;
			}
			Database.getInstance().insertUpdateHashes(cacheFiles);
			
			cacheFiles.addAll(hasHash);			
			cacheFiles = Database.getInstance().filterSavedAndIgnoredByHash(cacheFiles); 
		}
		else{
			cacheFiles.addAll(knownMimeFiles);			
		}
		
		return cacheFiles;
	}
	
	@Override
	protected void process(List<Long> chunks) {
		super.process(chunks);
		pd.updateProgress(chunks.isEmpty() ? 0 : chunks.get(chunks.size() - 1), stage);		
	}
	
	@Override	
	protected void done() {
		super.done();
		
		try {
			pd.searchComplete(get());
		} catch (InterruptedException e) {

			SimpleLogger.logMessage("interrupted"); //$NON-NLS-1$
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					JOptionPane.showMessageDialog(null, String.format(Messages.getString("FileCollectorWorker.ExecutionException"), e.getMessage())); //$NON-NLS-1$
					
				}});
		}
		catch (CancellationException e) {
			SimpleLogger.logMessage("Worker cancelled"); //$NON-NLS-1$
		}
	}

	@Override
	public synchronized void progressStep() {
		totalCount++;
		publish(totalCount);		
	}

	@Override
	public boolean isAllowedToContinue() {		
		return !isCancelled();
	}

}
