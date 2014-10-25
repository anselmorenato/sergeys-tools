package org.sergeys.webcachedigger.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;





import org.sergeys.library.OsUtils;
import org.sergeys.library.swing.DirSelectorDialog;
import org.sergeys.webcachedigger.logic.Database;
import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;
import org.sergeys.webcachedigger.logic.Settings.CompareFilesType;
import org.sergeys.webcachedigger.logic.Settings.MediaPlayerType;

public class SettingsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

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
    private JLabel lblCompareFiles;
    private JPanel panelCompare;
    private JRadioButton rdbtnFast;
    private JRadioButton rdbtnFull;
    private JButton btnForgetIgnored;
    private JPanel panel_2;
    private JPanel panelSelectPlayer;
    private JRadioButton rdbtnUseVlc;
    private JRadioButton rdbtnUseExternalPlayer;
    private JLabel lblMediaPlayer;
    private JPanel panelVLC;
    private JLabel lblLocationOfLibvlc;
    private JTextField textLibvlcLocation;
    private JButton buttonLibvlc;
    private JButton btnLibvlcDetect;

    private Frame owner;

    /**
     * This is the default constructor
     * @param owner
     */
    public SettingsPanel(Frame owner) {
        super();
        this.owner = owner;
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
        gridBagLayout.rowWeights = new double[]{1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0};
        gridBagLayout.columnWeights = new double[]{1.0, 1.0};
        this.setLayout(gridBagLayout);
        //this.setSize(450, 120);
        this.setPreferredSize(new Dimension(488, 398));
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
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.gridwidth = 2;
        gbc_panel_2.insets = new Insets(0, 0, 5, 0);
        gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_2.gridx = 0;
        gbc_panel_2.gridy = 3;
        add(getPanel_2(), gbc_panel_2);
        GridBagConstraints gbc_lblCompareFiles = new GridBagConstraints();
        gbc_lblCompareFiles.anchor = GridBagConstraints.EAST;
        gbc_lblCompareFiles.insets = new Insets(0, 0, 5, 5);
        gbc_lblCompareFiles.gridx = 0;
        gbc_lblCompareFiles.gridy = 4;
        add(getLblCompareFiles(), gbc_lblCompareFiles);
        GridBagConstraints gbc_panelCompare = new GridBagConstraints();
        gbc_panelCompare.insets = new Insets(0, 0, 5, 0);
        gbc_panelCompare.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelCompare.gridx = 1;
        gbc_panelCompare.gridy = 4;
        add(getPanelCompare(), gbc_panelCompare);
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.gridwidth = 2;
        gbc_panel.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 5;
        add(getPanel(), gbc_panel);
        GridBagConstraints gbc_panelSelectPlayer = new GridBagConstraints();
        gbc_panelSelectPlayer.gridwidth = 2;
        gbc_panelSelectPlayer.insets = new Insets(0, 0, 5, 0);
        gbc_panelSelectPlayer.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelSelectPlayer.gridx = 0;
        gbc_panelSelectPlayer.gridy = 6;
        add(getPanelSelectPlayer(), gbc_panelSelectPlayer);
        GridBagConstraints gbc_lblLocationOfLibvlc = new GridBagConstraints();
        gbc_lblLocationOfLibvlc.anchor = GridBagConstraints.EAST;
        gbc_lblLocationOfLibvlc.insets = new Insets(0, 0, 5, 5);
        gbc_lblLocationOfLibvlc.gridx = 0;
        gbc_lblLocationOfLibvlc.gridy = 7;
        add(getLblLocationOfLibvlc(), gbc_lblLocationOfLibvlc);
        GridBagConstraints gbc_panelVLC = new GridBagConstraints();
        gbc_panelVLC.insets = new Insets(0, 0, 5, 0);
        gbc_panelVLC.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelVLC.gridx = 1;
        gbc_panelVLC.gridy = 7;
        add(getPanelVLC(), gbc_panelVLC);
        GridBagConstraints gbc_lblExternalMediaPlayer = new GridBagConstraints();
        gbc_lblExternalMediaPlayer.anchor = GridBagConstraints.EAST;
        gbc_lblExternalMediaPlayer.insets = new Insets(0, 0, 0, 5);
        gbc_lblExternalMediaPlayer.gridx = 0;
        gbc_lblExternalMediaPlayer.gridy = 8;
        add(getLblExternalMediaPlayer(), gbc_lblExternalMediaPlayer);
        GridBagConstraints gbc_panelPlayer = new GridBagConstraints();
        gbc_panelPlayer.fill = GridBagConstraints.HORIZONTAL;
        gbc_panelPlayer.gridx = 1;
        gbc_panelPlayer.gridy = 8;
        add(getPanelPlayer(), gbc_panelPlayer);
    }

    private void setForgetButtonsEnabled(){
        boolean hasSaved = false;
        boolean hasIgnored = false;
        if(getChckbxExcludeSaved().isSelected()){
            try {
                hasSaved = (Database.getInstance().countSaved() > 0);
                hasIgnored = (Database.getInstance().countIgnored() > 0);
            } catch (SQLException e1) {
                Settings.getLogger().error("", e1); //$NON-NLS-1$
            }
            getBtnForget().setEnabled(hasSaved);
            getBtnForgetIgnored().setEnabled(hasIgnored);
        }
        else{
            getBtnForget().setEnabled(false);
            getBtnForgetIgnored().setEnabled(false);
        }
    }

    public void setSettings() {

        getJTextFieldSavePath().setText(Settings.getInstance().getSaveToPath());
        getJTextFieldMinFileSizeBytes().setText(String.valueOf(Settings.getInstance().getMinFileSizeBytes()));

        getTxtPlayerCommand().setText(Settings.getInstance().getExternalPlayerCommand());
        getChckbxRenameMpFiles().setSelected(Settings.getInstance().isRenameMp3byTags());

        getChckbxExcludeSaved().setSelected(Settings.getInstance().isExcludeSavedAndIgnored());
        //getBtnForget().setEnabled(getChckbxExcludeSaved().isSelected());
        setForgetButtonsEnabled();

        switch(Settings.getInstance().getCompareFilesMethod()){
            case Fast:
                getRdbtnFast().setSelected(true);
                break;
            case Full:
                getRdbtnFull().setSelected(true);
                break;
        }

        switch(Settings.getInstance().getMediaPlayerType()){
            case External:
                getRdbtnUseExternalPlayer().setSelected(true);
                break;
            case Vlc:
                getRdbtnUseVlc().setSelected(true);
                break;
//		default:
//			break;
        }

        //getTextLibvlcLocation().setText(Settings.getInstance().getLibVlc());
    }

    public void updateSettings() {

        Settings.getInstance().setSaveToPath(getJTextFieldSavePath().getText());
        Settings.getInstance().setMinFileSizeBytes(Long.parseLong(getJTextFieldMinFileSizeBytes().getText()));
        Settings.getInstance().setExternalPlayerCommand(getTxtPlayerCommand().getText());
        Settings.getInstance().setRenameMp3byTags(getChckbxRenameMpFiles().isSelected());
        Settings.getInstance().setExcludeSavedAndIgnored(getChckbxExcludeSaved().isSelected());

        if(getRdbtnFast().isSelected()){
            Settings.getInstance().setCompareFilesMethod(CompareFilesType.Fast);
        }
        else if(getRdbtnFull().isSelected()){
            Settings.getInstance().setCompareFilesMethod(CompareFilesType.Full);
        }
        else{
            Settings.getInstance().setCompareFilesMethod(null);
        }

        if(getRdbtnUseExternalPlayer().isSelected()){
            Settings.getInstance().setMediaPlayerType(MediaPlayerType.External);
        }
        else if(getRdbtnUseVlc().isSelected()){
            Settings.getInstance().setMediaPlayerType(MediaPlayerType.Vlc);
        }
        else{
            Settings.getInstance().setMediaPlayerType(null);
        }

        //Settings.getInstance().setLibVlc(getTextLibvlcLocation().getText());
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

    private DirSelectorDialog dirSelector = null;

    protected void choosePath() {
        /*
        JFileChooser fc = new JFileChooser();

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(Settings.getInstance().getSaveToPath() != null){
            fc.setSelectedFile(new File(Settings.getInstance().getSaveToPath()));
        }

        if (fc.showOpenDialog(this.getParent()) == JFileChooser.APPROVE_OPTION) {
            getJTextFieldSavePath().setText(
                    fc.getSelectedFile().getAbsolutePath());
        }
        */

        if (dirSelector == null) {
            dirSelector = new DirSelectorDialog(owner);
            dirSelector.setLocationRelativeTo(owner);
        }

        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(DirSelectorDialog.DIRECTORY_SELECTED)) {

                    String path = evt.getNewValue().toString();

                    getJTextFieldSavePath().setText(path);
                }
            }
        };

        dirSelector.addPropertyChangeListener(DirSelectorDialog.DIRECTORY_SELECTED, listener);
        dirSelector.setVisible(true);	// waits for closing
        dirSelector.removePropertyChangeListener(DirSelectorDialog.DIRECTORY_SELECTED, listener);
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
        if(OsUtils.isWindows()){
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
        setForgetButtonsEnabled();
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
            Database.getInstance().clearSaved();
        } catch (SQLException e1) {
            Settings.getLogger().error("", e1); //$NON-NLS-1$
        }
        setForgetButtonsEnabled();
    }

    private JLabel getLblCompareFiles() {
        if (lblCompareFiles == null) {
            lblCompareFiles = new JLabel(Messages.getString("SettingsPanel.lblCompareFiles.text")); //$NON-NLS-1$
        }
        return lblCompareFiles;
    }
    private JPanel getPanelCompare() {
        if (panelCompare == null) {
            panelCompare = new JPanel();
            panelCompare.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

            ButtonGroup bg = new ButtonGroup();
            bg.add(getRdbtnFast());
            bg.add(getRdbtnFull());

            panelCompare.add(getRdbtnFast());
            panelCompare.add(getRdbtnFull());
        }
        return panelCompare;
    }
    private JRadioButton getRdbtnFast() {
        if (rdbtnFast == null) {
            rdbtnFast = new JRadioButton(Messages.getString("SettingsPanel.rdbtnFastrecommended.text")); //$NON-NLS-1$
        }
        return rdbtnFast;
    }
    private JRadioButton getRdbtnFull() {
        if (rdbtnFull == null) {
            rdbtnFull = new JRadioButton(Messages.getString("SettingsPanel.rdbtnFullslow.text")); //$NON-NLS-1$
        }
        return rdbtnFull;
    }
    private JButton getBtnForgetIgnored() {
        if (btnForgetIgnored == null) {
            btnForgetIgnored = new JButton(Messages.getString("SettingsPanel.btnForget_1.text")); //$NON-NLS-1$
            btnForgetIgnored.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doForgetIgnored(e);
                }
            });
        }
        return btnForgetIgnored;
    }

    protected void doForgetIgnored(ActionEvent e) {
        try {
            Database.getInstance().clearIgnored();
        } catch (SQLException e1) {
            Settings.getLogger().error("", e1); //$NON-NLS-1$
        }
        setForgetButtonsEnabled();
    }

    private JPanel getPanel_2() {
        if (panel_2 == null) {
            panel_2 = new JPanel();
            panel_2.add(getBtnForget());
            panel_2.add(getBtnForgetIgnored());
        }
        return panel_2;
    }
    private JPanel getPanelSelectPlayer() {
        if (panelSelectPlayer == null) {
            panelSelectPlayer = new JPanel();
            FlowLayout fl_panelSelectPlayer = (FlowLayout) panelSelectPlayer.getLayout();
            fl_panelSelectPlayer.setAlignment(FlowLayout.LEFT);
            panelSelectPlayer.add(getLblMediaPlayer());

            ButtonGroup bg = new ButtonGroup();
            bg.add(getRdbtnUseVlc());
            bg.add(getRdbtnUseExternalPlayer());

            panelSelectPlayer.add(getRdbtnUseVlc());
            panelSelectPlayer.add(getRdbtnUseExternalPlayer());
        }
        return panelSelectPlayer;
    }
    private JRadioButton getRdbtnUseVlc() {
        if (rdbtnUseVlc == null) {
            rdbtnUseVlc = new JRadioButton(Messages.getString("SettingsPanel.0")); //$NON-NLS-1$
        }
        return rdbtnUseVlc;
    }
    private JRadioButton getRdbtnUseExternalPlayer() {
        if (rdbtnUseExternalPlayer == null) {
            rdbtnUseExternalPlayer = new JRadioButton(Messages.getString("SettingsPanel.1")); //$NON-NLS-1$
        }
        return rdbtnUseExternalPlayer;
    }
    private JLabel getLblMediaPlayer() {
        if (lblMediaPlayer == null) {
            lblMediaPlayer = new JLabel(Messages.getString("SettingsPanel.lblMediaPlayer.text")); //$NON-NLS-1$
        }
        return lblMediaPlayer;
    }
    private JPanel getPanelVLC() {
        if (panelVLC == null) {
            panelVLC = new JPanel();
            FlowLayout flowLayout = (FlowLayout) panelVLC.getLayout();
            flowLayout.setAlignment(FlowLayout.LEFT);
            panelVLC.add(getTextLibvlcLocation());
            panelVLC.add(getButtonLibvlc());
            panelVLC.add(getBtnLibvlcDetect());
        }
        return panelVLC;
    }
    private JLabel getLblLocationOfLibvlc() {
        if (lblLocationOfLibvlc == null) {
            lblLocationOfLibvlc = new JLabel(Messages.getString("SettingsPanel.lblLocationOfLibvlc.text")); //$NON-NLS-1$
        }
        return lblLocationOfLibvlc;
    }

    private JTextField getTextLibvlcLocation() {
        if (textLibvlcLocation == null) {
            textLibvlcLocation = new JTextField();
            textLibvlcLocation.setColumns(20);
        }
        return textLibvlcLocation;
    }

    private JButton getButtonLibvlc() {
        if (buttonLibvlc == null) {
            buttonLibvlc = new JButton(Messages.getString("SettingsPanel.button.text")); //$NON-NLS-1$
            buttonLibvlc.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doChooseLibVlc(e);
                }
            });
        }
        return buttonLibvlc;
    }

    protected void doChooseLibVlc(ActionEvent e) {
        JFileChooser fc = new JFileChooser();

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if(OsUtils.isWindows()){
            fc.addChoosableFileFilter(new FileFilter(){

                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().equals("libvlc.dll"); //$NON-NLS-1$
                }

                @Override
                public String getDescription() {
                    return Messages.getString("SettingsPanel.6"); //$NON-NLS-1$
                }

            });
        }
        else if(OsUtils.isMacOSX()){
            fc.addChoosableFileFilter(new FileFilter(){
                @Override
                public boolean accept(File f) {
                    // TODO: actual name
                    return f.isDirectory() || f.getName().toLowerCase().equals("libvlc.dll"); //$NON-NLS-1$
                }

                @Override
                public String getDescription() {
                    return Messages.getString("SettingsPanel.8"); //$NON-NLS-1$
                }
            });
        }
        else{
            // assume linux and other unices
            fc.addChoosableFileFilter(new FileFilter(){
                @Override
                public boolean accept(File f) {
                    // TODO: actual name
                    return f.isDirectory() || f.getName().toLowerCase().equals("libvlc.so"); //$NON-NLS-1$
                }

                @Override
                public String getDescription() {
                    return Messages.getString("SettingsPanel.10"); //$NON-NLS-1$
                }
            });
        }

        if (fc.showOpenDialog(this.getParent()) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            if(path.contains(" ")){ //$NON-NLS-1$
                path = "\"" + path + "\""; //$NON-NLS-1$ //$NON-NLS-2$
            }

            getTextLibvlcLocation().setText(path);
        }
    }

    private JButton getBtnLibvlcDetect() {
        if (btnLibvlcDetect == null) {
            btnLibvlcDetect = new JButton(Messages.getString("SettingsPanel.btnDetect.text")); //$NON-NLS-1$
            btnLibvlcDetect.setEnabled(false);
            btnLibvlcDetect.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doDetectVlc(e);
                }
            });
        }
        return btnLibvlcDetect;
    }

    protected void doDetectVlc(ActionEvent e) {
        // TODO: detect vlc lib

    }
}
