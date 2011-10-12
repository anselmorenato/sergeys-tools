package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.sergeys.webcachedigger.logic.Settings;

public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanelBottom = null;
	private JButton jButtonSave = null;
	private JButton jButtonCancel = null;
	private SettingsPanel jPanelSettings = null;

	private Settings settings;  //  @jve:decl-index=0:
	
	/**
	 * @param owner
	 */
	public SettingsDialog(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(521, 197);
		this.setModal(true);
		this.setTitle("Settings");
		this.setContentPane(getJContentPane());
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		
		//pack();
	};
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanelBottom(), BorderLayout.SOUTH);
			jContentPane.add(getJPanelSettings(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanelBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(flowLayout);
			jPanelBottom.add(getJButtonSave(), null);
			jPanelBottom.add(getJButtonCancel(), null);
		}
		return jPanelBottom;
	}

	/**
	 * This method initializes jButtonSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton();
			jButtonSave.setText("Save");
			jButtonSave.setActionCommand(Settings.COMMAND_SAVE_SETTINGS);			
			jButtonSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return jButtonSave;
	}

	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Cancel");
			jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jPanelSettings	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private SettingsPanel getJPanelSettings() {
		if (jPanelSettings == null) {
			jPanelSettings = new SettingsPanel();
			//jPanelSettings.setLayout(new GridBagLayout());
			
		}
		return jPanelSettings;
	}
	
//	private void doSave(){		
//		setVisible(false);
//	}
	
	public void addSaveActionListener(ActionListener l){
		getJButtonSave().addActionListener(l);
	}

	/**
	 * @param settings the settings to set
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
		getJPanelSettings().setSettings(this.settings);
	}

	/**
	 * @return the settings
	 */
	public Settings getSettings() {
		settings = getJPanelSettings().getSettings();
		return settings;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
