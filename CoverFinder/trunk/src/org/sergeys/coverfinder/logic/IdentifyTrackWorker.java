package org.sergeys.coverfinder.logic;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.sergeys.coverfinder.logic.AcoustIdUtil.Fingerprint;
import org.sergeys.coverfinder.logic.IProgressWatcher.Stage;

public class IdentifyTrackWorker
extends SwingWorker<Collection<IdentifyTrackResult>, Long>
{
	IProgressWatcher<IdentifyTrackResult> watcher;
	Track track;
	
	public IdentifyTrackWorker(Track track, IProgressWatcher<IdentifyTrackResult> watcher){
		this.watcher = watcher;
		this.track = track;
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
			
			watcher.reportException(e.getCause());
			watcher.progressComplete(null, Stage.Finish);
		}
	}

	@Override
	protected Collection<IdentifyTrackResult> doInBackground() throws Exception {		 		
		Fingerprint fp = AcoustIdUtil.getInstance().getFingerprint(track.getFile());

		if(!fp.fingerprint.isEmpty()){				
			Collection<IdentifyTrackResult> list = AcoustIdUtil.getInstance().identify(fp);
			return list;
		}
		else{
			return null;
		}				
	}

}
