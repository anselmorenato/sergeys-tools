package org.sergeys.coverfinder.logic;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.sergeys.coverfinder.logic.IProgressWatcher.Stage;

/**
 * Worker which notifies its caller through IProgressWatcher interface.
 * Returns collection of T items, reports intermediate results as Long.
 * 
 * @author sergeys
 *
 * @param <T>
 */
public abstract class AbstractWorker<T>
extends SwingWorker<Collection<T>, Long>
{
	protected IProgressWatcher<T> watcher;
	
	public AbstractWorker(IProgressWatcher<T> watcher){
		this.watcher = watcher;
	}
	
	@Override
	protected void done() {
		
		super.done();
		
		try {
			watcher.progressComplete(get(), Stage.Finish);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {			
			e.printStackTrace();
			
			watcher.reportException(e.getCause());
			watcher.progressComplete(null, Stage.Finish);
		}
	}

//	@Override
//	protected abstract Collection<T> doInBackground() throws Exception;

}
