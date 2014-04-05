package org.sergeys.webcachedigger.ui;

import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import org.sergeys.webcachedigger.logic.CachedFile;

public abstract class AbstractFilePreviewPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private CachedFile cachedFile;
    //private Settings settings;

    /**
     * This is the default constructor
     */
//    public AbstractFilePreviewPanel() {
//        super();
//    }


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

    public static AbstractFilePreviewPanel createFilePreviewPanel(String mimeType, PropertyChangeListener listener){

        AbstractFilePreviewPanel panel = null;

        if(mimeType.startsWith("image/")){ //$NON-NLS-1$
            panel = new ImagePreviewPanel();
        }
        else if(mimeType.startsWith("audio/")){ //$NON-NLS-1$
            panel = new AudioPreviewPanel();
            panel.addPropertyChangeListener(AudioPreviewPanel.PROPERTY_FILE_TO_PLAY, listener);
        }
        else if(mimeType.startsWith("video/")){ //$NON-NLS-1$
            panel = new VideoPreviewPanel();
            panel.addPropertyChangeListener(VideoPreviewPanel.PROPERTY_FILE_TO_PLAY, listener);
        }
        else{
            panel = new EmptyPreviewPanel();
        }

        return panel;
    }


    static class EmptyPreviewPanel extends AbstractFilePreviewPanel{
        private static final long serialVersionUID = 1L;

        //public EmptyPreviewPanel() {};
    }
//    protected Settings getSettings() {
//        return settings;
//    }
//
//    protected void setSettings(Settings s) {
//        this.settings = s;
//    }

}
