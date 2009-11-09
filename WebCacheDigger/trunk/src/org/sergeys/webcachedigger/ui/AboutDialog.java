package org.sergeys.webcachedigger.ui;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Dimension;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTabbedPane jTabbedPaneCenter = null;
	private JPanel jPanelBottom = null;
	private JButton jButtonOK = null;
	private JPanel jPanelAbout = null;
	private JLabel jLabelTitle = null;
	private JLabel jLabelVersion = null;
	private JLabel jLabelAuthor = null;
	private JPanel jPanelSystem = null;

	/**
	 * @param owner
	 */
	public AboutDialog(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setPreferredSize(new Dimension(300, 200));
		this.setTitle("About");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJTabbedPaneCenter(), BorderLayout.CENTER);
			jContentPane.add(getJPanelBottom(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTabbedPaneCenter	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPaneCenter() {
		if (jTabbedPaneCenter == null) {
			jTabbedPaneCenter = new JTabbedPane();
			jTabbedPaneCenter.addTab("About", null, getJPanelAbout(), null);
			jTabbedPaneCenter.addTab("System", null, getJPanelSystem(), null);
		}
		return jTabbedPaneCenter;
	}

	/**
	 * This method initializes jPanelBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(new GridBagLayout());
			jPanelBottom.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			jPanelBottom.add(getJButtonOK(), gridBagConstraints);
		}
		return jPanelBottom;
	}

	/**
	 * This method initializes jButtonOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setText("OK");
			jButtonOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jPanelAbout	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelAbout() {
		if (jPanelAbout == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			jLabelAuthor = new JLabel();
			jLabelAuthor.setText("Sergey Selivanov");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			jLabelVersion = new JLabel();
			jLabelVersion.setText("1.01 Nov 7 2009");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			jLabelTitle = new JLabel();
			jLabelTitle.setText("Web Cache Digger");
			jPanelAbout = new JPanel();
			jPanelAbout.setLayout(new GridBagLayout());
			jPanelAbout.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			jPanelAbout.add(jLabelTitle, gridBagConstraints1);
			jPanelAbout.add(jLabelVersion, gridBagConstraints2);
			jPanelAbout.add(jLabelAuthor, gridBagConstraints3);
		}
		return jPanelAbout;
	}

	/**
	 * This method initializes jPanelSystem	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSystem() {
		if (jPanelSystem == null) {
			jPanelSystem = new JPanel();
			jPanelSystem.setLayout(new GridBagLayout());
		}
		return jPanelSystem;
	}

}
