package org.sergeys.webcachedigger.logic;

import java.util.ArrayList;

public interface IBrowser {
	public ArrayList<String> getDefaultCachePaths() throws Exception;
}
