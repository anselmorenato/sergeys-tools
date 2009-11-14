package org.sergeys.webcachedigger.logic;

import java.util.List;

public interface IBrowser {
	public List<CachedFile> collectCachedFiles(Settings settings) throws Exception;
}
