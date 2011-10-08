package org.sergeys.webcachedigger.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class MainWinTopPanel2 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public MainWinTopPanel2() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblBrowsers = new JLabel("Search in:");
		GridBagConstraints gbc_lblBrowsers = new GridBagConstraints();
		gbc_lblBrowsers.anchor = GridBagConstraints.EAST;
		gbc_lblBrowsers.insets = new Insets(0, 0, 5, 5);
		gbc_lblBrowsers.gridx = 0;
		gbc_lblBrowsers.gridy = 0;
		add(lblBrowsers, gbc_lblBrowsers);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.WEST;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		
		JToggleButton tglbtnFirefox = new JToggleButton("Firefox");
		panel.add(tglbtnFirefox);
		
		JToggleButton tglbtnInternetExplorer = new JToggleButton("Internet Explorer");
		panel.add(tglbtnInternetExplorer);
		
		JLabel lblMedia = new JLabel("Search for:");
		GridBagConstraints gbc_lblMedia = new GridBagConstraints();
		gbc_lblMedia.anchor = GridBagConstraints.EAST;
		gbc_lblMedia.insets = new Insets(0, 0, 5, 5);
		gbc_lblMedia.gridx = 0;
		gbc_lblMedia.gridy = 1;
		add(lblMedia, gbc_lblMedia);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.anchor = GridBagConstraints.WEST;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 1;
		add(panel_1, gbc_panel_1);
		
		JToggleButton tglbtnImages = new JToggleButton("Images");
		panel_1.add(tglbtnImages);
		
		JToggleButton tglbtnAudio = new JToggleButton("Audio");
		panel_1.add(tglbtnAudio);
		
		JToggleButton tglbtnVideo = new JToggleButton("Video");
		panel_1.add(tglbtnVideo);
		
		JToggleButton tglbtnOther = new JToggleButton("Other");
		panel_1.add(tglbtnOther);
		
		JLabel lblLargeThan = new JLabel("Large than");
		GridBagConstraints gbc_lblLargeThan = new GridBagConstraints();
		gbc_lblLargeThan.anchor = GridBagConstraints.EAST;
		gbc_lblLargeThan.insets = new Insets(0, 0, 0, 5);
		gbc_lblLargeThan.gridx = 0;
		gbc_lblLargeThan.gridy = 2;
		add(lblLargeThan, gbc_lblLargeThan);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.anchor = GridBagConstraints.WEST;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 2;
		add(panel_2, gbc_panel_2);
		
		textField = new JTextField();
		textField.setText("100000");
		panel_2.add(textField);
		textField.setColumns(10);
		
		JLabel lblBytes = new JLabel("bytes");
		panel_2.add(lblBytes);

	}

}
