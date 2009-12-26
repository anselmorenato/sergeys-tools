package org.sergeys.webcachedigger.ui;

import javax.swing.JPanel;

import org.sergeys.webcachedigger.logic.CachedFile;

public abstract class FilePreviewPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private CachedFile cachedFile;
	
	/**
	 * This is the default constructor
	 */
	public FilePreviewPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		//this.setSize(300, 200);
		//this.setLayout(new GridBagLayout());
	}

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

	public static FilePreviewPanel createFilePreviewPanel(String mimeType){
		FilePreviewPanel panel = null;
		if(mimeType.startsWith("image/")){
			panel = new ImagePreviewPanel();
		}
		
		return panel;
	}
}
