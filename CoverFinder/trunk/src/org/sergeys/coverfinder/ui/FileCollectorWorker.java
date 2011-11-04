package org.sergeys.coverfinder.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.sergeys.coverfinder.logic.Database;
import org.sergeys.coverfinder.logic.IProgressWatcher;
import org.sergeys.coverfinder.logic.IProgressWatcher.Stage;
import org.sergeys.coverfinder.logic.Track;
import org.sergeys.coverfinder.logic.Settings;
import org.sergeys.library.FileUtils;
import org.sergeys.library.NotImplementedException;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;


public class FileCollectorWorker
extends SwingWorker<Collection<Track>, Long>
{
	// stage
	//public static final int COLLECTION = 1;
	private Stage stage;
	
	IProgressWatcher<Track> watcher;
	Collection<File> rootPaths;
	
	public FileCollectorWorker(Collection<File> rootPaths, IProgressWatcher<Track> watcher){
		this.watcher = watcher;
		this.rootPaths = rootPaths;
	}

	@Override
	protected Collection<Track> doInBackground() throws Exception {				
		
		Collection<Track> collected = Collections.synchronizedList(new ArrayList<Track>());
		
		List<File> files = new ArrayList<File>();
		FileFilter filter = null;
		
		switch(Settings.getInstance().getDetectFilesMethod()){
			case Extension:
				filter = new FileFilter() {		
					private long count = 0;
					@Override
					public boolean accept(File file) {
//try {
//	Thread.sleep(100);
//} catch (InterruptedException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}						
						boolean match = file.getName().toLowerCase().endsWith(".mp3");
						//if(match){
							publish(++count);
						//}
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
		
		stage = Stage.Collecting;
		
		for(File root: rootPaths){
			FileUtils.listFilesRecursive(root, filter, null, files);
		}
		
		synchronized (collected) {
			for(File file: files){
				Track mf = new Track(file);
				mf.setDetectFilesMethod(Settings.getInstance().getDetectFilesMethod());
				mf.setMimeType("audio/mpeg");
				collected.add(mf);
			}			
		}
		
		Collection<Track> changed = Database.getInstance().filterUnchanged(collected);
		System.out.println("Files total: " + collected.size());
		System.out.println("Files new or changed: " + changed.size());
		
		// process changed files
		long count = 0;
		stage = Stage.Analyzing;
		for(Track track: changed){
			
			Mp3File mp3 = new Mp3File(track.getFile().getAbsolutePath());
			if(mp3.hasId3v2Tag()){
				ID3v2 id3v2 = mp3.getId3v2Tag();
				track.setArtist(id3v2.getArtist());
				track.setAlbum(id3v2.getAlbum());
				track.setTitle(id3v2.getTitle());
				byte[] bytes = id3v2.getAlbumImage();				
				if(bytes != null){
					track.setHasPicture(true);
				}
			}
			
			publish(++count);
		}
		
		// update database
		Database.getInstance().insertOrUpdate(changed);
		
		return collected;
	}

	@Override
	protected void done() {
		
		super.done();
		
		try {
			watcher.progressComplete(get(), Stage.Finish);
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
		watcher.updateProgress(chunks.isEmpty() ? 0 : chunks.get(chunks.size() - 1), stage);
	}
	

}
