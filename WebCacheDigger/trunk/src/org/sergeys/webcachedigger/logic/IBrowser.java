package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.util.List;

public interface IBrowser {
	public List<CachedFile> collectCachedFiles() throws Exception;
}
