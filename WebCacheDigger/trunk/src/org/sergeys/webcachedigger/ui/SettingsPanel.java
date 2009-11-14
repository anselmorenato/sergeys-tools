package org.sergeys.webcachedigger.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.sergeys.webcachedigger.logic.Settings;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Settings settings; // @jve:decl-index=0:

	private JLabel jLabel1 = null;

	private JPanel jPanelSavePath = null;

	private JTextField jTextFieldSavePath = null;

	private JButton jButtonSavePath = null;

	private JLabel jLabel2 = null;

	private JPanel jPanel1 = null;

	private JTextField jTextFieldMinFileSizeBytes = null;

	private JLabel jLabel3 = null;

	/**
	 * This is the default constructor
	 */
	public SettingsPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.gridy = 1;

		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.anchor = GridBagConstraints.EAST;
		gridBagConstraints2.gridy = 0;

		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 1;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.gridy = 0;
		jLabel2 = new JLabel();
		jLabel2.setText("Search for files larger than:");
		jLabel1 = new JLabel();
		jLabel1.setText("Save files to:");
		this.setLayout(new GridBagLayout());
		this.setSize(450, 120);
		this.setPreferredSize(new Dimension(450, 120));
		this.add(jLabel1, gridBagConstraints2);
		this.add(getJPanelSavePath(), gridBagConstraints3);
		this.add(jLabel2, gridBagConstraints);
		this.add(getJPanel1(), gridBagConstraints1);
	}

	public void setSettings(Settings settings) {
		this.settings = settings;

		getJTextFieldSavePath().setText(
				this.settings.getProperty(Settings.SAVE_TO_PATH));
		if (this.settings.containsKey(Settings.MIN_FILE_SIZE_BYTES)) {

			getJTextFieldMinFileSizeBytes().setText(
					String.valueOf(this.settings
							.getIntProperty(Settings.MIN_FILE_SIZE_BYTES)));
		}

	}

	public Settings getSettings() {
		settings.setProperty(Settings.SAVE_TO_PATH, getJTextFieldSavePath()
				.getText());
		try {
			settings.setIntProperty(Settings.MIN_FILE_SIZE_BYTES,
					getJTextFieldMinFileSizeBytes().getText());
		} catch (NumberFormatException ex) {
			getJTextFieldMinFileSizeBytes().setText(null);				
		}

		return settings;
	}

	/**
	 * This method initializes jPanelSavePath
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelSavePath() {
		if (jPanelSavePath == null) {
			jPanelSavePath = new JPanel();
			jPanelSavePath.setLayout(new FlowLayout());
			jPanelSavePath.add(getJTextFieldSavePath(), null);
			jPanelSavePath.add(getJButtonSavePath(), null);
		}
		return jPanelSavePath;
	}

	/**
	 * This method initializes jTextFieldSavePath
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldSavePath() {
		if (jTextFieldSavePath == null) {
			jTextFieldSavePath = new JTextField();
			jTextFieldSavePath.setColumns(20);
		}
		return jTextFieldSavePath;
	}

	/**
	 * This method initializes jButtonSavePath
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSavePath() {
		if (jButtonSavePath == null) {
			jButtonSavePath = new JButton();
			jButtonSavePath.setText("...");
			jButtonSavePath
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							SettingsPanel.this.choosePath();
						}
					});
		}
		return jButtonSavePath;
	}

	private void choosePath() {
		JFileChooser fc = new JFileChooser();
		// fc.showDialog(parent, approveButtonText)
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setSelectedFile(new File(getSettings().getProperty(
				Settings.SAVE_TO_PATH)));
		if (fc.showOpenDialog(this.getParent()) == JFileChooser.APPROVE_OPTION) {
			getJTextFieldSavePath().setText(
					fc.getSelectedFile().getAbsolutePath());
		}
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("bytes");
			jPanel1 = new JPanel();
			jPanel1.setLayout(new FlowLayout());
			jPanel1.add(getJTextFieldMinFileSizeBytes(), null);
			jPanel1.add(jLabel3, null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jTextFieldMinFileSizeBytes
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldMinFileSizeBytes() {
		if (jTextFieldMinFileSizeBytes == null) {
			jTextFieldMinFileSizeBytes = new JTextField();
			jTextFieldMinFileSizeBytes.setColumns(8);
		}
		return jTextFieldMinFileSizeBytes;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
