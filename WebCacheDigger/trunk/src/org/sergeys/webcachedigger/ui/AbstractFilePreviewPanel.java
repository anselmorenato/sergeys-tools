package org.sergeys.webcachedigger.ui;

import javax.swing.JPanel;

import org.sergeys.webcachedigger.logic.CachedFile;

public abstract class AbstractFilePreviewPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private CachedFile cachedFile;
	
	/**
	 * This is the default constructor
	 */
//	public AbstractFilePreviewPanel() {
//		super();
//	}


	/**
	 * @param cachedFile the cachedFile to set
	 */
	public void setCachedFile(CachedFile cachedFile) {
		this.cachedFile = cachedFile;
	}

	/**
	 * @return the cachedFile
	 */
	public CachedFile getCachedFile() {
		return cachedFile;
	}

	public static AbstractFilePreviewPanel createFilePreviewPanel(String mimeType){
		AbstractFilePreviewPanel panel = null;
		if(mimeType.startsWith("image/")){
			panel = new ImagePreviewPanel();
		}
		else if(mimeType.startsWith("audio/")){
			panel = new AudioPreviewPanel();
		}
		
		return panel;
	}
}
