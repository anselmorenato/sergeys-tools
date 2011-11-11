package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.sergeys.library.swing.ScaledImage;

public class ResultImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ScaledImage scaledImage;
	JLabel lblDimension;
	JLabel lblSize;
	
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
		
		JOptionPane.showMessageDialog(this, "Okay");
	}

	public ResultImagePanel(Image img, int w, int h, long size){
		this();
		
		scaledImage.setImage(img);
		lblDimension.setText(String.format("%dx%d", w, h));
		lblSize.setText((size > 0) ? String.format("%d bytes", size) : "");
		revalidate();
	}
}
