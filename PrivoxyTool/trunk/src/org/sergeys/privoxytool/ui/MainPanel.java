package org.sergeys.privoxytool.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;

public class MainPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panelActions = new JPanel();
		tabbedPane.addTab("Block Actions", null, panelActions, null);
		
		JPanel panelLogAnalyzer = new JPanel();
		tabbedPane.addTab("Log Analyzer", null, panelLogAnalyzer, null);
		
		JPanel panelPrivoxySettings = new JPanel();
		tabbedPane.addTab("Privoxy Settings", null, panelPrivoxySettings, null);
	}

}
