package org.sergeys.coverfinder.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.sergeys.coverfinder.logic.IProgressWatcher;
import org.sergeys.coverfinder.logic.MusicFile;
import org.sergeys.coverfinder.logic.Settings;
import org.sergeys.library.FileUtils;
import org.sergeys.library.NotImplementedException;


public class FileCollectorWorker
extends SwingWorker<Collection<MusicFile>, Long>
{
	// stage
	//public static final int COLLECTION = 1;
	
	IProgressWatcher<MusicFile> watcher;
	Collection<File> rootPaths;
	
	public FileCollectorWorker(Collection<File> rootPaths, IProgressWatcher<MusicFile> watcher){
		this.watcher = watcher;
		this.rootPaths = rootPaths;
	}

	@Override
	protected Collection<MusicFile> doInBackground() throws Exception {				
		
		Collection<MusicFile> collected = Collections.synchronizedList(new ArrayList<MusicFile>());
		
		List<File> files = new ArrayList<File>();
		FileFilter filter = null;
		
		switch(Settings.getInstance().getDetectFilesMethod()){
			case Extension:
				filter = new FileFilter() {		
					private long count = 0;
					@Override
					public boolean accept(File file) {						
						boolean match = file.getName().toLowerCase().endsWith(".mp3");
						if(match){
							//watcher.updateProgress(count++, COLLECTION);
							publish(count++);
						}
						return match;
					}
				};
				break;
			case MimeMagic:
				throw new NotImplementedException();
				//break;
			case Mp3File:
				throw new NotImplementedException();
				//break;
			default:
				throw new NotImplementedException();
				//break;
		}
		
		for(File root: rootPaths){
			FileUtils.listFilesRecursive(root, filter, null, files);
		}
		
		synchronized (collected) {
			for(File file: files){
				MusicFile mf = new MusicFile(file);
				mf.setDetectFilesMethod(Settings.getInstance().getDetectFilesMethod());
				collected.add(mf);
			}			
		}
		
		return collected;
	}

	@Override
	protected void done() {
		
		super.done();
		
		try {
			watcher.progressComplete(get(), IProgressWatcher.COLLECTING);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void process(List<Long> chunks) {		
		super.process(chunks);		
		watcher.updateProgress(chunks.isEmpty() ? 0 : chunks.get(chunks.size() - 1), IProgressWatcher.COLLECTING);
	}
	

}
