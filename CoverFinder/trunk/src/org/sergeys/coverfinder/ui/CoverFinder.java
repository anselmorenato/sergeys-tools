package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ServiceLoader;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.sergeys.coverfinder.logic.AcoustIdUtil;
import org.sergeys.coverfinder.logic.Album;
import org.sergeys.coverfinder.logic.FileCollectorWorker;
import org.sergeys.coverfinder.logic.IImageSearchEngine;
import org.sergeys.coverfinder.logic.IProgressWatcher;
import org.sergeys.coverfinder.logic.IdentifyTrackResult;
import org.sergeys.coverfinder.logic.IdentifyTrackWorker;
import org.sergeys.coverfinder.logic.ImageSearchRequest;
import org.sergeys.coverfinder.logic.ImageSearchResult;
import org.sergeys.coverfinder.logic.Mp3Utils;
import org.sergeys.coverfinder.logic.MusicItem;
import org.sergeys.coverfinder.logic.Settings;
import org.sergeys.coverfinder.logic.Track;
import org.sergeys.coverfinder.ui.EditTagsDialog.EditTagsEvent;
import org.sergeys.coverfinder.ui.ImageDetailsDialog.EditImageEvent;
import org.sergeys.library.swing.DisabledPanel;
import org.sergeys.library.swing.ScaledImage;

public class CoverFinder  
{	
	private JFrame frmCoverFinder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					// redirect outs to log files
					File logfile = new File(Settings.getSettingsDirPath() + File.separator + "output.log");
					if(logfile.exists()){
						logfile.delete();
					}
//					PrintStream ps = new PrintStream(logfile);
//					System.setOut(ps);					
//					System.setErr(ps);
					
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					
					Locale l = new Locale(Settings.getInstance().getLanguage());
					//Locale l = new Locale("ru");
					Locale.setDefault(l);
					

					final CoverFinder mainWindow = new CoverFinder();										
					
					// set size and position of main window									
					if(Settings.getInstance().getWindowLocation() == null){
						//Dimension desktop = Toolkit.getDefaultToolkit().getScreenSize();
						
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
					
					mainWindow.scanLibrary();
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

	private ActionListener actionListener = new ActionListener() {

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
		frmCoverFinder.setTitle("Cover Finder");
		frmCoverFinder.setIconImage(Toolkit.getDefaultToolkit().getImage(CoverFinder.class.getResource("/images/icon.png")));
		frmCoverFinder.setBounds(100, 100, 450, 300);
		frmCoverFinder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelTop = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelTop.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		dPanelTop = new DisabledPanel(panelTop);
		frmCoverFinder.getContentPane().add(dPanelTop, BorderLayout.NORTH);
		
		JButton btnSearchFiles = new JButton("Search");
		btnSearchFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearch();
			}
		});
		panelTop.add(btnSearchFiles);
		
		btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doTest();
			}
		});
		panelTop.add(btnTest);
		btnTest.setEnabled(false);
		
		
		btnIdentify = new JButton("Identify");
		btnIdentify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doIdentify();
			}
		});
		panelTop.add(btnIdentify);
		btnIdentify.setEnabled(false);
		
		lblTagsEncoding = new JLabel("Tags encoding:");
		panelTop.add(lblTagsEncoding);
		
		comboBoxTagEncoding = new JComboBox();
		
		panelTop.add(comboBoxTagEncoding);		
		locales = new ArrayList<Locale>();
		comboBoxTagEncoding.addItem("do not change");
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
		dPanelCenter = new DisabledPanel(panelTree);
		frmCoverFinder.getContentPane().add(dPanelCenter, BorderLayout.CENTER);
		
		panelTree.addTreeSelectionListener(new TreeSelectionListener(){

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				
				Object o = e.getPath().getLastPathComponent();
				
				// adjust available buttons
				btnIdentify.setEnabled(o instanceof Track && AcoustIdUtil.getInstance().isAvailable());
				btnTest.setEnabled(o instanceof Track || o instanceof Album);

			}});
										
		
		panelStatusBar = new StatusBarPanel();
		panelStatusBar.setMessage("Ready");
		panelStatusBar.setWorking(false);
		frmCoverFinder.getContentPane().add(panelStatusBar, BorderLayout.SOUTH);
		
		menuBar = new JMenuBar();
		frmCoverFinder.setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doExit();
			}
		});
		
		mntmRescanLibrary = new JMenuItem("Rescan library");
		mntmRescanLibrary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doRescanLibrary();
			}
		});
		mnNewMenu.add(mntmRescanLibrary);
		mnNewMenu.add(mntmExit);
		
		mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		mnLanguage = new JMenu("Language");
		mnSettings.add(mnLanguage);
		
		mntmSettings = new JMenuItem("Settings");
		mntmSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSettings(e);
			}
		});
		mnSettings.add(mntmSettings);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doAbout(e);
			}
		});
		mnHelp.add(mntmAbout);
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
					panelStatusBar.setMessage("OK", false);
					
					if(items != null){
						if(items.size() > 0){
							IdentifyTrackDialog dlg = new IdentifyTrackDialog((List<IdentifyTrackResult>) items, 
									tr, frmCoverFinder, actionListener);	// TODO: cast allowed??
							dlg.setLocationRelativeTo(frmCoverFinder);
							dlg.setVisible(true);
						}
						else{
							JOptionPane.showMessageDialog(frmCoverFinder, "Track is not known at acoustid.org");
						}
					}					
				}

				@Override
				public boolean isAllowedToContinue(Stage stage) {
					return false;
				}

				@Override
				public void reportException(Throwable ex) {
					JOptionPane.showMessageDialog(frmCoverFinder, String.format("Failed to identify track:\n%s", ex.getLocalizedMessage()));
				}});
			
			dPanelTop.setEnabled(false);
			dPanelCenter.setEnabled(false);
			panelStatusBar.setMessage("Identifying track...", true);
			
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
			query = String.format("%s %s", ((Album)o).getArtist(), ((Album)o).getTitle());
		}
		else if(o instanceof Track){
			query = String.format("%s %s", ((Track)o).getArtist(), ((Track)o).getAlbumTitle());
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

	private void scanLibrary(){
//		Settings.getInstance().getLibraryPaths().clear();
//		Settings.getInstance().getLibraryPaths().add("c:\\tmp\\test");
//		Settings.getInstance().getLibraryPaths().add("i:\\music\\2");
//		Settings.getInstance().getLibraryPaths().add("i:\\music\\3");
		//Settings.getInstance().getLibraryPaths().add("i:\\music");
		//Settings.getInstance().getLibraryPaths().add(System.getProperty("user.home"));

		
		ArrayList<File> paths = new ArrayList<File>();
		for(String path: Settings.getInstance().getLibraryPaths()){
System.out.println("will scan " + path);			
			paths.add(new File(path));
		}		
		
		dPanelTop.setEnabled(false);
		dPanelCenter.setEnabled(false);
		panelStatusBar.setMessage("Scanning library...", true);
		
		FileCollectorWorker fcw = new FileCollectorWorker(paths, new IProgressWatcher<Track>(){

			@Override
			public void updateStage(Stage stage) {
			}

			@Override
			public void updateProgress(long count, Stage stage) {
				switch(stage){
					case Collecting:
						panelStatusBar.setMessage("Collected: " + count);
						break;
					case Analyzing:
						panelStatusBar.setMessage("Analyzed: " + count);
						break;
				}
			}

			@Override
			public void progressComplete(Collection<Track> items, Stage stage) {
				dPanelTop.setEnabled(true);
				dPanelCenter.setEnabled(true);
				
				if(items != null){
														
					panelTree.update();					
					panelStatusBar.setMessage("Found files: " + items.size(), false);
				}
				else{
					panelStatusBar.setMessage("", false);
				}
			}

			@Override
			public boolean isAllowedToContinue(Stage stage) {
				return true;
			}

			@Override
			public void reportException(Throwable ex) {
				JOptionPane.showMessageDialog(frmCoverFinder, String.format("Failed to scan music files:\n%s", ex.getLocalizedMessage()));				
			}});
		
		fcw.execute();
	}
	
	protected void doAbout(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	SettingsDialog settingsDlg;
	protected void doSettings(ActionEvent e) {
		if(settingsDlg == null){
			settingsDlg = new SettingsDialog(frmCoverFinder);
			settingsDlg.setLocationRelativeTo(frmCoverFinder.getContentPane());
		}
		settingsDlg.setVisible(true);
	}

	protected void doExit() {
		Settings.getInstance().setWindowLocation(frmCoverFinder.getLocation());
		Settings.getInstance().setWindowSize(frmCoverFinder.getSize());
		
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

	protected void doTest() {
		
		String query = "led zeppelin \"houses of the holy\"";//txtQuery.getText();
		
		if(query.isEmpty()){
			return;
		}
				
		Settings.getInstance().setSearchEngineName("Bing");
		//Settings.getInstance().setSearchEngineName("Google Image Search");
		IImageSearchEngine engine = CoverFinder.getSearchEngine();
		System.out.println("search via " + engine.getName());
		
		ImageSearchRequest r = new ImageSearchRequest();
		r.setQuery(query);
		Collection<ImageSearchResult> res = engine.search(r);
		for(ImageSearchResult item: res){
			System.out.println(item.getWidth() + "x" + item.getHeight() + " " + item.getImageUrl());
		}
				
		
		//panelJunk.removeAll();
		Image img = null;
		try{
			for(ImageSearchResult item: res){
				img = ImageIO.read(item.getThumbnailUrl());
				
				ScaledImage scaledImage = new ScaledImage(img, false);
				
				scaledImage.setPreferredSize(new Dimension(100, 100));
				scaledImage.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
										
				//panelJunk.add(scaledImage);
				scaledImage.invalidate();
			}	
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static LinkedHashSet<IImageSearchEngine> getSearchEngines(){
		LinkedHashSet<IImageSearchEngine> engines = new LinkedHashSet<IImageSearchEngine>();
		ServiceLoader<IImageSearchEngine> ldr = ServiceLoader.load(IImageSearchEngine.class);
		for(IImageSearchEngine engine: ldr){
			engines.add(engine);
		}
		
		return engines;
	}
	
	
	private static IImageSearchEngine searchEngine = null;
	private JButton btnTest;
	private StatusBarPanel panelStatusBar;
	private JMenuItem mntmRescanLibrary;
	private JButton btnIdentify;
	private JLabel lblTagsEncoding;
	private JComboBox comboBoxTagEncoding;

	private ArrayList<Locale> locales;
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
}
