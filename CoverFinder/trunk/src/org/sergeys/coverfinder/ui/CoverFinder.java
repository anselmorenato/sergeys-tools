package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.ServiceLoader;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import org.sergeys.coverfinder.logic.AcoustIdUtil;
import org.sergeys.coverfinder.logic.AcoustIdUtil.Fingerprint;
import org.sergeys.coverfinder.logic.BingImageSearch;
import org.sergeys.coverfinder.logic.GoogleImageSearch;
import org.sergeys.coverfinder.logic.IImageSearchEngine;
import org.sergeys.coverfinder.logic.IProgressWatcher;
import org.sergeys.coverfinder.logic.ImageSearchRequest;
import org.sergeys.coverfinder.logic.ImageSearchResult;
import org.sergeys.coverfinder.logic.Track;
import org.sergeys.coverfinder.logic.Settings;
import org.sergeys.library.swing.DisabledPanel;
import org.sergeys.library.swing.ScaledImage;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class CoverFinder implements IProgressWatcher<Track> {

	
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					
					Locale l = new Locale(Settings.getInstance().getLanguage());
					Locale.setDefault(l);
										
					final CoverFinder mainWindow = new CoverFinder();										
					
					// set size and position of main window									
					if(Settings.getInstance().getWindowLocation() == null){
						Dimension desktop = Toolkit.getDefaultToolkit().getScreenSize();
						
					}
					else{
						mainWindow.frame.setLocation(Settings.getInstance().getWindowLocation());
						mainWindow.frame.setSize(Settings.getInstance().getWindowSize());
					}
										
					// http://mindprod.com/jgloss/close.html
					mainWindow.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					mainWindow.frame.addWindowListener(new WindowAdapter(){						
						@Override
						public void windowClosing(WindowEvent e) {
							//super.windowClosing(e);
							mainWindow.doExit();																					
						}
					});
					
					mainWindow.frame.setVisible(true);
					
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
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelTop = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelTop.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		dPanelTop = new DisabledPanel(panelTop);
		frame.getContentPane().add(dPanelTop, BorderLayout.NORTH);
		
		JButton btnSearchFiles = new JButton("Search");
		btnSearchFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearch(e);
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
		
		panelTree = new TrackTreePanel();
		dPanelCenter = new DisabledPanel(panelTree);
		frame.getContentPane().add(dPanelCenter, BorderLayout.CENTER);
		
		
						
		
		panelStatusBar = new StatusBarPanel();
		panelStatusBar.setMessage("Ready");
		panelStatusBar.setWorking(false);
		frame.getContentPane().add(panelStatusBar, BorderLayout.SOUTH);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
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

	protected void doRescanLibrary() {
		scanLibrary();
	}

	protected void doTest() {
		
		
		
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("i:\\music"));
		if (fc.showOpenDialog(this.frame) == JFileChooser.APPROVE_OPTION) {
			panelStatusBar.setMessage("Recognition...");
			panelStatusBar.setWorking(true);

			try{
				Fingerprint fp =
						AcoustIdUtil.getInstance().getFingerprint(fc.getSelectedFile());
						
				if(!fp.fingerprint.isEmpty()){				
					String titles = AcoustIdUtil.getInstance().identify(fp);
					
					JOptionPane.showMessageDialog(this.frame, titles);
				}
			}
			catch(Exception ex){
				JOptionPane.showMessageDialog(this.frame, ex.getMessage());
			}
		}
		
		panelStatusBar.setMessage("Ready");
		panelStatusBar.setWorking(false);
	}

	private void scanLibrary(){
		Settings.getInstance().getLibraryPaths().clear();
		//Settings.getInstance().getLibraryPaths().add("c:\\tmp\\test");
		Settings.getInstance().getLibraryPaths().add("i:\\music\\2");
		Settings.getInstance().getLibraryPaths().add("i:\\music\\3");

		
		ArrayList<File> paths = new ArrayList<File>();
		for(String path: Settings.getInstance().getLibraryPaths()){
			paths.add(new File(path));
		}		
		
		dPanelTop.setEnabled(false);
		dPanelCenter.setEnabled(false);
		panelStatusBar.setMessage("Scanning library...", true);
		
		FileCollectorWorker fcw = new FileCollectorWorker(paths, this);
		fcw.execute();
	}
	
	protected void doAbout(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	protected void doSettings(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	protected void doExit() {
		Settings.getInstance().setWindowLocation(frame.getLocation());
		Settings.getInstance().setWindowSize(frame.getSize());
		
		try {
			Settings.save();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		
		frame.setVisible(false);
		frame.dispose();
		
		System.exit(0);
	}

	protected void doSearch(ActionEvent e) {
		
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

	@Override
	public void updateStage(Stage stage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProgress(long count, Stage stage) {
		// TODO Auto-generated method stub
		//System.out.println("Found: " + count);
		panelStatusBar.setMessage("Examined: " + count);
	}

	@Override
	public void progressComplete(Collection<Track> items, Stage stage) {
		System.out.println("Found files: " + items.size());
		
		dPanelTop.setEnabled(true);
		dPanelCenter.setEnabled(true);
		panelTree.update();
		
		panelStatusBar.setMessage("Found files: " + items.size(), false);
	}

	@Override
	public boolean isAllowedToContinue(Stage stage) {
		// TODO Auto-generated method stub
		return false;
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
