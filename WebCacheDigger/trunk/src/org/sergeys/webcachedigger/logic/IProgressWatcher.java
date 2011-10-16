package org.sergeys.webcachedigger.logic;

public interface IProgressWatcher {
	public void progressStep();
	public boolean isAllowedToContinue();
//	public void progressComplete();
}
