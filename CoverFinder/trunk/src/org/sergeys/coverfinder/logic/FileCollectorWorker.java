package org.sergeys.coverfinder.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.sergeys.coverfinder.logic.IProgressWatcher.Stage;
import org.sergeys.library.FileUtils;
import org.sergeys.library.NotImplementedException;

// how to run several in parallel: http://stackoverflow.com/questions/3652973/backgrounds-tasks-by-swingworkers-become-sequential

public class FileCollectorWorker
extends AbstractWorker<Track>
{
	private Stage stage;
	Collection<File> rootPaths;
	
	public FileCollectorWorker(Collection<File> rootPaths, IProgressWatcher<Track> watcher){
		super(watcher);
		this.rootPaths = rootPaths;
	}

	@Override
	protected Collection<Track> doInBackground() throws Exception {				
		
		Collection<Track> collected = Collections.synchronizedList(new ArrayList<Track>());
		
		List<File> files = new ArrayList<File>();
		FileFilter filter = null;
		
		switch(Settings.getInstance().getDetectFilesMethod()){
			case Extension:
//				filter = new FileFilter() {		
//					private long count = 0;
//					@Override
//					public boolean accept(File file) {
////try {
////	Thread.sleep(100);
////} catch (InterruptedException e) {
////	// TODO Auto-generated catch block
////	e.printStackTrace();
////}						
//						boolean match = file.getName().toLowerCase().endsWith(".mp3");
//						//if(match){
//							publish(++count);
//						//}
//						return match;
//					}
//				};
				
				filter = new AudioFileFilter(false); 
				
				break;
			case MimeMagic:
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
			try{
				AudioFile af = AudioFileIO.read(track.getFile());
				Tag tag = af.getTag();
				if(tag != null){
					track.setArtist(tag.getFirst(FieldKey.ARTIST));
					track.setAlbumTitle(tag.getFirst(FieldKey.ALBUM));
					track.setTitle(tag.getFirst(FieldKey.TITLE));
					track.setHasPicture(tag.getFirstArtwork() != null);
				}							
			}
			catch(CannotReadException ex){
				ex.printStackTrace();
			}
			
			publish(++count);
		}
		
		// update database
		Database.getInstance().insertOrUpdate(changed);
		
		return collected;
	}

	@Override
	protected void process(List<Long> chunks) {		
		super.process(chunks);		
		watcher.updateProgress(chunks.isEmpty() ? 0 : chunks.get(chunks.size() - 1), stage);
	}	
}
