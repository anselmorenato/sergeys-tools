package org.sergeys.webcachedigger.logic;

import java.util.ArrayList;
import java.util.List;

public class FileCollector {

	private List<IBrowser> browsers;

	public FileCollector(List<IBrowser> browsers) {
		this.browsers = browsers;
	}

	public ArrayList<CachedFile> collect(Settings settings) throws Exception {

		ArrayList<CachedFile> files = new ArrayList<CachedFile>();

		for (IBrowser browser : browsers) {
			files.addAll(browser.collectCachedFiles(settings));
		}

		return files;
	}
}
