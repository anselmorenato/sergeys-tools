package org.sergeys.coverfinder.logic;

import java.util.Collection;

import org.sergeys.coverfinder.logic.AcoustIdUtil.Fingerprint;

public class IdentifyTrackWorker
extends AbstractWorker<IdentifyTrackResult>
{	
	Track track;
	
	public IdentifyTrackWorker(Track track, IProgressWatcher<IdentifyTrackResult> watcher){
		super(watcher);
				
		this.track = track;
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
