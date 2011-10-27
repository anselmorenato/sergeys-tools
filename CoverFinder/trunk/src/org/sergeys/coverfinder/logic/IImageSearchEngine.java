package org.sergeys.coverfinder.logic;

import java.util.Collection;

public interface IImageSearchEngine {
	Collection<ImageSearchResult> search(ImageSearchRequest req);
	Collection<ImageSearchResult> searchMore();
}
