package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.NativeLibrary;

public class VideoPreviewPanel extends AbstractFilePreviewPanel {

    private static final long serialVersionUID = 1L;
    // for property listeners
    public static final String PROPERTY_FILE_TO_PLAY = "VideoPreviewPanel_PROPERTY_FILE_TO_PLAY";  //$NON-NLS-1$

    private static ImageIcon iconPlay, iconStop;

    private EmbeddedMediaPlayerComponent mpComponent;
    private boolean isPlaying = false;
    private JButton btnPlay;

    static {
        iconPlay = new ImageIcon(VideoPreviewPanel.class.getResource("/images/player_play.png"));
        iconStop = new ImageIcon(VideoPreviewPanel.class.getResource("/images/player_stop.png"));
    }

    public VideoPreviewPanel() {

        //NativeLibrary.addSearchPath("libvlc", "f:\\bin\\vlc");
        //NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "f:\\bin\\vlc201");
        File libVlcDir = new File(Settings.getInstance().getLibVlc()).getParentFile();
        if(libVlcDir.isDirectory()){
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), libVlcDir.getAbsolutePath());
        }

        setLayout(new BorderLayout(0, 0));

        JLabel lblVideoFile = new JLabel(Messages.getString("VideoPreviewPanel.videoFile")); //$NON-NLS-1$
        lblVideoFile.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblVideoFile, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        add(panel, BorderLayout.SOUTH);

        btnPlay = new JButton("");

        btnPlay.setIcon(iconPlay);
        panel.add(btnPlay);
        //btnPlay.setEnabled(Settings.getInstance().isExternalPlayerConfigured());

        mpComponent = new EmbeddedMediaPlayerComponent();
        add(mpComponent, BorderLayout.CENTER);

        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doPlay(e);
            }
        });
    }

    protected void doPlay(ActionEvent e) {

        //firePropertyChange(PROPERTY_FILE_TO_PLAY, null, getCachedFile());

        togglePlayback(!isPlaying);
    }

    private void togglePlayback(boolean play){
        if(play){
            btnPlay.setIcon(iconStop);
            mpComponent.getMediaPlayer().play();
            isPlaying = true;
        }
        else{
            btnPlay.setIcon(iconPlay);
            mpComponent.getMediaPlayer().stop();
            isPlaying = false;
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);

        if(aFlag){
            String url = "file:///" + getCachedFile().getAbsolutePath();
            System.out.println(url);

            mpComponent.getMediaPlayer().prepareMedia(url);
            togglePlayback(true);
        }
        else{
            togglePlayback(false);
        }
    }
}
