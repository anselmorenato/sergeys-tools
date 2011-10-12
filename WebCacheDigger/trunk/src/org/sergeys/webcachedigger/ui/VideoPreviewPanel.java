package org.sergeys.webcachedigger.ui;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class VideoPreviewPanel extends AbstractFilePreviewPanel {
	
	// for property listeners
	public static final String PROPERTY_FILE_TO_PLAY = "VideoPreviewPanel_PROPERTY_FILE_TO_PLAY"; 
	
	public VideoPreviewPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPlay(e);
			}
		});
		add(btnPlay, BorderLayout.SOUTH);
		
		JLabel lblVideoFile = new JLabel("Video File");
		lblVideoFile.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblVideoFile, BorderLayout.CENTER);
	}

	protected void doPlay(ActionEvent e) {
		// TODO Auto-generated method stub
		firePropertyChange(PROPERTY_FILE_TO_PLAY, null, getCachedFile());
	}

}
