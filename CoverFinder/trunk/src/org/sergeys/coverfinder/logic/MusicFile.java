package org.sergeys.coverfinder.logic;

import java.io.File;

public class MusicFile
extends File
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MusicFile(File file) {
		super(file.getAbsolutePath());
	}
}
