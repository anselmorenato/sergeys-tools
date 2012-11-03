package org.sergeys.dupfinder.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;

public class MainPanel extends JPanel {
	public MainPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panelConditions = new JPanel();
		tabbedPane.addTab("Search Conditions", null, panelConditions, null);
		
		JPanel panelResults = new JPanel();
		tabbedPane.addTab("Results", null, panelResults, null);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
