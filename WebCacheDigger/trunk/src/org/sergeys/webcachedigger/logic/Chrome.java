package org.sergeys.webcachedigger.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Google Chrome Windows
 * 
 * @author sergeys
 *
 */
public class Chrome extends AbstractBrowser {


	@Override
	public String getName() {
		return "Google Chrome (not implemented)";
	}

	@Override
	protected List<String> collectCachePaths() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CachedFile> collectCachedFiles(IProgressWatcher watcher) throws Exception {
		ArrayList<CachedFile> files = new ArrayList<CachedFile>(); 
		return files;
	}
}
