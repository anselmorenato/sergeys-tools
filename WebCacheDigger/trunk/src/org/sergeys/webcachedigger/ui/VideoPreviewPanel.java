package org.sergeys.webcachedigger.ui;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;
import javax.swing.JPanel;

public class VideoPreviewPanel extends AbstractFilePreviewPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// for property listeners
	public static final String PROPERTY_FILE_TO_PLAY = "VideoPreviewPanel_PROPERTY_FILE_TO_PLAY";  //$NON-NLS-1$
	
	public VideoPreviewPanel() {
				
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblVideoFile = new JLabel(Messages.getString("VideoPreviewPanel.videoFile")); //$NON-NLS-1$
		lblVideoFile.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblVideoFile, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		
		JButton btnPlay = new JButton(Messages.getString("VideoPreviewPanel.play")); //$NON-NLS-1$
		panel.add(btnPlay);
		btnPlay.setEnabled(Settings.getInstance().isExternalPlayerConfigured());
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPlay(e);
			}
		});
	}

	protected void doPlay(ActionEvent e) {
		// TODO Auto-generated method stub
		firePropertyChange(PROPERTY_FILE_TO_PLAY, null, getCachedFile());
	}

}
