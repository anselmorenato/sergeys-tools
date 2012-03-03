package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Point;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ServiceLoader;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.sergeys.coverfinder.logic.AcoustIdUtil;
import org.sergeys.coverfinder.logic.Album;
import org.sergeys.coverfinder.logic.FileCollectorWorker;
import org.sergeys.coverfinder.logic.IImageSearchEngine;
import org.sergeys.coverfinder.logic.IProgressWatcher;
import org.sergeys.coverfinder.logic.IdentifyTrackResult;
import org.sergeys.coverfinder.logic.IdentifyTrackWorker;
import org.sergeys.coverfinder.logic.Mp3Utils;
import org.sergeys.coverfinder.logic.MusicItem;
import org.sergeys.coverfinder.logic.Settings;
import org.sergeys.coverfinder.logic.Track;
import org.sergeys.coverfinder.ui.EditTagsDialog.EditTagsEvent;
import org.sergeys.coverfinder.ui.ImageDetailsDialog.EditImageEvent;
import org.sergeys.library.swing.DisabledPanel;

public class CoverFinder
{
    //private JFrame frmCoverFinder;
    protected JFrame frmCoverFinder;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    // redirect outs to log files
                    File logfile = new File(Settings.getSettingsDirPath() + File.separator + "output.log"); //$NON-NLS-1$
                    if(logfile.exists()){
                        logfile.delete();
                    }
//                    PrintStream ps = new PrintStream(logfile);
//                    System.setOut(ps);
//                    System.setErr(ps);

                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); //$NON-NLS-1$

                    Locale l = new Locale(Settings.getInstance().getLanguage());
                    //Locale l = new Locale("ru");
                    Locale.setDefault(l);


                    final CoverFinder mainWindow = new CoverFinder();

                    // set size and position of main window
                    if(Settings.getInstance().getWindowLocation() == null){
                        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                        int w = screen.width / 5;
                        int h = screen.height / 5;
                        Dimension size = new Dimension(w * 3, h * 3);
                        Point loc = new Point(w, h);
                        mainWindow.frmCoverFinder.setLocation(loc);
                        mainWindow.frmCoverFinder.setSize(size);
                        mainWindow.splitPane.setDividerLocation(w * 2);
                    }
                    else{
                        mainWindow.frmCoverFinder.setLocation(Settings.getInstance().getWindowLocation());
                        mainWindow.frmCoverFinder.setSize(Settings.getInstance().getWindowSize());
                    }

                    // http://mindprod.com/jgloss/close.html
                    mainWindow.frmCoverFinder.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    mainWindow.frmCoverFinder.addWindowListener(new WindowAdapter(){
                        @Override
                        public void windowClosing(WindowEvent e) {
                            //super.windowClosing(e);
                            mainWindow.doExit();
                        }
                    });

                    mainWindow.frmCoverFinder.setVisible(true);

                    if(Settings.getInstance().getLibraryPaths().isEmpty()){
                        JOptionPane.showMessageDialog(mainWindow.frmCoverFinder, Messages.getString("CoverFinder.PleaseEditSettings")); //$NON-NLS-1$
                        mainWindow.doSettings();
                    }
                    else{
                        // scan for changes
                        mainWindow.scanLibrary();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public CoverFinder() {
        initialize();
    }

    //private ActionListener actionListener = new ActionListener() {
    protected ActionListener actionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e instanceof EditTagsEvent){
                EditTagsEvent etEvt = (EditTagsEvent)e;

                if(etEvt.musicItem instanceof Track){
                    Mp3Utils.getInstance().
                        updateTags((Track)etEvt.musicItem, etEvt.artist, etEvt.title, etEvt.albumTitle);
                }
                else{
                    for(@SuppressWarnings("rawtypes")
                    Enumeration en = etEvt.musicItem.children(); en.hasMoreElements();){
                        Track track = (Track)en.nextElement();
                        Mp3Utils.getInstance().
                            updateTags(track, etEvt.artist, etEvt.title, etEvt.albumTitle);
                    }
                }

            }
            else if(e instanceof EditImageEvent){
                EditImageEvent evt = (EditImageEvent)e;

                if(evt.musicItem instanceof Track){
                    Mp3Utils.getInstance().updateArtwork((Track)evt.musicItem, evt.imgResult.getImageFile());
                }
                else{
                    for(@SuppressWarnings("rawtypes")
                    Enumeration en = evt.musicItem.children(); en.hasMoreElements();){
                        Track track = (Track)en.nextElement();
                        Mp3Utils.getInstance().updateArtwork(track, evt.imgResult.getImageFile());
                    }
                }
            }
            else if(e.getSource() instanceof JMenuItem){
                JMenuItem item = (JMenuItem)e.getSource();
                if(item.getName().equals(TrackTreePanel.MENU_IDENTIFY_TRACK)){
                    doIdentify();
                }
                else if(item.getName().equals(TrackTreePanel.MENU_SEARCH_COVER)){
                    doSearch();
                }
                else if(item.getName().equals(TrackTreePanel.MENU_EDIT_NAME)){
                    doEditTags();
                }
                else if(item.getName().equals(TrackTreePanel.MENU_OPEN_LOCATION)){
                    doOpenLocation();
                }
            }
        }
    };


    DisabledPanel dPanelCenter;

    private JMenuBar menuBar;
    private JMenu mnNewMenu;
    private JMenuItem mntmExit;
    private JMenu mnSettings;
    private JMenu mnLanguage;
    private JMenuItem mntmSettings;
    private JMenu mnHelp;
    private JMenuItem mntmAbout;
    DisabledPanel dPanelTop;
    TrackTreePanel panelTree;

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmCoverFinder = new JFrame();
        frmCoverFinder.setTitle("Cover Finder"); //$NON-NLS-1$
        frmCoverFinder.setIconImage(Toolkit.getDefaultToolkit().getImage(CoverFinder.class.getResource("/images/icon.png"))); //$NON-NLS-1$
        frmCoverFinder.setBounds(100, 100, 450, 300);
        frmCoverFinder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelTop = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panelTop.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        dPanelTop = new DisabledPanel(panelTop);
        frmCoverFinder.getContentPane().add(dPanelTop, BorderLayout.NORTH);

        btnSearchArtwork = new JButton(Messages.getString("CoverFinder.SearchArtwork")); //$NON-NLS-1$
        btnSearchArtwork.setIcon(new ImageIcon(CoverFinder.class.getResource("/images/image.png"))); //$NON-NLS-1$
        btnSearchArtwork.setEnabled(false);
        btnSearchArtwork.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSearch();
            }
        });
        panelTop.add(btnSearchArtwork);

        btnEditTags = new JButton(Messages.getString("CoverFinder.EditTags")); //$NON-NLS-1$
        btnEditTags.setIcon(new ImageIcon(CoverFinder.class.getResource("/images/kedit.png"))); //$NON-NLS-1$
        btnEditTags.setEnabled(false);
        btnEditTags.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doEditTags();
            }
        });

//        btnTest = new JButton("Test");
//        btnTest.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent arg0) {
//                doTest();
//            }
//        });
//        panelTop.add(btnTest);
//        btnTest.setEnabled(false);


        btnIdentifyTrack = new JButton(Messages.getString("CoverFinder.IdentifyTrack")); //$NON-NLS-1$
        btnIdentifyTrack.setIcon(new ImageIcon(CoverFinder.class.getResource("/images/cdaudio_unmount.png"))); //$NON-NLS-1$
        btnIdentifyTrack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doIdentify();
            }
        });
        panelTop.add(btnIdentifyTrack);
        btnIdentifyTrack.setEnabled(false);
        panelTop.add(btnEditTags);

        btnOpenLocation = new JButton(Messages.getString("CoverFinder.OpenLocation")); //$NON-NLS-1$
        btnOpenLocation.setIcon(new ImageIcon(CoverFinder.class.getResource("/images/my_docs.png"))); //$NON-NLS-1$
        btnOpenLocation.setEnabled(false);
        btnOpenLocation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doOpenLocation();
            }
        });
        panelTop.add(btnOpenLocation);

        lblTagsEncoding = new JLabel(Messages.getString("CoverFinder.TagsEncoding")); //$NON-NLS-1$
        panelTop.add(lblTagsEncoding);

        comboBoxTagEncoding = new JComboBox<String>();

        panelTop.add(comboBoxTagEncoding);
        locales = new ArrayList<Locale>();
        comboBoxTagEncoding.addItem(Messages.getString("CoverFinder.DoNotChange")); //$NON-NLS-1$
        comboBoxTagEncoding.setSelectedIndex(0);
        locales.add(null);
        for(Object lang: Mp3Utils.getInstance().getDecodingLanguages()){
            Locale l = new Locale(lang.toString());
            comboBoxTagEncoding.addItem(l.getDisplayName());
            if(lang.toString().equals(Settings.getInstance().getAudioTagsLanguage())){
                comboBoxTagEncoding.setSelectedItem(l.getDisplayName());
            }
            locales.add(l);
        }
        // add listener after adding items, or saved selection will be lost
        comboBoxTagEncoding.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doTagEncodingChanged();
            }
        });

        panelTree = new TrackTreePanel(actionListener);

        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BorderLayout(0, 0));
        splitPane = new JSplitPane();
        panelCenter.add(splitPane);
        splitPane.setLeftComponent(panelTree);
        trackDetailsPanel = new TrackDetailsPanel();
        splitPane.setRightComponent(trackDetailsPanel);
        splitPane.setDividerLocation(Settings.getInstance().getSplitterPosition());

        //dPanelCenter = new DisabledPanel(panelTree);
        dPanelCenter = new DisabledPanel(panelCenter);
        frmCoverFinder.getContentPane().add(dPanelCenter, BorderLayout.CENTER);

        panelTree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent e) {

                Object o = e.getPath().getLastPathComponent();

                // adjust available buttons
                btnIdentifyTrack.setEnabled(o instanceof Track && AcoustIdUtil.getInstance().isAvailable());
//                btnTest.setEnabled(o instanceof Track || o instanceof Album);
                btnSearchArtwork.setEnabled(o instanceof Track || o instanceof Album);
                btnEditTags.setEnabled(o instanceof Track || o instanceof Album);
                btnOpenLocation.setEnabled((o instanceof Track || o instanceof Album) && Desktop.isDesktopSupported());

                if(o instanceof Track){
                    trackDetailsPanel.setTrack((Track)o);
                }
            }});


        panelStatusBar = new StatusBarPanel();
        panelStatusBar.setMessage(Messages.getString("CoverFinder.Ready")); //$NON-NLS-1$
        panelStatusBar.setWorking(false);
        frmCoverFinder.getContentPane().add(panelStatusBar, BorderLayout.SOUTH);

        menuBar = new JMenuBar();
        frmCoverFinder.setJMenuBar(menuBar);

        mnNewMenu = new JMenu(Messages.getString("CoverFinder.File")); //$NON-NLS-1$
        menuBar.add(mnNewMenu);

        mntmExit = new JMenuItem(Messages.getString("CoverFinder.Exit")); //$NON-NLS-1$
        mntmExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doExit();
            }
        });

        mntmRescanLibrary = new JMenuItem(Messages.getString("CoverFinder.RescanLibrary")); //$NON-NLS-1$
        mntmRescanLibrary.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doRescanLibrary();
            }
        });
        mnNewMenu.add(mntmRescanLibrary);
        mnNewMenu.add(mntmExit);

        mnSettings = new JMenu(Messages.getString("CoverFinder.Settings")); //$NON-NLS-1$
        menuBar.add(mnSettings);

        mnLanguage = new JMenu(Messages.getString("CoverFinder.Language")); //$NON-NLS-1$
        mnSettings.add(mnLanguage);

        createLanguagesMenu(mnLanguage);

        mntmSettings = new JMenuItem(Messages.getString("CoverFinder.Settings")); //$NON-NLS-1$
        mntmSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSettings();
            }
        });
        mnSettings.add(mntmSettings);

        mnHelp = new JMenu(Messages.getString("CoverFinder.Help")); //$NON-NLS-1$
        menuBar.add(mnHelp);

        mntmAbout = new JMenuItem(Messages.getString("CoverFinder.About")); //$NON-NLS-1$
        mntmAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doAbout(e);
            }
        });
        mnHelp.add(mntmAbout);
    }

    protected void doOpenLocation() {
        Object o = panelTree.getSelectedItem();
        if(o != null && Desktop.isDesktopSupported()){
            if(o instanceof Track){
                try {
                    Desktop.getDesktop().open(new File(((Track)o).getFilesystemDir()));
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            else if(o instanceof Album){
                try {
                    Desktop.getDesktop().open(new File(((Album)o).getFilesystemDir()));
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }

    protected void doTagEncodingChanged() {
        int selectedIndex = comboBoxTagEncoding.getSelectedIndex();
        if(selectedIndex > 0){
            Locale l = locales.get(selectedIndex);
            Settings.getInstance().setAudioTagsLanguage(l.getLanguage());
        }
        else if(selectedIndex == 0){
            Settings.getInstance().setAudioTagsLanguage(null);
        }

        if(dPanelCenter != null){
            scanLibrary();
        }
    }

    protected void doEditTags() {
        Object selected = panelTree.getSelectedItem();
        if(selected != null && selected instanceof MusicItem){
            EditTagsDialog dlg = new EditTagsDialog(frmCoverFinder, actionListener);
            dlg.setMusicItem((MusicItem)selected);
            dlg.setLocationRelativeTo(frmCoverFinder);
            dlg.setVisible(true);
        }
    }

    protected void doIdentify() {
        Object item = panelTree.getSelectedItem();
        if(item != null && item instanceof Track){
            final Track tr = (Track)item;

            IdentifyTrackWorker worker = new IdentifyTrackWorker(tr, new IProgressWatcher<IdentifyTrackResult>(){

                @Override
                public void updateStage(Stage stage) {
                }

                @Override
                public void updateProgress(long count, Stage stage) {
                }

                @Override
                public void progressComplete(Collection<IdentifyTrackResult> items, Stage stage) {
                    dPanelTop.setEnabled(true);
                    dPanelCenter.setEnabled(true);
                    panelStatusBar.setMessage("OK", false); //$NON-NLS-1$

                    if(items != null){
                        if(items.size() > 0){
                            IdentifyTrackDialog dlg = new IdentifyTrackDialog((List<IdentifyTrackResult>) items,
                                    tr, frmCoverFinder, actionListener);    // TODO: cast allowed??
                            dlg.setLocationRelativeTo(frmCoverFinder);
                            dlg.setVisible(true);
                        }
                        else{
                            JOptionPane.showMessageDialog(frmCoverFinder, Messages.getString("CoverFinder.TrackIsNotKnown")); //$NON-NLS-1$
                        }
                    }
                }

                @Override
                public boolean isAllowedToContinue(Stage stage) {
                    return false;
                }

                @Override
                public void reportException(Throwable ex) {
                    String message;
                    if(ex.getCause() != null){
                        message = String.format(Messages.getString("CoverFinder.FailedToIdentifyTrack2"), ex.getLocalizedMessage(), ex.getCause().getLocalizedMessage()); //$NON-NLS-1$
                    }
                    else{
                        message = String.format(Messages.getString("CoverFinder.FailedToIdentifyTrack1"), ex.getLocalizedMessage()); //$NON-NLS-1$
                    }
                    JOptionPane.showMessageDialog(frmCoverFinder, message);
                }});

            dPanelTop.setEnabled(false);
            dPanelCenter.setEnabled(false);
            panelStatusBar.setMessage(Messages.getString("CoverFinder.IdentifyingTrack"), true); //$NON-NLS-1$

            worker.execute();
        }

    }

    protected void doRescanLibrary() {
        scanLibrary();
    }

    ImageSearchDialog imageSearchDlg;
    protected void doSearch() {
        String query;

        Object o = panelTree.getSelectedItem();
        if(o instanceof Album){
            query = String.format("%s %s", ((Album)o).getArtist(), ((Album)o).getTitle()); //$NON-NLS-1$
        }
        else if(o instanceof Track){
            query = String.format("%s %s", ((Track)o).getArtist(), ((Track)o).getAlbumTitle()); //$NON-NLS-1$
        }
        else{
            return;
        }

        if(imageSearchDlg == null){
            imageSearchDlg = new ImageSearchDialog(this.frmCoverFinder, (MusicItem)o, actionListener);
            imageSearchDlg.setLocationRelativeTo(frmCoverFinder);
        }

        imageSearchDlg.setQuery(query, (MusicItem)o);
        imageSearchDlg.setVisible(true);
    }

    protected void scanLibrary(){
//        Settings.getInstance().getLibraryPaths().clear();
//        Settings.getInstance().getLibraryPaths().add("c:\\tmp\\test");
//        Settings.getInstance().getLibraryPaths().add("i:\\music\\2");
//        Settings.getInstance().getLibraryPaths().add("i:\\music\\3");
        //Settings.getInstance().getLibraryPaths().add("i:\\music");
        //Settings.getInstance().getLibraryPaths().add(System.getProperty("user.home"));


        ArrayList<File> paths = new ArrayList<File>();
        for(String path: Settings.getInstance().getLibraryPaths()){
System.out.println("will scan " + path);             //$NON-NLS-1$
            paths.add(new File(path));
        }

        dPanelTop.setEnabled(false);
        dPanelCenter.setEnabled(false);
        panelStatusBar.setMessage(Messages.getString("CoverFinder.ScanningLibrary"), true); //$NON-NLS-1$

        FileCollectorWorker fcw = new FileCollectorWorker(paths, new IProgressWatcher<Track>(){

            @Override
            public void updateStage(Stage stage) {
            }

            @Override
            public void updateProgress(long count, Stage stage) {
                switch(stage){
                    case Collecting:
                        panelStatusBar.setMessage(Messages.getString("CoverFinder.Collected") + count); //$NON-NLS-1$
                        break;
                    case Analyzing:
                        panelStatusBar.setMessage(Messages.getString("CoverFinder.Analyzed") + count); //$NON-NLS-1$
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void progressComplete(Collection<Track> items, Stage stage) {
                dPanelTop.setEnabled(true);
                dPanelCenter.setEnabled(true);

                if(items != null){

                    panelTree.update();
                    panelStatusBar.setMessage(Messages.getString("CoverFinder.FoundFiles") + items.size(), false); //$NON-NLS-1$
                }
                else{
                    panelStatusBar.setMessage("", false); //$NON-NLS-1$
                }
            }

            @Override
            public boolean isAllowedToContinue(Stage stage) {
                return true;
            }

            @Override
            public void reportException(Throwable ex) {
                JOptionPane.showMessageDialog(frmCoverFinder, String.format(Messages.getString("CoverFinder.FailedToScanMusicFiles"), ex.getLocalizedMessage()));                 //$NON-NLS-1$
            }});

        fcw.execute();
    }

    AboutDialog aboutDialog;
    protected void doAbout(ActionEvent e) {
        if(aboutDialog == null){
            aboutDialog = new AboutDialog(frmCoverFinder);
            aboutDialog.setLocationRelativeTo(frmCoverFinder);
        }
        aboutDialog.setVisible(true);
    }

    SettingsDialog settingsDlg;
    protected void doSettings() {
        if(settingsDlg == null){
            settingsDlg = new SettingsDialog(frmCoverFinder);
            settingsDlg.setLocationRelativeTo(frmCoverFinder.getContentPane());
            settingsDlg.addPropertyChangeListener(SettingsDialog.SETTINGS_SAVED_PROPERTY, new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    doRescanLibrary();
                }
            });
        }
        settingsDlg.setVisible(true);
    }

    protected void doExit() {
        Settings.getInstance().setWindowLocation(frmCoverFinder.getLocation());
        Settings.getInstance().setWindowSize(frmCoverFinder.getSize());
        Settings.getInstance().setSplitterPosition(splitPane.getDividerLocation());

        try {
            Settings.save();
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        frmCoverFinder.setVisible(false);
        frmCoverFinder.dispose();

        System.exit(0);
    }

//    protected void doTest() {
//
//        String query = "led zeppelin \"houses of the holy\"";//txtQuery.getText();
//
//        if(query.isEmpty()){
//            return;
//        }
//
//        Settings.getInstance().setSearchEngineName("Bing");
//        //Settings.getInstance().setSearchEngineName("Google Image Search");
//        IImageSearchEngine engine = CoverFinder.getSearchEngine();
//        System.out.println("search via " + engine.getName());
//
//        ImageSearchRequest r = new ImageSearchRequest();
//        r.setQuery(query);
//        Collection<ImageSearchResult> res = engine.search(r);
//        for(ImageSearchResult item: res){
//            System.out.println(item.getWidth() + "x" + item.getHeight() + " " + item.getImageUrl());
//        }
//
//
//        //panelJunk.removeAll();
//        Image img = null;
//        try{
//            for(ImageSearchResult item: res){
//                img = ImageIO.read(item.getThumbnailUrl());
//
//                ScaledImage scaledImage = new ScaledImage(img, false);
//
//                scaledImage.setPreferredSize(new Dimension(100, 100));
//                scaledImage.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
//
//                //panelJunk.add(scaledImage);
//                scaledImage.invalidate();
//            }
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }

    public static LinkedHashSet<IImageSearchEngine> getSearchEngines(){
        LinkedHashSet<IImageSearchEngine> engines = new LinkedHashSet<IImageSearchEngine>();
        ServiceLoader<IImageSearchEngine> ldr = ServiceLoader.load(IImageSearchEngine.class);
        for(IImageSearchEngine engine: ldr){
            engines.add(engine);
        }

        return engines;
    }

    private static IImageSearchEngine searchEngine = null;
//    private JButton btnTest;
    //private StatusBarPanel panelStatusBar;
    protected StatusBarPanel panelStatusBar;
    private JMenuItem mntmRescanLibrary;
    protected JButton btnIdentifyTrack;
    private JLabel lblTagsEncoding;
    private JComboBox<String> comboBoxTagEncoding;
    private ArrayList<Locale> locales;
    protected TrackDetailsPanel trackDetailsPanel;
    //private JSplitPane splitPane;
    protected JSplitPane splitPane;
    protected JButton btnEditTags;
    protected JButton btnOpenLocation;


    protected JButton btnSearchArtwork;

    public static IImageSearchEngine getSearchEngine(){

        if(searchEngine == null){
            LinkedHashSet<IImageSearchEngine> engines = getSearchEngines();
            for(IImageSearchEngine engine: engines){
                if(engine.getName().equals(Settings.getInstance().getSearchEngineName())){
                    searchEngine = engine;
                    break;
                }
            }
        }

        return searchEngine;
    }

    private void createLanguagesMenu(JMenu parent){

        Properties p = new Properties();
        InputStream is = CoverFinder.class.getResourceAsStream("/resources/lang/supportedLanguages.properties");
        try {
            p.load(is); //$NON-NLS-1$
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        String supported = p.getProperty("supported", "en"); //$NON-NLS-1$ //$NON-NLS-2$
        String[] lang = supported.split(","); //$NON-NLS-1$

        ButtonGroup group = new ButtonGroup();

        for(int i = 0; i < lang.length; i++){
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

    protected void doLanguageSelected(ActionEvent e) {
        Settings.getInstance().setLanguage(e.getActionCommand());
        JOptionPane.showMessageDialog(frmCoverFinder, Messages.getString("CoverFinder.PleaseRestartAppForLang"));         //$NON-NLS-1$
    }

}
