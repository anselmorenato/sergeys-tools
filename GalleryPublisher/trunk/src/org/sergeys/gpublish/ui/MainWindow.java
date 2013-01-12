package org.sergeys.gpublish.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker.StateValue;
import javax.swing.UIManager;

import org.sergeys.gpublish.logic.RenamerWorker;
import org.sergeys.gpublish.logic.Settings;

public class MainWindow implements ClipboardOwner {

    private JFrame frame;
    private JTextField textFieldSrcPostImagesDir;
    private JTextField textFieldSrcRawWpDir;
    private JTextPane textPaneHtml;
    private DisabledPanel disabledPanel;
    private JTextField textFieldDstWpDir;
    private JTextField textFieldPostImagesWebPrefix;
    private JTextField textFieldWpWebPrefix;

    /**
     * Create the application.
     */
    public MainWindow() {
        try {
            initialize();
        } catch (Exception ex) {
            Settings.getLogger().error("failed to init main window", ex);
        }
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        // looks like old name is used in 1.6 on macosx and this not works
        // UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            Settings.getLogger().error("failed to set lookandfeel", ex);
        }

        frame = new JFrame();
        frame.setTitle("Gallery Publisher");

        // see main()
        // frame.setBounds(
        // Settings.getInstance().getWinPosition().width,
        // Settings.getInstance().getWinPosition().height,
        // Settings.getInstance().getWinSize().width,
        // Settings.getInstance().getWinSize().height);

        frame.setBounds(0, 0, 500, 385); // for eclipse windowbuilder editor

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //
        // http://stackoverflow.com/questions/258099/how-to-close-a-java-swing-application-from-the-code

        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/images/BPCameraCherry.png")));

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doExit();
            }
        });

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doExit();
            }
        });
        mnFile.add(mntmExit);

        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        JMenuItem mntmAbout = new JMenuItem("About ...");
        mntmAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doAbout();
            }
        });
        mnHelp.add(mntmAbout);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel contentPanel = new JPanel();
        // frame.getContentPane().add(contentPanel, BorderLayout.EAST);

        disabledPanel = new DisabledPanel(contentPanel);

        JComponent glass = disabledPanel.getGlassPane();


        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0};
        gbl_contentPane.rowWeights = new double[]{1.0, 1.0};

        glass.setLayout(gbl_contentPane);

        JLabel lblProgress = new JLabel("");
        lblProgress.setIcon(new ImageIcon(getClass().getResource(
                "/images/progress.gif")));
        GridBagConstraints gbc_lblProgress = new GridBagConstraints();
        gbc_lblProgress.insets = new Insets(0, 0, 5, 5);
        gbc_lblProgress.gridx = 1;
        gbc_lblProgress.gridy = 0;
        gbc_lblProgress.anchor = GridBagConstraints.SOUTH;
        glass.add(lblProgress, gbc_lblProgress);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doCancelBackgroundWork();
            }
        });

        GridBagConstraints gbc_btnCancel = new GridBagConstraints();
        gbc_btnCancel.insets = new Insets(0, 0, 5, 5);
        gbc_btnCancel.gridx = 1;
        gbc_btnCancel.gridy = 1;
        gbc_btnCancel.anchor = GridBagConstraints.NORTH;
        glass.add(btnCancel, gbc_btnCancel);



        frame.getContentPane().add(disabledPanel, BorderLayout.CENTER);

        contentPanel.setLayout(new BorderLayout(0, 0));

        JPanel panelTop = new JPanel();
        contentPanel.add(panelTop, BorderLayout.NORTH);
        GridBagLayout gbl_panelTop = new GridBagLayout();
        gbl_panelTop.columnWidths = new int[] { 0, 0, 0 };
        // gbl_panelTop.rowHeights = new int[] {0, 0, 0};
        gbl_panelTop.columnWeights = new double[] { 0.5, 2.0, 1.0 };
        gbl_panelTop.rowWeights = new double[] { 1.0, 0.0, 1.0, 1.0, 1.0, 1.0 };
        panelTop.setLayout(gbl_panelTop);

        JLabel lblNewLabel1 = new JLabel("Source folder with post images:");
        GridBagConstraints gbc_lblNewLabel1 = new GridBagConstraints();
        gbc_lblNewLabel1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel1.fill = GridBagConstraints.VERTICAL;
        gbc_lblNewLabel1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel1.gridx = 0;
        gbc_lblNewLabel1.gridy = 0;
        panelTop.add(lblNewLabel1, gbc_lblNewLabel1);

        textFieldSrcPostImagesDir = new JTextField();
        textFieldSrcPostImagesDir.setEditable(false);
        GridBagConstraints gbc_textFieldSrcPostImagesDir = new GridBagConstraints();
        gbc_textFieldSrcPostImagesDir.fill = GridBagConstraints.BOTH;
        gbc_textFieldSrcPostImagesDir.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldSrcPostImagesDir.gridx = 1;
        gbc_textFieldSrcPostImagesDir.gridy = 0;
        panelTop.add(textFieldSrcPostImagesDir, gbc_textFieldSrcPostImagesDir);
        textFieldSrcPostImagesDir.setColumns(10);

        JButton btnSelectSrcPostImages = new JButton("...");
        btnSelectSrcPostImages.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSelectSrcImageDir();
            }
        });
        GridBagConstraints gbc_btnSelectSrcPostImages = new GridBagConstraints();
        gbc_btnSelectSrcPostImages.anchor = GridBagConstraints.WEST;
        gbc_btnSelectSrcPostImages.insets = new Insets(0, 0, 5, 0);
        gbc_btnSelectSrcPostImages.gridx = 2;
        gbc_btnSelectSrcPostImages.gridy = 0;
        panelTop.add(btnSelectSrcPostImages, gbc_btnSelectSrcPostImages);

                                JLabel lblWebDir = new JLabel("Source folder with raw wallpaper subfolders:");
                                GridBagConstraints gbc_lblWebDir = new GridBagConstraints();
                                gbc_lblWebDir.anchor = GridBagConstraints.EAST;
                                gbc_lblWebDir.insets = new Insets(0, 0, 5, 5);
                                gbc_lblWebDir.fill = GridBagConstraints.VERTICAL;
                                gbc_lblWebDir.gridx = 0;
                                gbc_lblWebDir.gridy = 1;
                                panelTop.add(lblWebDir, gbc_lblWebDir);

                        textFieldSrcRawWpDir = new JTextField();
                        textFieldSrcRawWpDir.setEditable(false);
                        GridBagConstraints gbc_textFieldSrcRawWpDir = new GridBagConstraints();
                        gbc_textFieldSrcRawWpDir.insets = new Insets(0, 0, 5, 5);
                        gbc_textFieldSrcRawWpDir.fill = GridBagConstraints.HORIZONTAL;
                        gbc_textFieldSrcRawWpDir.gridx = 1;
                        gbc_textFieldSrcRawWpDir.gridy = 1;
                        panelTop.add(textFieldSrcRawWpDir, gbc_textFieldSrcRawWpDir);
                        textFieldSrcRawWpDir.setColumns(10);


                JButton btnSelectSrcWallpapers = new JButton("...");
                btnSelectSrcWallpapers.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doSelectSrcWallpapers();
                    }
                });
                GridBagConstraints gbc_btnSelectSrcWallpapers = new GridBagConstraints();
                gbc_btnSelectSrcWallpapers.anchor = GridBagConstraints.WEST;
                gbc_btnSelectSrcWallpapers.insets = new Insets(0, 0, 5, 0);
                gbc_btnSelectSrcWallpapers.gridx = 2;
                gbc_btnSelectSrcWallpapers.gridy = 1;
                panelTop.add(btnSelectSrcWallpapers, gbc_btnSelectSrcWallpapers);

        JLabel lblNewLabel = new JLabel("Target folder for renamed wallpapers:");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 2;
        panelTop.add(lblNewLabel, gbc_lblNewLabel);

        textFieldDstWpDir = new JTextField();
        textFieldDstWpDir.setEditable(false);
        GridBagConstraints gbc_textFieldDstWpDir = new GridBagConstraints();
        gbc_textFieldDstWpDir.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldDstWpDir.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldDstWpDir.gridx = 1;
        gbc_textFieldDstWpDir.gridy = 2;
        panelTop.add(textFieldDstWpDir, gbc_textFieldDstWpDir);
        textFieldDstWpDir.setColumns(10);

        JButton btnSelectDstWallpapers = new JButton("...");
        btnSelectDstWallpapers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSelectDstWallpapers();
            }
        });
        GridBagConstraints gbc_btnSelectDstWallpapers = new GridBagConstraints();
        gbc_btnSelectDstWallpapers.anchor = GridBagConstraints.WEST;
        gbc_btnSelectDstWallpapers.insets = new Insets(0, 0, 5, 0);
        gbc_btnSelectDstWallpapers.gridx = 2;
        gbc_btnSelectDstWallpapers.gridy = 2;
        panelTop.add(btnSelectDstWallpapers, gbc_btnSelectDstWallpapers);

        JLabel lblNewLabel_1 = new JLabel("Web prefix for post images:");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 3;
        panelTop.add(lblNewLabel_1, gbc_lblNewLabel_1);

        textFieldPostImagesWebPrefix = new JTextField();
        GridBagConstraints gbc_textFieldPostImagesWebPrefix = new GridBagConstraints();
        gbc_textFieldPostImagesWebPrefix.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldPostImagesWebPrefix.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldPostImagesWebPrefix.gridx = 1;
        gbc_textFieldPostImagesWebPrefix.gridy = 3;
        panelTop.add(textFieldPostImagesWebPrefix, gbc_textFieldPostImagesWebPrefix);
        textFieldPostImagesWebPrefix.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Web prefix for wallpapers:");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 0;
        gbc_lblNewLabel_2.gridy = 4;
        panelTop.add(lblNewLabel_2, gbc_lblNewLabel_2);

        textFieldWpWebPrefix = new JTextField();
        GridBagConstraints gbc_textFieldWpWebPrefix = new GridBagConstraints();
        gbc_textFieldWpWebPrefix.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldWpWebPrefix.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldWpWebPrefix.gridx = 1;
        gbc_textFieldWpWebPrefix.gridy = 4;
        panelTop.add(textFieldWpWebPrefix, gbc_textFieldWpWebPrefix);
        textFieldWpWebPrefix.setColumns(10);

                JPanel panel = new JPanel();
                FlowLayout flowLayout = (FlowLayout) panel.getLayout();
                flowLayout.setAlignment(FlowLayout.LEFT);
                GridBagConstraints gbc_panel = new GridBagConstraints();
                gbc_panel.insets = new Insets(0, 0, 5, 5);
                gbc_panel.fill = GridBagConstraints.BOTH;
                gbc_panel.gridx = 1;
                gbc_panel.gridy = 5;
                panelTop.add(panel, gbc_panel);

                                chckbxDeleteRawWallpapers = new JCheckBox("Delete raw wallpapers");
                                chckbxDeleteRawWallpapers.addItemListener(new ItemListener() {
                                    public void itemStateChanged(ItemEvent e) {
                                        doCheckboxDelete(e.getStateChange() == ItemEvent.SELECTED);
                                    }
                                });

                                panel.add(chckbxDeleteRawWallpapers);



                                JButton btnGenerateHtml = new JButton("Generate HTML");
                                panel.add(btnGenerateHtml);
                                btnGenerateHtml.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent arg0) {
                                        doGenerateHtml();
                                    }
                                });

        JPanel panelCenter = new JPanel();
        contentPanel.add(panelCenter, BorderLayout.CENTER);
        panelCenter.setLayout(new BorderLayout(0, 0));



        textPaneHtml = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPaneHtml);
        panelCenter.add(scrollPane);

        JPanel panelBottom = new JPanel();
        contentPanel.add(panelBottom, BorderLayout.SOUTH);

        JButton btnClipboard = new JButton("Copy to clipboard");
        btnClipboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doCopyToClipboard();
            }
        });

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doClear();
            }
        });
        panelBottom.add(btnClear);
        panelBottom.add(btnClipboard);

        JButton btnViewLog = new JButton("View log");
        btnViewLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doViewLog();
            }
        });
        panelBottom.add(btnViewLog);

        setInitialValues();
    }

    private void setInitialValues(){
        textFieldSrcPostImagesDir.setText(Settings.getInstance().getSrcPostImagesDir());
        textFieldSrcRawWpDir.setText(Settings.getInstance().getSrcWallpapersDir());
        textFieldDstWpDir.setText(Settings.getInstance().getDstWallpapersDir());
        textFieldPostImagesWebPrefix.setText(Settings.getInstance().getWebPrefixPostImages());
        textFieldWpWebPrefix.setText(Settings.getInstance().getWebPrefixWallpapers());
        chckbxDeleteRawWallpapers.setSelected(Settings.getInstance().isDeleteRawWallpapers());
    }

    private void saveCurrentValues(){
        Settings.getInstance().setWinPosition(new Dimension(frame.getX(), frame.getY()));
        Settings.getInstance().setWinSize(new Dimension(frame.getWidth(), frame.getHeight()));

        Settings.getInstance().setSrcPostImagesDir(textFieldSrcPostImagesDir.getText());
        Settings.getInstance().setSrcWallpapersDir(textFieldSrcRawWpDir.getText());
        Settings.getInstance().setDstWallpapersDir(textFieldDstWpDir.getText());
        Settings.getInstance().setWebPrefixPostImages(textFieldPostImagesWebPrefix.getText());
        Settings.getInstance().setWebPrefixWallpapers(textFieldWpWebPrefix.getText());

        Settings.getInstance().setDeleteRawWallpapers(chckbxDeleteRawWallpapers.isSelected());

        try {
            Settings.save();
        } catch (FileNotFoundException e) {
            Settings.getLogger().error("failed to save settings", e);
        }
    }

    private DirSelectorDialog dirSelector;

    private enum DirectoryType { PostImages, SourceWallpapers, TargetWallpapers };

    private void selectDirHelper(final DirectoryType dirType){
        if (dirSelector == null) {
            dirSelector = new DirSelectorDialog(frame);
            dirSelector.setLocationRelativeTo(frame);
        }

        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(DirSelectorDialog.DIRECTORY_SELECTED)) {

                    String path = evt.getNewValue().toString();
                    switch(dirType){
                    case PostImages:
                        textFieldSrcPostImagesDir.setText(path);
                        Settings.getInstance().setSrcPostImagesDir(textFieldSrcPostImagesDir.getText());
                        break;
                    case SourceWallpapers:
                        textFieldSrcRawWpDir.setText(path);
                        Settings.getInstance().setSrcWallpapersDir(textFieldSrcRawWpDir.getText());
                        break;
                    case TargetWallpapers:
                        textFieldDstWpDir.setText(path);
                        Settings.getInstance().setDstWallpapersDir(textFieldDstWpDir.getText());
                        break;
                    }
                }
            }
        };

        dirSelector.addPropertyChangeListener(DirSelectorDialog.DIRECTORY_SELECTED, listener);
        dirSelector.setVisible(true);	// waits for closing
        dirSelector.removePropertyChangeListener(DirSelectorDialog.DIRECTORY_SELECTED, listener);
    }

    protected void doSelectSrcImageDir() {
        selectDirHelper(DirectoryType.PostImages);
    }

    protected void doSelectDstWallpapers() {
        selectDirHelper(DirectoryType.TargetWallpapers);
    }

    protected void doSelectSrcWallpapers() {
        selectDirHelper(DirectoryType.SourceWallpapers);
    }

    protected void doCheckboxDelete(boolean isSelected) {
        Settings.getInstance().setDeleteRawWallpapers(isSelected);
    }

    private LogfileDialog logWindow;

    protected void doViewLog() {

        if(logWindow == null){
            logWindow = new LogfileDialog(frame);
            //logWindow.setLocationRelativeTo(frame);
        }

        logWindow.setVisible(true);
    }

    protected void doClear() {
        textPaneHtml.setText("");
    }

    protected void doCopyToClipboard() {
        StringSelection stringSelection = new StringSelection(textPaneHtml.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    private RenamerWorker worker;

    protected void doGenerateHtml() {

        // update settings, dir selectors already ipdated
        Settings.getInstance().setWebPrefixPostImages(textFieldPostImagesWebPrefix.getText());
        Settings.getInstance().setWebPrefixWallpapers(textFieldWpWebPrefix.getText());

        worker = new RenamerWorker(this);
        worker.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if("state".equals(evt.getPropertyName())){
                    if((StateValue)evt.getNewValue() == StateValue.DONE){
                        Settings.getLogger().debug("worker done");
                        doWorkerDone();
                    }
                    //Settings.getLogger().debug("worker state: " + evt.getNewValue().toString());
                }
            }
        });

        disabledPanel.setEnabled(false);
        worker.execute();
    }

    protected void doWorkerDone() {
        // just reenables panel. Textarea is updated from worker class.
        disabledPanel.setEnabled(true);
    }

    protected void doCancelBackgroundWork() {
        worker.cancel(false);
        disabledPanel.setEnabled(true);
    }

    private AboutDialog aboutDlg;
    private JCheckBox chckbxDeleteRawWallpapers;

    protected void doAbout() {
        if(aboutDlg == null){
            aboutDlg = new AboutDialog(frame);
            aboutDlg.setLocationRelativeTo(frame);
        }

        aboutDlg.setVisible(true);
    }

    protected void doExit() {
        Settings.getLogger().debug("application exit");

        saveCurrentValues();

        frame.setVisible(false);
        frame.dispose();
        System.exit(0);
    }

    public JFrame getFrame() {
        return frame;
    }

    @Override
    public void lostOwnership(Clipboard arg0, Transferable arg1) {
        // do nothing
    }

    public JTextPane getTextPaneHtml() {
        return textPaneHtml;
    }

}
