package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.util.List;

public interface IBrowser {
	//public List<String> getDefaultCachePaths() throws Exception;
	//public List<File> collectFilesFromCacheDirectory(String directory);
	public List<File> collect() throws Exception;
}
