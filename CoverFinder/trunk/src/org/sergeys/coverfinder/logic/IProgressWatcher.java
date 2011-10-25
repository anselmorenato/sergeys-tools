package org.sergeys.coverfinder.logic;

import java.util.Collection;

public interface IProgressWatcher<T> {
	// stages
	public static final int COLLECTING = 1;
	
	public void updateStage(int stage);
	public void updateProgress(long count, int stage);
	public void progressComplete(Collection<T> items, int stage);
	public boolean isAllowedToContinue(int stage);
}
