package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.sergeys.coverfinder.logic.ImageSearchResult;
import org.sergeys.library.swing.ScaledImage;

public class ResultImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SELECTED_IMAGE_PROPERTY = "SELECTED_IMAGE_PROPERTY";
	
	ScaledImage scaledImage;
	JLabel lblDimension;
	JLabel lblSize;
	ImageSearchResult imgResult;
	
	/**
	 * Create the panel.
	 */
	public ResultImagePanel() {
				
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));
		
		scaledImage = new ScaledImage((Image) null, false);
		scaledImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				doClicked(e);
			}
		});
		scaledImage.setPreferredSize(new Dimension(100, 100));
		add(scaledImage, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		lblDimension = new JLabel("Dimension");
		lblDimension.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(lblDimension);
		
		lblSize = new JLabel("Size");
		lblSize.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(lblSize);

	}

	protected void doClicked(MouseEvent e) {
		firePropertyChange(SELECTED_IMAGE_PROPERTY, null, imgResult);
	}

	public ResultImagePanel(Image img, ImageSearchResult imgResult){
		this();
		
		this.imgResult = imgResult;
		
		scaledImage.setImage(img);
		lblDimension.setText(String.format("%dx%d", imgResult.getWidth(), imgResult.getHeight()));
		lblSize.setText((imgResult.getFileSize() > 0) ? String.format("%d bytes", imgResult.getFileSize()) : "");
		revalidate();
	}
}
