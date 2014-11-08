package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.xml.stream.XMLStreamException;

import org.sergeys.library.rss.Feed;
import org.sergeys.library.rss.RssFeedParser;
import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.Database;
import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;
import org.sergeys.webcachedigger.ui.ProgressDialog.WorkType;

public class WebCacheDigger
implements ActionListener, PropertyChangeListener
{

    private JFrame jFrame = null;
    private JPanel jContentPane = null;
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu = null;
    private JMenu settingsMenu = null;
    private JMenu helpMenu = null;
    private JMenuItem exitMenuItem = null;
    private JMenuItem aboutMenuItem = null;
    private JDialog aboutDialog = null;
    private JPanel jPanelFoundFiles = null;
    private JPanel jPanelFoundFilesActions = null;
    private JButton jButtonCopySelectedFiles = null;
    private JSplitPane jSplitPaneMain = null;
    private JPanel jPanelTop = null;
    private JButton jButtonSearch = null;
    private FilesListPanel filesListPanel = null;
    private FileDetailsPanel jPanelFileDetails = null;
    private JMenuItem jMenuItemSettings = null;

//	private Settings settings;

    /**
     * This method initializes jPanelFoundFiles
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelFoundFiles() {
        if (jPanelFoundFiles == null) {
            jPanelFoundFiles = new JPanel();
            jPanelFoundFiles.setLayout(new BorderLayout());
            jPanelFoundFiles.add(getJPanelFoundFilesActions(), BorderLayout.SOUTH);
            jPanelFoundFiles.add(getFilesListPanel(), BorderLayout.CENTER);
        }
        return jPanelFoundFiles;
    }

    JButton bthIgnoreSelectedFiles;

    private JPanel getJPanelFoundFilesActions() {
        if (jPanelFoundFilesActions == null) {
            jPanelFoundFilesActions = new JPanel();
            jPanelFoundFilesActions.setLayout(new FlowLayout());


            bthIgnoreSelectedFiles = new JButton(Messages.getString("WebCacheDigger.IgnoreCheckedFiles")); //$NON-NLS-1$
            bthIgnoreSelectedFiles.setEnabled(false);
            bthIgnoreSelectedFiles.setIcon(new ImageIcon(WebCacheDigger.class.getResource("/images/fileclose.png"))); //$NON-NLS-1$
            bthIgnoreSelectedFiles.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    doIgnoreSelectedFiles(e);
                }});

            jPanelFoundFilesActions.add(bthIgnoreSelectedFiles, null);

            jPanelFoundFilesActions.add(getJButtonCopySelectedFiles(), null);
        }
        return jPanelFoundFilesActions;
    }

    protected void doIgnoreSelectedFiles(ActionEvent e) {
        Hashtable<String, CachedFile> markAsIgnored = new Hashtable<String, CachedFile>();
        ArrayList<CachedFile> ignoredList = new ArrayList<CachedFile>();

        for(CachedFile file: getFilesListPanel().getCachedFiles()){
            if(file.isSelectedToCopy()){
                if(!markAsIgnored.containsKey(file.getHash())){
                    markAsIgnored.put(file.getHash(), file);
                }

                ignoredList.add(file);
            }
        }

        if(!ignoredList.isEmpty()){
            try {
                Database.getInstance().updateIgnored(markAsIgnored.values());
            } catch (SQLException e1) {
                Settings.getLogger().error("", e1); //$NON-NLS-1$
            } catch (Exception e1) {
                Settings.getLogger().error("", e1); //$NON-NLS-1$
            }

            List<CachedFile> current = getFilesListPanel().getCachedFiles();
            current.removeAll(ignoredList);
            getFilesListPanel().filesListChanged();
        }
    }

    /**
     * This method initializes jButtonCopySelectedFiles
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonCopySelectedFiles() {
        if (jButtonCopySelectedFiles == null) {
            jButtonCopySelectedFiles = new JButton();
            jButtonCopySelectedFiles.setIcon(new ImageIcon(WebCacheDigger.class.getResource("/images/lc_open.png"))); //$NON-NLS-1$
            jButtonCopySelectedFiles.setText(Messages.getString("WebCacheDigger.CopyCheckedFiles")); //$NON-NLS-1$
            jButtonCopySelectedFiles.setEnabled(false);
            jButtonCopySelectedFiles.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    doCopySelectedFiles(e);
                }
            });
        }
        return jButtonCopySelectedFiles;
    }

    /**
     * This method initializes jSplitPaneMain
     *
     * @return javax.swing.JSplitPane
     */
    protected JSplitPane getJSplitPaneMain() {
        if (jSplitPaneMain == null) {
            jSplitPaneMain = new JSplitPane();
            jSplitPaneMain.setPreferredSize(new Dimension(564, 350));
            jSplitPaneMain.setRightComponent(getJPanelFileDetails());
            jSplitPaneMain.setLeftComponent(getJPanelFoundFiles());

            getFilesListPanel().addPropertyChangeListener(CachedFile.SELECTED_FILE, (PropertyChangeListener)getJPanelFileDetails());
        }
        return jSplitPaneMain;
    }

    /**
     * This method initializes jPanelTop
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelTop() {
        if (jPanelTop == null) {

            jPanelTop = new JPanel();
            jPanelTop.setLayout(new FlowLayout());
            jPanelTop.setBorder(new EmptyBorder(0, 20, 0, 20));

            try {
                jPanelTop.add(new MainWinTopPanel(this));
            } catch (IOException e) {
                Settings.getLogger().error("", e); //$NON-NLS-1$
            }

            jPanelTop.add(getJButtonSearch(), null);


        }
        return jPanelTop;
    }

    /**
     * This method initializes jButtonSearch
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonSearch() {
        if (jButtonSearch == null) {
            jButtonSearch = new JButton();
            jButtonSearch.setIcon(new ImageIcon(WebCacheDigger.class.getResource("/images/lc_zoompage.png"))); //$NON-NLS-1$
            jButtonSearch.setText(Messages.getString("WebCacheDigger.Search")); //$NON-NLS-1$
            jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    WebCacheDigger.this.doSearchFiles();
                }
            });
        }
        return jButtonSearch;
    }

    /**
     * This method initializes filesListPanel
     *
     * @return org.sergeys.webcachedigger.ui.FilesListPanel
     */
    private FilesListPanel getFilesListPanel() {
        if (filesListPanel == null) {
            filesListPanel = new FilesListPanel();
        }
        return filesListPanel;
    }

    /**
     * This method initializes jPanelFileDetails
     *
     * @return javax.swing.JPanel
     */
    private FileDetailsPanel getJPanelFileDetails() {
        if (jPanelFileDetails == null) {
            jPanelFileDetails = new FileDetailsPanel(this);
        }
        return jPanelFileDetails;
    }

    /**
     * This method initializes jMenuItemSettings
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getJMenuItemSettings() {
        if (jMenuItemSettings == null) {
            jMenuItemSettings = new JMenuItem();
            jMenuItemSettings.setText(Messages.getString("WebCacheDigger.Settings") + " ...");  //$NON-NLS-1$ //$NON-NLS-2$
            jMenuItemSettings.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    WebCacheDigger.this.editSettings(false);
                }
            });
        }
        return jMenuItemSettings;
    }

    private static File lockfile;
    private static FileChannel lockchannel;
    private static FileLock applock;

    public static void unlockFile() {
        // release and delete file lock
        try {
            if(applock != null) {
                applock.release();
                lockchannel.close();
                //lockfile.delete();
            }
        } catch(IOException e) {
            Settings.getLogger().error("", e); //$NON-NLS-1$
        }
    }

    static class ShutdownHook extends Thread {
        public void run() {
            unlockFile();
        }
    }

    /**
     * @param args
     * @return
     */
    public static void main(String[] args) {

        Settings.getLogger().info("application started"); //$NON-NLS-1$
        Settings.getLogger().info(Calendar.getInstance().getTime().toString());
        Settings.getLogger().info("version: " + Settings.getInstance().getVersionDisplay());

        // check if there is another instance running
        // http://jimlife.wordpress.com/2008/07/21/java-application-make-sure-only-singleone-instance-running-with-file-lock-ampampampampamp-shutdownhook/
        File dir = new File(Settings.getSettingsDirPath());
        if(!dir.exists()){
            dir.mkdirs();
        }
        String lockfilename = Settings.getSettingsDirPath() + File.separator + Settings.LOCK_FILE;
        lockfile = new File(lockfilename);

//    	if(lockfile.exists()){
//    		lockfile.delete();
//    	}

        // Try to get the lock
        try {
            lockchannel = new RandomAccessFile(lockfile, "rw").getChannel(); //$NON-NLS-1$
            applock = lockchannel.tryLock();
            if(applock == null)
            {
                // File is locked by other application
                lockchannel.close();
                //throw new RuntimeException("Only 1 instance of MyApp can run.");
                JOptionPane.showMessageDialog(null, Messages.getString("WebCacheDigger.0")); //$NON-NLS-1$
                System.exit(1);
            }
        } catch (FileNotFoundException ex) {
            Settings.getLogger().error("", ex); //$NON-NLS-1$
            JOptionPane.showMessageDialog(null, "Cannot access lockfile " + lockfilename + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
            System.exit(1);
        } catch (IOException ex) {
            Settings.getLogger().error("", ex); //$NON-NLS-1$
            JOptionPane.showMessageDialog(null, "Failed to get lock on lockfile " + lockfilename + "\n" + ex.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
            System.exit(1);
        }

        // Add shutdown hook to release lock when application shutdown
        ShutdownHook shutdownHook = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                try {

                    String laf = Settings.getInstance().getLookAndFeel();
                    if(laf == null || laf.equals("default") || laf.isEmpty()) //$NON-NLS-1$
                    {
                        // looks like old name is used in 1.6 on macosx and this not works
                        //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); //$NON-NLS-1$
                    }
                    else{
                        UIManager.setLookAndFeel(laf);
                    }

                    //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); //$NON-NLS-1$
                } catch (ClassNotFoundException e) {
                    Settings.getLogger().error("", e); //$NON-NLS-1$
                } catch (InstantiationException e) {
                    Settings.getLogger().error("", e); //$NON-NLS-1$
                } catch (IllegalAccessException e) {
                    Settings.getLogger().error("", e); //$NON-NLS-1$
                } catch (UnsupportedLookAndFeelException e) {
                    Settings.getLogger().error("", e); //$NON-NLS-1$
                }

                final WebCacheDigger application = new WebCacheDigger();

                Locale l = new Locale(Settings.getInstance().getLanguage());
                Locale.setDefault(l);

                JFrame mainWindow = application.getJFrame();

                mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                mainWindow.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e) {
                        //super.windowClosing(e);
                        application.doExit();
                    }
                });

                // set size and position of main window
                Dimension desktop = Toolkit.getDefaultToolkit().getScreenSize();

                int x = (desktop.width - mainWindow.getWidth())/2;
                int y = (desktop.height - mainWindow.getHeight())/2;
                int w = mainWindow.getWidth();
                int h = mainWindow.getHeight();
                int divpos = application.getJSplitPaneMain().getDividerLocation();
                x = Settings.getInstance().getIntProperty(Settings.WINDOW_X, x);
                y = Settings.getInstance().getIntProperty(Settings.WINDOW_Y, y);
                w = Settings.getInstance().getIntProperty(Settings.WINDOW_W, w);
                h = Settings.getInstance().getIntProperty(Settings.WINDOW_H, h);
                divpos = Settings.getInstance().getIntProperty(Settings.SPLITTER_POS, divpos);

                mainWindow.setLocation(x, y);
                mainWindow.setSize(w, h);
                application.getJSplitPaneMain().setDividerLocation(divpos);
                mainWindow.setVisible(true);

                if(Settings.getInstance().isFirstRun()){
                    application.editSettings(true);
                }
                else{
                    // show what's changed
                    String resName = "/resources/changes_" + Locale.getDefault().getLanguage() + ".xml";					 //$NON-NLS-1$ //$NON-NLS-2$
                    InputStream is = getClass().getResourceAsStream(resName);
                    if(is == null){
                        is = getClass().getResourceAsStream("/resources/changes.xml");						 //$NON-NLS-1$
                    }

                    if(is != null){
                        //RssFeedParser parser = new RssFeedParser(is, "EEE, d MMM yyyy HH:mm:ss Z");
                        RssFeedParser parser = new RssFeedParser(is, "d MMM yyyy HH:mm"); //$NON-NLS-1$
                        Feed changes = null;
                        try {
                            changes = parser.readFeed();
                        } catch (XMLStreamException e1) {
                            Settings.getLogger().error("failed to read news feed", e1); //$NON-NLS-1$
                        } catch (ParseException e1) {
                            Settings.getLogger().error("failed to read news feed", e1); //$NON-NLS-1$
                        }

//						for(FeedMessage msg: changes.getMessages()){
//							System.out.println(msg.getPubDate() + " " + msg.getDescription());
//						}

                        if(changes != null && changes.getPubDate().after(Settings.getInstance().getSavedVersionTimestamp())){
                            ChangesDialog chdlg = new ChangesDialog(mainWindow);
                            chdlg.setLocationRelativeTo(mainWindow);
                            chdlg.createText(changes);
                            chdlg.setModal(false);
                            chdlg.setVisible(true);
                        }

                    }
                }

                DatabaseCleanerWorker dbcleaner = new DatabaseCleanerWorker();
                dbcleaner.execute();
            }
        });

    }

    /**
     * This method initializes jFrame
     *
     * @return javax.swing.JFrame
     */
    protected JFrame getJFrame() {
        if (jFrame == null) {
            jFrame = new JFrame();
            jFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(WebCacheDigger.class.getResource("/images/icon.png"))); //$NON-NLS-1$
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setPreferredSize(new Dimension(750, 400));
            jFrame.setJMenuBar(getJJMenuBar());
            jFrame.setSize(750, 400);
            jFrame.setContentPane(getJContentPane());
            jFrame.setTitle("Web Cache Digger"); //$NON-NLS-1$
        }
        return jFrame;
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    protected JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            BorderLayout bl_jContentPane = new BorderLayout();
            jContentPane.setLayout(bl_jContentPane);
            jContentPane.add(getJSplitPaneMain(), BorderLayout.CENTER);
            jContentPane.add(getJPanelTop(), BorderLayout.NORTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jJMenuBar
     *
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getSettingsMenu());
            jJMenuBar.add(getHelpMenu());
        }
        return jJMenuBar;
    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setText(Messages.getString("WebCacheDigger.File")); //$NON-NLS-1$
            fileMenu.add(getExitMenuItem());
        }
        return fileMenu;
    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getSettingsMenu() {
        if (settingsMenu == null) {
            settingsMenu = new JMenu();
            settingsMenu.setText(Messages.getString("WebCacheDigger.Settings")); //$NON-NLS-1$
            settingsMenu.add(getMnLanguage());
            settingsMenu.add(getMnLookAndFeel());
            settingsMenu.add(getJMenuItemSettings());
        }
        return settingsMenu;
    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new JMenu();
            helpMenu.setText(Messages.getString("WebCacheDigger.Help")); //$NON-NLS-1$
            helpMenu.add(getMntmLog());
            helpMenu.add(getSeparator());
            helpMenu.add(getAboutMenuItem());
        }
        return helpMenu;
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setText(Messages.getString("WebCacheDigger.Exit")); //$NON-NLS-1$
            exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    WebCacheDigger.this.doExit();
                }
            });
        }
        return exitMenuItem;
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setText(Messages.getString("WebCacheDigger.About") + " ...");  //$NON-NLS-1$ //$NON-NLS-2$
            aboutMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JDialog aboutDialog = getAboutDialog();
                    aboutDialog.setLocationRelativeTo(getJContentPane());
                    aboutDialog.setVisible(true);
                }
            });
        }
        return aboutMenuItem;
    }

    protected JDialog getAboutDialog() {
        if (aboutDialog == null) {
            aboutDialog = new AboutDialog(this.getJFrame());

        }
        return aboutDialog;
    }

    ProgressDialog progressDialog;

    protected void doSearchFiles(){

        try {

            if(progressDialog == null){
                progressDialog = new ProgressDialog(getExistingBrowsers());
                progressDialog.addPropertyChangeListener(this);
            }

            progressDialog.setWorkType(WorkType.CollectFiles);

            getFilesListPanel().setEnabled(false);
            getJPanelFileDetails().setFile(null);
            getJPanelFileDetails().setEnabled(false);

            progressDialog.setLocationRelativeTo(getJContentPane());
            progressDialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(getJFrame(),
                    String.format(Messages.getString("WebCacheDigger.FailedToCollectFiles"), e.getMessage()), //$NON-NLS-1$
                    Messages.getString("WebCacheDigger.Error"),  //$NON-NLS-1$
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateCachedFiles(ArrayList<CachedFile> files){

        getFilesListPanel().setEnabled(true);
        getFilesListPanel().init(files);
        progressDialog.setVisible(false);
    }

    private SettingsDialog settingsDialog;
    private SettingsDialog getSettingsDialog(){
        if(settingsDialog == null){
            settingsDialog = new SettingsDialog(this.getJFrame());
            settingsDialog.addSaveActionListener(this);
        }
        settingsDialog.setLocationRelativeTo(getJContentPane());
        return settingsDialog;
    }

    private void editSettings(boolean firstTime){

        if(firstTime){
            JOptionPane.showMessageDialog(getJContentPane(), Messages.getString("WebCacheDigger.PleaseChooseDestFolder")); //$NON-NLS-1$
        }

        SettingsDialog dlg = getSettingsDialog();

        dlg.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(Settings.COMMAND_SAVE_SETTINGS)){

            try {
                getSettingsDialog().updateSettings();
                Settings.save();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(getJFrame(),
                        String.format(Messages.getString("WebCacheDigger.FailedToSaveSettings"), Settings.getSettingsFilePath(), e1.getMessage()),  //$NON-NLS-1$
                        "",  //$NON-NLS-1$
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void doCopySelectedFiles(ActionEvent e){

        String targetDir = Settings.getInstance().getSaveToPath();


        int copied = 0;

        try {

            if(progressDialog == null){
                progressDialog = new ProgressDialog(getExistingBrowsers());
                progressDialog.addPropertyChangeListener(this);
            }

            progressDialog.setWorkType(WorkType.CopyFiles);
            progressDialog.setFilesToCopy(getFilesListPanel().getCachedFiles());
            progressDialog.setTargetDir(targetDir);

            progressDialog.setLocationRelativeTo(getJContentPane());
            progressDialog.setVisible(true);


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(getJFrame(),
                    String.format(Messages.getString("WebCacheDigger.FailedToCopyFiles"), ex.getMessage()), //$NON-NLS-1$
                    Messages.getString("WebCacheDigger.Error"),   //$NON-NLS-1$
                    JOptionPane.ERROR_MESSAGE);
        }

        if(copied > 0){
            String msg = String.format(Messages.getString("WebCacheDigger.CopiedFilesTo"), copied, targetDir); //$NON-NLS-1$

            JOptionPane.showMessageDialog(getJFrame(),
                    msg,
                    Messages.getString("WebCacheDigger.Message"),  //$NON-NLS-1$
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    protected void doExit(){
        // save window position
        JFrame mainWindow = getJFrame();
        try {
            Settings.getInstance().setIntProperty(Settings.WINDOW_X, mainWindow.getX());
            Settings.getInstance().setIntProperty(Settings.WINDOW_Y, mainWindow.getY());
            Settings.getInstance().setIntProperty(Settings.WINDOW_W, mainWindow.getWidth());
            Settings.getInstance().setIntProperty(Settings.WINDOW_H, mainWindow.getHeight());
            Settings.getInstance().setIntProperty(Settings.SPLITTER_POS, getJSplitPaneMain().getDividerLocation());

            Settings.save();
        } catch (IOException e) {
            Settings.getLogger().error("", e); //$NON-NLS-1$
        }

        mainWindow.setVisible(false);
        mainWindow.dispose();

        Settings.getLogger().info("application exit"); //$NON-NLS-1$
        System.exit(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(ProgressDialog.SEARCH_COMPLETE)){
            progressDialog.setVisible(false);

            if(evt.getNewValue() != null){
                List<CachedFile> files = (List<CachedFile>) evt.getNewValue();

                getFilesListPanel().setEnabled(true);
                getFilesListPanel().init(files);

                if(files.size() == 0){
                    JOptionPane.showMessageDialog(getJFrame(),
                            Messages.getString("WebCacheDigger.NothingFound"), //$NON-NLS-1$
                            "",   //$NON-NLS-1$
                            JOptionPane.INFORMATION_MESSAGE);

                }

                getJButtonCopySelectedFiles().setEnabled(files.size() > 0);
                bthIgnoreSelectedFiles.setEnabled(Settings.getInstance().isExcludeSavedAndIgnored() && (files.size() > 0));
            }
        }
        else if(evt.getPropertyName().equals(ProgressDialog.COPY_COMPLETE)){
            progressDialog.setVisible(false);

            List<CachedFile> copied = (List<CachedFile>) evt.getNewValue();

            if(copied.size() > 0){
                List<CachedFile> current = getFilesListPanel().getCachedFiles();
                current.removeAll(copied);
                getFilesListPanel().filesListChanged();

                String msg = String.format(Messages.getString("WebCacheDigger.CopiedFilesTo"), copied.size(), Settings.getInstance().getSaveToPath()); //$NON-NLS-1$

                JOptionPane.showMessageDialog(getJFrame(),
                        msg,
                        Messages.getString("WebCacheDigger.Message"),  //$NON-NLS-1$
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if(evt.getPropertyName().equals(AudioPreviewPanel.PROPERTY_FILE_TO_PLAY)
                || evt.getPropertyName().equals(VideoPreviewPanel.PROPERTY_FILE_TO_PLAY)){

            CachedFile f = (CachedFile)evt.getNewValue();

            doExternalPlayer(f);
        }
    }

    private void doExternalPlayer(final CachedFile f) {

        if(Settings.getInstance().isExternalPlayerConfigured()){
            SwingWorker<Void, Void> w = new SwingWorker<Void, Void>(){

                @Override
                protected Void doInBackground() throws Exception {
                    final String cmdLine = Settings.getInstance().getExternalPlayerCommand();


                    // http://www.regexplanet.com/simple/index.html
                    Pattern p = Pattern.compile("\"[^\"]+\"|[^\"\\s]+");					 //$NON-NLS-1$
                    Matcher m = p.matcher(cmdLine);
                    ArrayList<String> tokens = new ArrayList<String>();
                    while(m.find()){
                        tokens.add(m.group());
                    }

                    if(tokens.size() == 0){
                        SwingUtilities.invokeLater(new Runnable(){

                            @Override
                            public void run() {
                                JOptionPane.showMessageDialog(WebCacheDigger.this.getJContentPane(),
                                        String.format(Messages.getString("WebCacheDigger.FailedParseCmdLine"), cmdLine) //$NON-NLS-1$
                                        );
                            }});

                        return null;
                    }

                    String[] args = new String[tokens.size()];
                    for(int i = 0; i<tokens.size(); i++){
                        if(tokens.get(i).startsWith("\"")){   //$NON-NLS-1$
                            args[i] = tokens.get(i).substring(1, tokens.get(i).length() - 1);
                        }
                        else if(tokens.get(i).equals(Settings.EXT_PLAYER_FILEPATH)){
                            args[i] = f.getAbsolutePath();
                        }
                        else{
                            args[i] = tokens.get(i);
                        }
                    }

                    try{
                        Process process = Runtime.getRuntime().exec(args);

                        final int exitCode = process.waitFor();
                        if(exitCode != 0){
                            SwingUtilities.invokeLater(new Runnable(){

                                @Override
                                public void run() {
                                    JOptionPane.showMessageDialog(WebCacheDigger.this.getJContentPane(),
                                            String.format(Messages.getString("WebCacheDigger.ExtPlayerFailed"), exitCode)); //$NON-NLS-1$

                                }});
                        }
                    }
                    catch(final Exception ex){
                        SwingUtilities.invokeLater(new Runnable(){

                            @Override
                            public void run() {
                                JOptionPane.showMessageDialog(WebCacheDigger.this.getJContentPane(),
                                        String.format(Messages.getString("WebCacheDigger.ExtPlayerFailed1"), ex.getMessage())); //$NON-NLS-1$

                            }});

                        if(ex instanceof RuntimeException){
                            throw ex;
                        }
                    }

                    return null;
                }

            };

            w.execute();
        }

    }

    private LinkedHashSet<IBrowser> existingBrowsers = null;
    private JMenu mnLanguage;
    @SuppressWarnings("unused")
    private JMenuItem mntmEnglish;
    private JMenu mnLookAndFeel;
    private JMenuItem mntmLog;

    public synchronized LinkedHashSet<IBrowser> getExistingBrowsers(){
        if(existingBrowsers == null){
            existingBrowsers = new LinkedHashSet<IBrowser>();

            ServiceLoader<IBrowser> ldr = ServiceLoader.load(IBrowser.class);
            for(IBrowser browser : ldr){
                if(browser.isPresent()){
                    existingBrowsers.add(browser);
                    Settings.getLogger().info(browser.getName() + " found"); //$NON-NLS-1$
                }
                else{
                    Settings.getLogger().info(browser.getName() + " not found"); //$NON-NLS-1$
                }
            }
        }

        return existingBrowsers;
    }

    private void createLanguagesMenu(JMenu parent){

        Properties p = new Properties();
        InputStream is = null;
        try {
            is = WebCacheDigger.class.getResourceAsStream("/resources/lang/supportedLanguages.properties"); //$NON-NLS-1$
            p.load(is);
        } catch (IOException e1) {
            Settings.getLogger().error("", e1); //$NON-NLS-1$
        }
        finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    Settings.getLogger().error("", e); //$NON-NLS-1$
                }
            }
        }

        String supported = p.getProperty("supported", "en"); //$NON-NLS-1$ //$NON-NLS-2$
        String[] lang = supported.split(","); //$NON-NLS-1$

        ButtonGroup group = new ButtonGroup();

        for(int i = 0; i<lang.length; i++){
            Locale loc = new Locale(lang[i]);
            JRadioButtonMenuItem mi = new JRadioButtonMenuItem(loc.getDisplayLanguage());
            String s = Settings.getInstance().getLanguage();

            if(s.equals(loc.getLanguage())){
                mi.setSelected(true);
            }
            mi.setActionCommand(loc.getLanguage());
            mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doLanguageSelected(e);
                }
            });

            group.add(mi);

            parent.add(mi);
        }
    }

    private void createLafMenu(JMenu parent){
        ButtonGroup group = new ButtonGroup();

        JRadioButtonMenuItem mi = new JRadioButtonMenuItem(Messages.getString("WebCacheDigger.1")); //$NON-NLS-1$
        mi.setActionCommand("default"); //$NON-NLS-1$
        mi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doLafSelected(e);
            }
        });

        String currentLaf = Settings.getInstance().getLookAndFeel();

        if(currentLaf == null || currentLaf.equals("default") || currentLaf.isEmpty()){ //$NON-NLS-1$
            mi.setSelected(true);
        }

        group.add(mi);
        parent.add(mi);

        JSeparator sep = new JSeparator();
        parent.add(sep);

        LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        for(LookAndFeelInfo laf: lafs){
            mi = new JRadioButtonMenuItem(laf.getName());

            mi.setActionCommand(laf.getClassName());
            mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doLafSelected(e);
                }
            });

            if(laf.getClassName().equals(currentLaf)){
                mi.setSelected(true);
            }

            group.add(mi);
            parent.add(mi);
        }
    }

    protected void doLafSelected(ActionEvent e) {
//System.out.println(e.getActionCommand());
        Settings.getInstance().setLookAndFeel(e.getActionCommand());
        JOptionPane.showMessageDialog(this.getJContentPane(), Messages.getString("WebCacheDigger.2")); //$NON-NLS-1$
    }

    private JMenu getMnLanguage() {
        if (mnLanguage == null) {
            mnLanguage = new JMenu(Messages.getString("WebCacheDigger.Language")); //$NON-NLS-1$

            createLanguagesMenu(mnLanguage);
        }
        return mnLanguage;
    }

    protected void doLanguageSelected(ActionEvent e) {
        Settings.getInstance().setLanguage(e.getActionCommand());
        //Locale l = new Locale(Settings.getInstance().getLanguage());
        //Messages.setLocale(l);
        JOptionPane.showMessageDialog(this.getJContentPane(), Messages.getString("WebCacheDigger.RestartAppForNewLanguage")); //$NON-NLS-1$
    }

    private JMenu getMnLookAndFeel() {
        if (mnLookAndFeel == null) {
            mnLookAndFeel = new JMenu(Messages.getString("WebCacheDigger.3")); //$NON-NLS-1$

            createLafMenu(mnLookAndFeel);
        }
        return mnLookAndFeel;
    }

    LogfileDialog logfileDialog;
    private JSeparator separator;

    private JMenuItem getMntmLog() {
        if (mntmLog == null) {
            mntmLog = new JMenuItem(Messages.getString("WebCacheDigger.mntmLog.text")); //$NON-NLS-1$
            mntmLog.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(logfileDialog == null){
                        logfileDialog = new LogfileDialog(getJFrame());
                    }

                    logfileDialog.setVisible(true);
                }
            });
        }
        return mntmLog;
    }
    private JSeparator getSeparator() {
        if (separator == null) {
            separator = new JSeparator();
        }
        return separator;
    }
}

