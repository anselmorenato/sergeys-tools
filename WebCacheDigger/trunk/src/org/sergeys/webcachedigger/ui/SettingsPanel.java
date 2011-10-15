package org.sergeys.webcachedigger.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.sergeys.webcachedigger.logic.Database;
import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Settings settings;

	private JLabel jLabel1 = null;
	private JPanel jPanelSavePath = null;
	private JTextField jTextFieldSavePath = null;
	private JButton jButtonSavePath = null;
	private JLabel jLabel2 = null;
	private JPanel jPanel1 = null;
	private JTextField jTextFieldMinFileSizeBytes = null;
	private JLabel jLabel3 = null;
	private JLabel lblExternalMediaPlayer;
	private JPanel panelPlayer;
	private JTextField txtPlayerCommand;
	private JButton buttonPlayer;
	private JButton buttonPlayerHelp;
	private JCheckBox chckbxRenameMpFiles;
	private JPanel panel;
	private JPanel panel_1;
	private JCheckBox chckbxExcludeSaved;
	private JButton btnForget;

	/**
	 * This is the default constructor
	 */
	public SettingsPanel() {
		super();
		setBorder(new EmptyBorder(5, 5, 5, 5));
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.insets = new Insets(0, 0, 5, 0);
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(0, 0, 5, 5);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;

		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.anchor = GridBagConstraints.EAST;
		gridBagConstraints2.insets = new Insets(0, 0, 5, 5);
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 0;

		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.insets = new Insets(0, 0, 5, 0);
		gridBagConstraints3.gridx = 1;
		gridBagConstraints3.gridy = 0;
		jLabel2 = new JLabel();
		jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel2.setText(Messages.getString("SettingsPanel.searchLargeThan")); //$NON-NLS-1$
		jLabel1 = new JLabel();
		jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel1.setText(Messages.getString("SettingsPanel.saveTo")); //$NON-NLS-1$
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 0.0, 0.0, 1.0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		this.setLayout(gridBagLayout);
		//this.setSize(450, 120);
		this.setPreferredSize(new Dimension(477, 288));
		this.add(jLabel1, gridBagConstraints2);
		this.add(getJPanelSavePath(), gridBagConstraints3);
		this.add(jLabel2, gridBagConstraints);
		this.add(getJPanel1(), gridBagConstraints1);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 2;
		add(getPanel_1(), gbc_panel_1);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridwidth = 2;
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		add(getPanel(), gbc_panel);
		GridBagConstraints gbc_lblExternalMediaPlayer = new GridBagConstraints();
		gbc_lblExternalMediaPlayer.anchor = GridBagConstraints.EAST;
		gbc_lblExternalMediaPlayer.insets = new Insets(0, 0, 5, 5);
		gbc_lblExternalMediaPlayer.gridx = 0;
		gbc_lblExternalMediaPlayer.gridy = 4;
		add(getLblExternalMediaPlayer(), gbc_lblExternalMediaPlayer);
		GridBagConstraints gbc_panelPlayer = new GridBagConstraints();
		gbc_panelPlayer.insets = new Insets(0, 0, 5, 0);
		gbc_panelPlayer.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelPlayer.gridx = 1;
		gbc_panelPlayer.gridy = 4;
		add(getPanelPlayer(), gbc_panelPlayer);
	}

	private void setButtonForgetEnabled(){
		boolean hasSaved = false;
		try {
			hasSaved = (Database.getInstance().countSaved() > 0);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		getBtnForget().setEnabled(getChckbxExcludeSaved().isSelected() && hasSaved);
	}
	
	public void setSettings(Settings settings) {
		this.settings = settings;

		getJTextFieldSavePath().setText(settings.getSaveToPath());
		getJTextFieldMinFileSizeBytes().setText(String.valueOf(settings.getMinFileSizeBytes()));
		
		getTxtPlayerCommand().setText(settings.getExternalPlayerCommand());
		getChckbxRenameMpFiles().setSelected(settings.isRenameMp3byTags());

		getChckbxExcludeSaved().setSelected(settings.isExcludeAlreadySaved());
		//getBtnForget().setEnabled(getChckbxExcludeSaved().isSelected());
		setButtonForgetEnabled();
	}

	public Settings getSettings() {
		
		settings.setSaveToPath(getJTextFieldSavePath().getText());
		settings.setMinFileSizeBytes(Long.parseLong(getJTextFieldMinFileSizeBytes().getText()));
		settings.setExternalPlayerCommand(getTxtPlayerCommand().getText());
		settings.setRenameMp3byTags(getChckbxRenameMpFiles().isSelected());
		settings.setExcludeAlreadySaved(getChckbxExcludeSaved().isSelected());

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
			FlowLayout fl_jPanelSavePath = new FlowLayout();
			fl_jPanelSavePath.setAlignment(FlowLayout.LEFT);
			jPanelSavePath.setLayout(fl_jPanelSavePath);
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
			jButtonSavePath.setText("..."); //$NON-NLS-1$
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
		
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setSelectedFile(new File(getSettings().getSaveToPath()));
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
			jLabel3.setText(Messages.getString("SettingsPanel.bytes")); //$NON-NLS-1$
			jPanel1 = new JPanel();
			FlowLayout fl_jPanel1 = new FlowLayout();
			fl_jPanel1.setAlignment(FlowLayout.LEFT);
			jPanel1.setLayout(fl_jPanel1);
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

	private JLabel getLblExternalMediaPlayer() {
		if (lblExternalMediaPlayer == null) {
			lblExternalMediaPlayer = new JLabel(Messages.getString("SettingsPanel.playerCmdLine")); //$NON-NLS-1$
			lblExternalMediaPlayer.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblExternalMediaPlayer;
	}
	private JPanel getPanelPlayer() {
		if (panelPlayer == null) {
			panelPlayer = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelPlayer.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			panelPlayer.add(getTxtPlayerCommand());
			panelPlayer.add(getButtonPlayer());
			panelPlayer.add(getButtonPlayerHelp());
		}
		return panelPlayer;
	}
	private JTextField getTxtPlayerCommand() {
		if (txtPlayerCommand == null) {
			txtPlayerCommand = new JTextField();
			txtPlayerCommand.setColumns(20);
		}
		return txtPlayerCommand;
	}
	private JButton getButtonPlayer() {
		if (buttonPlayer == null) {
			buttonPlayer = new JButton("..."); //$NON-NLS-1$
			buttonPlayer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doChoosePlayer(e);
				}
			});
		}
		return buttonPlayer;
	}
	
	protected void doChoosePlayer(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(Settings.isOSWindows()){
			fc.addChoosableFileFilter(new FileFilter(){
	
				@Override
				public boolean accept(File f) {				
					return f.isDirectory() || f.getName().toLowerCase().endsWith(".exe"); //$NON-NLS-1$
				}
	
				@Override
				public String getDescription() {				
					return Messages.getString("SettingsPanel.programs") + " (*.exe)"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				
			});
		}
		
		if (fc.showOpenDialog(this.getParent()) == JFileChooser.APPROVE_OPTION) {			
			String path = fc.getSelectedFile().getAbsolutePath();
			if(path.contains(" ")){ //$NON-NLS-1$
				path = "\"" + path + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			path = path + " " + Settings.EXT_PLAYER_FILEPATH; //$NON-NLS-1$
			
			getTxtPlayerCommand().setText(path);
		}

		
	}

	private JButton getButtonPlayerHelp() {
		if (buttonPlayerHelp == null) {
			buttonPlayerHelp = new JButton("?"); //$NON-NLS-1$
			buttonPlayerHelp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doHelpChoosePlayer(e);
				}
			});
		}
		return buttonPlayerHelp;
	}

	protected void doHelpChoosePlayer(ActionEvent e) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(this, Messages.getString("SettingsPanel.cmdLineHelp")); //$NON-NLS-1$
		
	}
	private JCheckBox getChckbxRenameMpFiles() {
		if (chckbxRenameMpFiles == null) {
			chckbxRenameMpFiles = new JCheckBox(Messages.getString("SettingsPanel.chckbxRenameMpFiles.text")); //$NON-NLS-1$
		}
		return chckbxRenameMpFiles;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			panel.add(getChckbxRenameMpFiles());
		}
		return panel;
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			panel_1.add(getChckbxExcludeSaved());
			panel_1.add(getBtnForget());
		}
		return panel_1;
	}
			
	private JCheckBox getChckbxExcludeSaved() {
		if (chckbxExcludeSaved == null) {
			chckbxExcludeSaved = new JCheckBox(Messages.getString("SettingsPanel.chckbxDoNotShow.text")); //$NON-NLS-1$
			chckbxExcludeSaved.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					doExcludeChanged(e);
				}
			});
		}
		return chckbxExcludeSaved;
	}
	
	protected void doExcludeChanged(ChangeEvent e) {		
		setButtonForgetEnabled();
	}

	private JButton getBtnForget() {
		if (btnForget == null) {
			btnForget = new JButton(Messages.getString("SettingsPanel.btnForget.text")); //$NON-NLS-1$
			btnForget.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doForgetSavedFiles(e);
				}
			});
		}
		return btnForget;
	}

	protected void doForgetSavedFiles(ActionEvent e) {
		try {
			Database.getInstance().clear();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		setButtonForgetEnabled();
	}
} 
