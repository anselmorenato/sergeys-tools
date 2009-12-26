package org.sergeys.webcachedigger.logic;

import java.util.List;

public interface IBrowser {
	public String getName();
	public List<CachedFile> collectCachedFiles(Settings settings) throws Exception;
}
