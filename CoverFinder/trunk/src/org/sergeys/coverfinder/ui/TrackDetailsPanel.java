package org.sergeys.coverfinder.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;

import org.sergeys.coverfinder.logic.Mp3Utils;
import org.sergeys.coverfinder.logic.Track;
import org.sergeys.library.swing.ScaledImage;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

public class TrackDetailsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public TrackDetailsPanel() {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));
		
//		ScaledImage scimgArtwork = new ScaledImage((Image) null, false);
//		add(scimgArtwork, BorderLayout.CENTER);
//		
		JLabel lblMessage = new JLabel(Messages.getString("TrackDetailsPanel.NoArtwork")); //$NON-NLS-1$
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblMessage, BorderLayout.CENTER);
	}

	public void setTrack(Track track){
		removeAll();
		if(track.isHasPicture()){
			Image img = Mp3Utils.getInstance().getArtwork(track.getFile());
			ScaledImage scimgArtwork = new ScaledImage(img, false);
			add(scimgArtwork, BorderLayout.CENTER);			
		}
		else{
			JLabel lblMessage = new JLabel(Messages.getString("TrackDetailsPanel.NoArtwork")); //$NON-NLS-1$
			lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
			add(lblMessage, BorderLayout.CENTER);			
		}
		
		revalidate();
	}
}
