package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import com.sun.jna.NativeLibrary;

public class VideoPreviewPanel extends AbstractFilePreviewPanel {
	
	private static final long serialVersionUID = 1L;
	// for property listeners
	public static final String PROPERTY_FILE_TO_PLAY = "VideoPreviewPanel_PROPERTY_FILE_TO_PLAY";  //$NON-NLS-1$
	private EmbeddedMediaPlayer vlcPlayer;
	
	public VideoPreviewPanel() {
				
		NativeLibrary.addSearchPath("libvlc", "f:\\bin\\vlc");
		
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblVideoFile = new JLabel(Messages.getString("VideoPreviewPanel.videoFile")); //$NON-NLS-1$
		lblVideoFile.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblVideoFile, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		
		JButton btnPlay = new JButton(""); //$NON-NLS-1$
		btnPlay.setIcon(new ImageIcon(VideoPreviewPanel.class.getResource("/images/player_play.png"))); //$NON-NLS-1$
		panel.add(btnPlay);
		btnPlay.setEnabled(Settings.getInstance().isExternalPlayerConfigured());
		
		Canvas canvasVideo = new Canvas();
		add(canvasVideo, BorderLayout.CENTER);
		
		MediaPlayerFactory factory = new MediaPlayerFactory();
		vlcPlayer = factory.newEmbeddedMediaPlayer();
		vlcPlayer.setVideoSurface(factory.newVideoSurface(canvasVideo));
		vlcPlayer.setPlaySubItems(true);
	    vlcPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
	        @Override
	        public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
	          List<String> items = mediaPlayer.subItems();
	          System.out.println(items);
	        }
	      });
		
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPlay(e);
			}
		});
	}

	protected void doPlay(ActionEvent e) {
		
		//firePropertyChange(PROPERTY_FILE_TO_PLAY, null, getCachedFile());
		
		vlcPlayer.play();				
	}
		
	@Override
	public void setVisible(boolean aFlag) {		
		super.setVisible(aFlag);
		
		if(aFlag){
			String url = "file:///" + getCachedFile().getAbsolutePath();
			System.out.println(url);
			
			vlcPlayer.prepareMedia(url);
			//vlcPlayer.parseMedia();
			vlcPlayer.play();
			//vlcPlayer.pause();
		}
		else{
			vlcPlayer.stop();
		}		
	}	
}
