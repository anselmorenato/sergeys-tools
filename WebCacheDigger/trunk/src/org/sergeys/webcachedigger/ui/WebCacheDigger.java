package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.Settings;
import org.sergeys.webcachedigger.logic.SimpleLogger;

public class WebCacheDigger 
implements ActionListener, PropertyChangeListener
{

	private JFrame jFrame = null;  //  @jve:decl-index=0:visual-constraint="10,10"
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
	private JPanel jPanelFileDetails = null;
	private JMenuItem jMenuItemSettings = null;
	
	private Settings settings;  //  @jve:decl-index=0:
	
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

	/**
	 * This method initializes jPanelFoundFilesActions	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelFoundFilesActions() {
		if (jPanelFoundFilesActions == null) {
			jPanelFoundFilesActions = new JPanel();
			jPanelFoundFilesActions.setLayout(new FlowLayout());
			jPanelFoundFilesActions.add(getJButtonCopySelectedFiles(), null);
		}
		return jPanelFoundFilesActions;
	}

	/**
	 * This method initializes jButtonCopySelectedFiles	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCopySelectedFiles() {
		if (jButtonCopySelectedFiles == null) {
			jButtonCopySelectedFiles = new JButton();
			jButtonCopySelectedFiles.setText("Copy Checked Files");
			jButtonCopySelectedFiles.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String targetDir = settings.getSaveToPath();
					int count = WebCacheDigger.this.copyFiles(targetDir);
					String msg = String.format("Copied %d file(s) to %s.", count, targetDir);
					
					JOptionPane.showMessageDialog(getJFrame(), 					 
							msg,
							"Message", 
							JOptionPane.INFORMATION_MESSAGE);
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
	private JSplitPane getJSplitPaneMain() {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			jButtonSearch.setText("Search");
			jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					WebCacheDigger.this.searchCachedFiles();
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
	private JPanel getJPanelFileDetails() {
		if (jPanelFileDetails == null) {
			jPanelFileDetails = new FileDetailsPanel(this, getSettings());
			
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
			jMenuItemSettings.setText("Settings ...");
			jMenuItemSettings.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					WebCacheDigger.this.editSettings();
				}
			});
		}
		return jMenuItemSettings;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}

				WebCacheDigger application = new WebCacheDigger();
				
				JFrame mainWindow = application.getJFrame(); 
				
				// set size and position of main window				
				Dimension desktop = Toolkit.getDefaultToolkit().getScreenSize();
								
				int x = (desktop.width - mainWindow.getWidth())/2;
				int y = (desktop.height - mainWindow.getHeight())/2;
				int w = mainWindow.getWidth();
				int h = mainWindow.getHeight();
				int divpos = application.getJSplitPaneMain().getDividerLocation();
				x = application.getSettings().getIntProperty(Settings.WINDOW_X, x);
				y = application.getSettings().getIntProperty(Settings.WINDOW_Y, y);
				w = application.getSettings().getIntProperty(Settings.WINDOW_W, w);
				h = application.getSettings().getIntProperty(Settings.WINDOW_H, h);
				divpos = application.getSettings().getIntProperty(Settings.SPLITTER_POS, divpos); 				
				
				mainWindow.setLocation(x, y);
				mainWindow.setSize(w, h);
				application.getJSplitPaneMain().setDividerLocation(divpos);
				mainWindow.setVisible(true);								
			}
		});
	}

	/**
	 * This method initializes jFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(WebCacheDigger.class.getResource("/images/icon.png")));
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setPreferredSize(new Dimension(750, 400));
			jFrame.setJMenuBar(getJJMenuBar());
			jFrame.setSize(750, 400);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("Web Cache Digger");
		}
		return jFrame;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
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
			fileMenu.setText("File");
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
			settingsMenu.setText("Settings");
			settingsMenu.add(getMnLanguage());
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
			helpMenu.setText("Help");
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
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					WebCacheDigger.this.exit();
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
			aboutMenuItem.setText("About ...");
			aboutMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialog aboutDialog = getAboutDialog();
					//aboutDialog.pack();
					//Point loc = getJFrame().getLocation();
					//loc.translate(20, 20);
					//aboutDialog.setLocation(loc);
					aboutDialog.setLocationRelativeTo(getJContentPane());
					aboutDialog.setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	private JDialog getAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new AboutDialog(this.getJFrame());
			
		}
		return aboutDialog;
	}
	
	FileSearchProgressDialog progressDialog;
	
	private void searchCachedFiles(){					
		
		try {
						
			if(progressDialog == null){
				progressDialog = new FileSearchProgressDialog(getSettings(), getExistingBrowsers());
				progressDialog.addPropertyChangeListener(this);
			}
			
					
			getFilesListPanel().setEnabled(false);
			
			progressDialog.setLocationRelativeTo(getJContentPane());
			progressDialog.setVisible(true);
						
			// http://www.medsea.eu/mime-util/detectors.html
			
//			String msg = String.format("Total files: %d", files.size());
//			
//			JOptionPane.showMessageDialog(getJFrame(), 					 
//					msg,
//					"Message", 
//					JOptionPane.INFORMATION_MESSAGE);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(getJFrame(), 
					String.format("Failed to collect files: %s", e.getMessage()),
					"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void updateCachedFiles(ArrayList<CachedFile> files){
		//getJContentPane().setEnabled(true);
		getFilesListPanel().setEnabled(true);
		getFilesListPanel().init(files);
		progressDialog.setVisible(false);
	}
	
	private SettingsDialog settingsDialog;
	private SettingsDialog getSettingsDialog(){
		if(settingsDialog == null){
			settingsDialog = new SettingsDialog(this.getJFrame());
			//settingsDialog.pack();
			settingsDialog.addSaveActionListener(this);
		}
		settingsDialog.setLocationRelativeTo(getJContentPane());
		return settingsDialog;
	}
	
	private void editSettings(){
		SettingsDialog dlg = getSettingsDialog();
		dlg.setSettings(getSettings());
		dlg.setVisible(true);
	}

	/**
	 * @return the settings
	 * @throws IOException 
	 */
	public Settings getSettings() {
		if(settings == null){
			settings = Settings.load();
		}
		
		return settings;
	}

	/**
	 * @param settings the settings to set
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == Settings.COMMAND_SAVE_SETTINGS){
			setSettings(getSettingsDialog().getSettings());
			try {
				//getSettings().save();
				Settings.save(getSettings());
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(getJFrame(), 
						String.format("Failed to save settings to '%s':\n\n%s", Settings.getSettingsFilePath(), e1.getMessage()), 
						"", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}	
	
	private int copyFiles(String targetDir){
		int copied = 0;
		
		for(CachedFile file: getFilesListPanel().getCachedFiles()){
			if(file.isSelectedToCopy()){
				String targetFile = targetDir + File.separator + file.getName();
				if(file.guessExtension() != null){
					targetFile = targetFile + "." + file.guessExtension(); 
				}
				try {
					CachedFile.copyFile(file.getAbsolutePath(), targetFile);
					
					// TODO: java 7
					//Files.copy(file.getAbsolutePath(), targetFile, );
					
					copied++;
				} catch (IOException e) {
					String msg = String.format("Failed to copy file from 's' to 's':\n\n%s\n\n" +
							"Continue to copy other selected files, if present?", e.getMessage());
					if(JOptionPane.NO_OPTION ==
					JOptionPane.showConfirmDialog(getJFrame(), 
							msg, 
							"Failed to copy", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.ERROR_MESSAGE)){
						break;
					}
				}
			}
		}
		
		return copied;
	}

	private void exit(){
		// save window position
		JFrame mainWindow = getJFrame();
		try {
			getSettings().setIntProperty(Settings.WINDOW_X, mainWindow.getX());
			getSettings().setIntProperty(Settings.WINDOW_Y, mainWindow.getY());
			getSettings().setIntProperty(Settings.WINDOW_W, mainWindow.getWidth());
			getSettings().setIntProperty(Settings.WINDOW_H, mainWindow.getHeight());
			getSettings().setIntProperty(Settings.SPLITTER_POS, getJSplitPaneMain().getDividerLocation());
			//getSettings().save();
			Settings.save(getSettings());
		} catch (IOException e) {			
			e.printStackTrace();
		}
				
		System.exit(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(FileSearchProgressDialog.SEARCH_COMPLETE)){
			progressDialog.setVisible(false);
			getFilesListPanel().setEnabled(true);
			getFilesListPanel().init((List<CachedFile>) evt.getNewValue());			
		}
		else if(evt.getPropertyName().equals(AudioPreviewPanel.PROPERTY_FILE_TO_PLAY)
				|| evt.getPropertyName().equals(VideoPreviewPanel.PROPERTY_FILE_TO_PLAY)){
			
			CachedFile f = (CachedFile)evt.getNewValue();

			doExternalPlayer(f);			
		}
		
	}
	
	private void doExternalPlayer(final CachedFile f) {		
		// TODO: run in background
		if(getSettings().isExternalPlayerConfigured()){
			SwingWorker<Void, Void> w = new SwingWorker<Void, Void>(){

				@Override
				protected Void doInBackground() throws Exception {
					final String cmdLine = getSettings().getExternalPlayerCommand();
					
//final String cmdLine = "\"c:\\tmp\\sub dir\\runq.cmd\" %f";
//cmdLine = "f:\\bin32\\vlc\\flc.exe %f";
					
					// http://www.regexplanet.com/simple/index.html
					Pattern p = Pattern.compile("\"[^\"]+\"|[^\"\\s]+");					
					Matcher m = p.matcher(cmdLine);
					ArrayList<String> tokens = new ArrayList<String>();
					while(m.find()){
						tokens.add(m.group());
					}
					
					if(tokens.size() == 0){
						SwingUtilities.invokeLater(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								JOptionPane.showMessageDialog(WebCacheDigger.this.getJContentPane(),
										String.format("Failed to parse command line definition:\n\n%s", cmdLine)
										);								
							}});

						return null;
					}
					
					String[] args = new String[tokens.size()];
					for(int i = 0; i<tokens.size(); i++){
						if(tokens.get(i).startsWith("\"")){
							args[i] = tokens.get(i).substring(1, tokens.get(i).length()-1);
							//args[i] = tokens.get(i);
						}
						else if(tokens.get(i).equals(Settings.EXT_PLAYER_FILEPATH)){
							args[i] = f.getAbsolutePath();
//args[i] = "\"c:\\tmp\\sub dir\\1.mp3\""; 							
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
									// TODO Auto-generated method stub
									JOptionPane.showMessageDialog(WebCacheDigger.this.getJContentPane(), 
											String.format("External player failed with exit code %d", exitCode));
									
								}});
						}
						
					}
					catch(final Exception ex){
						SwingUtilities.invokeLater(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								JOptionPane.showMessageDialog(WebCacheDigger.this.getJContentPane(), 
										String.format("External player failed:\n\n%s", ex.getMessage()));
								
							}});						
					}
					
					
					return null;
				}
				
			};
			
			w.execute();
		}
		
//		String cmdLine;
//		try {
//			
//			String fmt = cmdLine.replaceAll(Settings.EXT_PLAYER_FILEPATH, "%s");
//			cmdLine = String.format(fmt, f.getAbsolutePath());
//			
//			SimpleLogger.logMessage(cmdLine);
//		    @SuppressWarnings("unused")
//			Process process = Runtime.getRuntime().exec(cmdLine);        
//		    //process.waitFor();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}			

	}

	private LinkedHashSet<IBrowser> existingBrowsers = null;
	private JMenu mnLanguage;
	private JMenuItem mntmEnglish;
	public synchronized LinkedHashSet<IBrowser> getExistingBrowsers(){
		if(existingBrowsers == null){
			existingBrowsers = new LinkedHashSet<IBrowser>();
			
			ServiceLoader<IBrowser> ldr = ServiceLoader.load(IBrowser.class);
			for(IBrowser browser : ldr){
				browser.setSettings(getSettings());
				SimpleLogger.logMessage("Can handle " + browser.getName());
				if(browser.isPresent()){
					existingBrowsers.add(browser);
					SimpleLogger.logMessage("Found " + browser.getName());					
				}
//				existingBrowsers.add(browser);
								
			}
			
		}
		
		return existingBrowsers;
	}
	private JMenu getMnLanguage() {
		if (mnLanguage == null) {
			mnLanguage = new JMenu("Language");
			mnLanguage.add(getMntmEnglish());
		}
		return mnLanguage;
	}
	private JMenuItem getMntmEnglish() {
		if (mntmEnglish == null) {
			mntmEnglish = new JMenuItem("English");
			mntmEnglish.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doLanguageSelected(e);
				}
			});
		}
		return mntmEnglish;
	}

	protected void doLanguageSelected(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
