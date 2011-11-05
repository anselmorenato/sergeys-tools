package org.sergeys.coverfinder.ui;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;

public class ImageSearchChooserPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public ImageSearchChooserPanel() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		JLabel lblImageSearchMethod = new JLabel("Image search method:");
		add(lblImageSearchMethod);
		
		
		
		JRadioButton rdbtnBing = new JRadioButton("Bing");
		add(rdbtnBing);
		
		JRadioButton rdbtnGoogle = new JRadioButton("Google");
		add(rdbtnGoogle);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnBing);
		bg.add(rdbtnGoogle);

	}

}
