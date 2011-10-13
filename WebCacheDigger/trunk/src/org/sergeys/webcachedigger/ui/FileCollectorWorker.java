package org.sergeys.webcachedigger.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.IProgressWatcher;
import org.sergeys.webcachedigger.logic.Mp3Utils;
import org.sergeys.webcachedigger.logic.Settings;

public class FileCollectorWorker 
extends SwingWorker<ArrayList<CachedFile>, Long> 
implements IProgressWatcher
//implements IBrowserProgressListener
{
	private List<IBrowser> browsers;
	private long totalCount = 0;
	private FileSearchProgressDialog pd;
	private int stage;
	private Settings settings;

	public FileCollectorWorker(List<IBrowser> browsers, FileSearchProgressDialog pd, Settings settings) {
		this.browsers = browsers;
		this.pd = pd;
		this.settings = settings;
	}
		
	@Override
	protected ArrayList<CachedFile> doInBackground() throws Exception {
		ArrayList<CachedFile> cachedFiles = new ArrayList<CachedFile>();
		ArrayList<CachedFile> filteredFiles = new ArrayList<CachedFile>();

		// collect raw files
		stage = 0;
		
		for (IBrowser browser : browsers) {
			cachedFiles.addAll(browser.collectCachedFiles(this));
		}		
		
		// determine file types, filter out by type
		stage = 1;
		totalCount = 0;
		for(CachedFile file: cachedFiles){
			// mime type examination
			String type = file.getFileType();
			
			if(type.startsWith("audio/")){
				if(settings.getActiveFileTypes().contains(Settings.FileType.Audio)){
					filteredFiles.add(file);
					
					if(settings.isRenameMp3byTags() && type.equals("audio/mpeg")){
						file.setProposedName(Mp3Utils.proposeName(file));
					}
				}
			}
			else if(type.startsWith("video/")){
				if(settings.getActiveFileTypes().contains(Settings.FileType.Video)){
					filteredFiles.add(file);
				}
			}
			else if(type.startsWith("image/")){
				if(settings.getActiveFileTypes().contains(Settings.FileType.Image)){
					filteredFiles.add(file);
				}
			}
			else{
				if(settings.getActiveFileTypes().contains(Settings.FileType.Other)){
					filteredFiles.add(file);
				}
			}
						
			totalCount++;
			publish(totalCount);
		}		
		
		return filteredFiles;
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
	}

	@Override
	public synchronized void progressStep() {
		totalCount++;
		publish(totalCount);		
	}

}
