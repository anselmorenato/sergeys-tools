package org.sergeys.coverfinder.logic;

import java.util.Collection;

public class FilesystemImageSearch
implements IImageSearchEngine
{

	@Override
	public String getName() {
		return "Filesystem";
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
	public String getDisplayName() {		
		return "Local files";
	}

	@Override
	public String getBranding() {
		return "Local files";
	}

}
