package org.sergeys.coverfinder.logic;

import java.util.Collection;

public interface IImageSearchEngine {
	Collection<ImageSearchResult> search(ImageSearchRequest req);
	
	/**
	 * Get more results from previous request
	 * @return
	 */
	Collection<ImageSearchResult> searchMore();
}
