package org.sergeys.coverfinder.logic;

import java.util.Collection;

public interface IImageSearchEngine {
	String getName();
	String getDisplayName();
	String getBranding();
	
	Collection<ImageSearchResult> search(ImageSearchRequest req);
	
	/**
	 * Get more results from previous request
	 * @return
	 */
	Collection<ImageSearchResult> searchMore();
}
