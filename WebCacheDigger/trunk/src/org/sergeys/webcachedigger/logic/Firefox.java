package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FF 3.5 on Windows XP
 * 
 * @author sergeys
 *
 */
public class Firefox extends AbstractBrowser {

	@Override
	protected List<String> collectDefaultCachePaths() throws Exception {

		ArrayList<String> paths = new ArrayList<String>();

		// String userName = System.getProperty("user.name");
		String userHome = System.getProperty("user.home");
		// String userDir = System.getProperty("user.dir");

		// paths.add("username: " + userName);
		// paths.add("userhome: " + userHome);
		// paths.add("userdir: " + userDir);

		// TODO: Windows specific path
		String profilesDirPath = userHome
				+ File.separator
				+ "Local Settings\\Application Data\\Mozilla\\Firefox\\Profiles";
		File profilesDir = new File(profilesDirPath);
		if (!profilesDir.isDirectory()) {
			throw new Exception(String.format("'%s' is not a directory.",
					profilesDirPath));
		}

		List<File> profiles = Arrays.asList(profilesDir
				.listFiles(new FileFilter() {
					public boolean accept(File file) {
						return file.isDirectory();
					}
				}));
		for (File profile : profiles) {
			paths.add(String.format("%s\\Cache", profile.getAbsolutePath()));
		}

		return paths;
	}

	@Override
	public List<CachedFile> collectCachedFiles() throws Exception {

		ArrayList<CachedFile> files = new ArrayList<CachedFile>();

		for (String path : getCachePaths()) {

			File directory = new File(path);

			if (directory.isDirectory()) {

				List<File> dirFiles = Arrays.asList(directory
						.listFiles(new FileFilter() {
							public boolean accept(File file) {
								return (!file.isDirectory())
										&& !file.getName().toLowerCase()
												.startsWith("_cache_");
							}
						}));

				for (File file : dirFiles) {
					// TODO: filter by size
					if(file.length() > 100000){
						files.add(new CachedFile(file.getAbsolutePath()));
					}
				}

			} else {
				// TODO: log warning
				throw new Exception(String.format("'%s' is not a directory.",
						path));
			}

		}

		return files;
	}

}
