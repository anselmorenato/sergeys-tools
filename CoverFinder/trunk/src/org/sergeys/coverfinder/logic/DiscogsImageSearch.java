package org.sergeys.coverfinder.logic;

import java.util.Collection;

/**
 * http://www.discogs.com/help/api
 * 
 * json or xml response
 * 
 * @author sergeys
 *
 */
public class DiscogsImageSearch implements IImageSearchEngine {

	@Override
	public String getName() {
		return "Discogs";
	}

	@Override
	public String getDisplayName() {		
		return "Discogs";
	}

	@Override
	public Collection<ImageSearchResult> search(ImageSearchRequest req) {
		return null;
	}

	@Override
	public Collection<ImageSearchResult> searchMore() {
		return null;
	}

	@Override
	public String getBranding() {
		return "Powered by Discogs";
	}

}
