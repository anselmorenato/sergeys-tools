package org.sergeys.webcachedigger.ui;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
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
import org.sergeys.webcachedigger.logic.Mp3Utils;
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
	private Settings settings;

	public FileCollectorWorker(List<IBrowser> browsers, ProgressDialog pd, Settings settings) {
		this.browsers = browsers;
		this.pd = pd;
		this.settings = settings;
	}
		
	private boolean checkAgainstDatabase(CachedFile file) throws SQLException, NoSuchAlgorithmException, IOException{
		return settings.isExcludeAlreadySaved() ? (! Database.getInstance().isSaved(file)) : true;
	}
	
	@Override
	protected ArrayList<CachedFile> doInBackground() throws Exception {
		ArrayList<CachedFile> cachedFiles = new ArrayList<CachedFile>();
		ArrayList<CachedFile> filteredFiles = new ArrayList<CachedFile>();

		// collect raw files
		stage = ProgressDialog.STAGE_COLLECT;
		
		for (IBrowser browser : browsers) {
			cachedFiles.addAll(browser.collectCachedFiles(this));
		}		
		
		SimpleLogger.logMessage("collected files: " + cachedFiles.size()); //$NON-NLS-1$
		
		// determine file types, filter out by type
		stage = ProgressDialog.STAGE_ANALYZE;
		totalCount = 0;
		for(CachedFile file: cachedFiles){
			
			// watch for cancellation inside loop
			if(isCancelled()){
				return null;
			}
			
			// mime type examination
			String type = file.getFileType();
			
			if(type.startsWith("audio/")){ //$NON-NLS-1$
				if(settings.getActiveFileTypes().contains(Settings.FileType.Audio)){
					if(checkAgainstDatabase(file)){
						filteredFiles.add(file);
						
						if(settings.isRenameMp3byTags() && type.equals("audio/mpeg")){ //$NON-NLS-1$
							file.setProposedName(Mp3Utils.proposeName(file));
						}
					}
				}
			}
			else if(type.startsWith("video/")){ //$NON-NLS-1$
				if(settings.getActiveFileTypes().contains(Settings.FileType.Video)){
					if(checkAgainstDatabase(file)){
						filteredFiles.add(file);
					}
				}
			}
			else if(type.startsWith("image/")){ //$NON-NLS-1$
				if(settings.getActiveFileTypes().contains(Settings.FileType.Image)){
					if(checkAgainstDatabase(file)){
						filteredFiles.add(file);
					}
				}
			}
			else{
				if(settings.getActiveFileTypes().contains(Settings.FileType.Other)){
					if(checkAgainstDatabase(file)){
						filteredFiles.add(file);
					}
				}
			}
						
			totalCount++;
			publish(totalCount);
		}		
		
		SimpleLogger.logMessage("processed files: " + totalCount); //$NON-NLS-1$
		
		return filteredFiles;
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

}
