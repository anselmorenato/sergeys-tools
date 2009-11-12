package org.sergeys.webcachedigger.ui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;

import org.sergeys.webcachedigger.logic.Settings;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Dimension;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Settings settings;  //  @jve:decl-index=0:

	private JLabel jLabel1 = null;

	private JPanel jPanelSavePath = null;

	private JTextField jTextFieldSavePath = null;

	private JButton jButtonSavePath = null;

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
		jLabel1 = new JLabel();
		jLabel1.setText("Save files to:");
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(1);
		this.setLayout(gridLayout);
		this.setSize(618, 200);
		this.add(jLabel1, null);
		this.add(getJPanelSavePath(), null);
	}

	public void setSettings(Settings settings){
		this.settings = settings;
	}
	
	public Settings getSettings(){
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
		}
		return jButtonSavePath;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
