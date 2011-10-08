package org.sergeys.webcachedigger.logic;

import java.util.EventListener;

public interface IBrowserProgressListener extends EventListener {
	public void fileFound(FileFoundEvent event);
	public void fileSearchCount(int count);
	public void fileSearchComplete();
}
