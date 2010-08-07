package org.sergeys.coverfinder.ui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ActionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox jComboBoxSource = null;
	private JButton jButtonFindAlbums = null;

	/**
	 * This is the default constructor
	 */
	public ActionPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		this.setLayout(flowLayout);
		this.setSize(300, 200);
		this.add(getJComboBoxSource(), null);
		this.add(getJButtonFindAlbums(), null);
	}

	/**
	 * This method initializes jComboBoxSource	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxSource() {
		if (jComboBoxSource == null) {
			jComboBoxSource = new JComboBox();
		}
		return jComboBoxSource;
	}

	/**
	 * This method initializes jButtonFindAlbums	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonFindAlbums() {
		if (jButtonFindAlbums == null) {
			jButtonFindAlbums = new JButton();
			jButtonFindAlbums.setText("Find Albums");
		}
		return jButtonFindAlbums;
	}

}
