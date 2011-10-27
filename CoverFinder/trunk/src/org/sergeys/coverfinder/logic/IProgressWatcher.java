package org.sergeys.coverfinder.logic;

import java.util.Collection;

public interface IProgressWatcher<T> {
	public enum Stage { Collecting, Analyzing, Finish };
	
	public void updateStage(Stage stage);
	public void updateProgress(long count, Stage stage);
	public void progressComplete(Collection<T> items, Stage stage);
	public boolean isAllowedToContinue(Stage stage);
}
