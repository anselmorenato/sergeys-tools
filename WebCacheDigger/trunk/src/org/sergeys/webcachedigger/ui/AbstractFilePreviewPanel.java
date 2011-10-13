package org.sergeys.webcachedigger.ui;

import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.Settings;

public abstract class AbstractFilePreviewPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private CachedFile cachedFile;
	private Settings settings;
	
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

	public static AbstractFilePreviewPanel createFilePreviewPanel(String mimeType, PropertyChangeListener listener, Settings settings){
		
		AbstractFilePreviewPanel panel = null;
		
		if(mimeType.startsWith("image/")){
			panel = new ImagePreviewPanel();
			
		}
		else if(mimeType.startsWith("audio/")){
			panel = new AudioPreviewPanel(settings);			
			panel.addPropertyChangeListener(AudioPreviewPanel.PROPERTY_FILE_TO_PLAY, listener);
		}
		else if(mimeType.startsWith("video/")){
			panel = new VideoPreviewPanel(settings);			
			panel.addPropertyChangeListener(VideoPreviewPanel.PROPERTY_FILE_TO_PLAY, listener);
		}
						
		return panel;
	}

	protected Settings getSettings() {
		return settings;
	}

	protected void setSettings(Settings s) {
		this.settings = s;
	}

}
