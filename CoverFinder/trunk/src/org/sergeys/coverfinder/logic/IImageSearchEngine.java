package org.sergeys.coverfinder.logic;

import java.util.Collection;

public interface IImageSearchEngine {
	String getName();
	String getDisplayName();
	String getBranding();
	
	Collection<ImageSearchResult> search(ImageSearchRequest req) throws ImageSearchException;
	
	/**
	 * Get more results from previous request
	 * @return
	 * @throws ImageSearchException 
	 */
	Collection<ImageSearchResult> searchMore() throws ImageSearchException;
}
