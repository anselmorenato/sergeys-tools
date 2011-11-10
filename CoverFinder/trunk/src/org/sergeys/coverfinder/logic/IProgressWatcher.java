package org.sergeys.coverfinder.logic;

import java.util.Collection;

/**
 * Used for classes that launch SwingWorkers
 * 
 * @author sergeys
 *
 * @param <T> Type of items, collection of which will be result of a process.
 */
public interface IProgressWatcher<T> {
	public enum Stage { Collecting, Analyzing, Finish };
	
	public void updateStage(Stage stage);
	public void updateProgress(long count, Stage stage);
	public void progressComplete(Collection<T> items, Stage stage);
	public boolean isAllowedToContinue(Stage stage);
}
