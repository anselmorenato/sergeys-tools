package org.sergeys.webcachedigger.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JToggleButton;
import javax.swing.JButton;

public class MainWinTopPanel extends JPanel {
	public MainWinTopPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelLeft = new JPanel();
		add(panelLeft, BorderLayout.WEST);
		panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
		
		JLabel lblBrowsers = new JLabel("Browsers:");
		panelLeft.add(lblBrowsers);
		
		JCheckBox chckbxBrowser = new JCheckBox("Browser 1");
		panelLeft.add(chckbxBrowser);
		
		JCheckBox chckbxBrowser_1 = new JCheckBox("Browser 2");
		panelLeft.add(chckbxBrowser_1);
		
		JPanel panelCenter = new JPanel();
		FlowLayout fl_panelCenter = (FlowLayout) panelCenter.getLayout();
		fl_panelCenter.setAlignment(FlowLayout.LEFT);
		add(panelCenter, BorderLayout.CENTER);
		
		JLabel lblSearch = new JLabel("Search for:");
		panelCenter.add(lblSearch);
		
		JToggleButton tglbtnImages = new JToggleButton("Images");
		panelCenter.add(tglbtnImages);
		
		JToggleButton tglbtnAudio = new JToggleButton("Audio");
		panelCenter.add(tglbtnAudio);
		
		JToggleButton tglbtnVideo = new JToggleButton("Video");
		panelCenter.add(tglbtnVideo);
		
		JToggleButton tglbtnOther = new JToggleButton("Other");
		panelCenter.add(tglbtnOther);
		
		JPanel panelBottom = new JPanel();
		add(panelBottom, BorderLayout.SOUTH);
		
		JButton btnSearch = new JButton("Search");
		panelBottom.add(btnSearch);
	}

}
