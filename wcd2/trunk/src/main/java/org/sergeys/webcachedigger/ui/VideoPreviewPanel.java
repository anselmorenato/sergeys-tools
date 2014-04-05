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
import org.sergeys.webcachedigger.logic.Settings.MediaPlayerType;

//import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
//import uk.co.caprica.vlcj.runtime.RuntimeUtil;

//import com.sun.jna.NativeLibrary;

public class VideoPreviewPanel extends AbstractFilePreviewPanel {

    private static final long serialVersionUID = 1L;
    // for property listeners
    public static final String PROPERTY_FILE_TO_PLAY = "VideoPreviewPanel_PROPERTY_FILE_TO_PLAY";  //$NON-NLS-1$

    private static ImageIcon iconPlay, iconStop;

//    private EmbeddedMediaPlayerComponent mpComponent;
    private boolean isPlaying = false;
    private JButton btnPlay;

    static {
        iconPlay = new ImageIcon(VideoPreviewPanel.class.getResource("/images/player_play.png")); //$NON-NLS-1$
        iconStop = new ImageIcon(VideoPreviewPanel.class.getResource("/images/player_stop.png")); //$NON-NLS-1$
    }

    public VideoPreviewPanel() {

        setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        add(panel, BorderLayout.SOUTH);

        btnPlay = new JButton(""); //$NON-NLS-1$

        btnPlay.setIcon(iconPlay);
        panel.add(btnPlay);
        //btnPlay.setEnabled(Settings.getInstance().isExternalPlayerConfigured());

        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doPlay(e);
            }
        });

        if(Settings.getInstance().getMediaPlayerType() == MediaPlayerType.Vlc){
            //NativeLibrary.addSearchPath("libvlc", "f:\\bin\\vlc");
            //NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "f:\\bin\\vlc201");
            File libVlcDir = new File(Settings.getInstance().getLibVlc()).getParentFile();
            if(libVlcDir.isDirectory()){
//                NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), libVlcDir.getAbsolutePath());
            }

//            mpComponent = new EmbeddedMediaPlayerComponent();
//            add(mpComponent, BorderLayout.CENTER);
        }
        else{
            JLabel lblVideoFile = new JLabel(Messages.getString("VideoPreviewPanel.videoFile")); //$NON-NLS-1$
            lblVideoFile.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblVideoFile, BorderLayout.NORTH);
        }
    }

    protected void doPlay(ActionEvent e) {
        if(Settings.getInstance().getMediaPlayerType() == MediaPlayerType.Vlc){
            toggleVlcPlayback(!isPlaying);
        }
        else{
            // let external player play this
            firePropertyChange(PROPERTY_FILE_TO_PLAY, null, getCachedFile());
        }
    }

    private void toggleVlcPlayback(boolean play){
        if(play){
            btnPlay.setIcon(iconStop);
//            mpComponent.getMediaPlayer().play();
            isPlaying = true;
        }
        else{
            btnPlay.setIcon(iconPlay);
//            mpComponent.getMediaPlayer().stop();
            isPlaying = false;
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);

        if(Settings.getInstance().getMediaPlayerType() == MediaPlayerType.Vlc){
            if(aFlag){
                String url = "file:///" + getCachedFile().getAbsolutePath(); //$NON-NLS-1$
                System.out.println(url);

//	            mpComponent.getMediaPlayer().prepareMedia(url);
                toggleVlcPlayback(true);
            }
            else{
                toggleVlcPlayback(false);
            }
        }
    }
}
