package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileCollector {

	private List<IBrowser> browsers;

	public FileCollector(List<IBrowser> browsers) {
		this.browsers = browsers;
	}

	public ArrayList<File> collect() throws Exception {

		ArrayList<File> files = new ArrayList<File>();

		for (IBrowser browser : browsers) {
			files.addAll(browser.collect());
		}

		return files;
	}
}
