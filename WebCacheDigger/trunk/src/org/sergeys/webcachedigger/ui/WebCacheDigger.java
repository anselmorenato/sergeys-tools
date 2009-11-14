package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.FileCollector;
import org.sergeys.webcachedigger.logic.Firefox;
import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.InternetExplorer;
import org.sergeys.webcachedigger.logic.Settings;

public class WebCacheDigger implements ActionListener {

	private JFrame jFrame = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private JPanel jContentPane = null;
	private JMenuBar jJMenuBar = null;
	private JMenu fileMenu = null;
	private JMenu editMenu = null;
	private JMenu helpMenu = null;
	private JMenuItem exitMenuItem = null;
	private JMenuItem aboutMenuItem = null;
	private JMenuItem cutMenuItem = null;
	private JMenuItem copyMenuItem = null;
	private JMenuItem pasteMenuItem = null;
	private JMenuItem saveMenuItem = null;
	
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
			jButtonCopySelectedFiles.setText("Copy Selected");
			jButtonCopySelectedFiles.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					WebCacheDigger.this.showNotImplemented();
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
					WebCacheDigger.this.doSearch();
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
			jPanelFileDetails = new FileDetailsPanel();
			
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				WebCacheDigger application = new WebCacheDigger();
				
				JFrame mainWindow = application.getJFrame(); 
				
				// TODO: set size and position of main window here 
				
				Dimension desktop = Toolkit.getDefaultToolkit().getScreenSize();
				
				mainWindow.setLocation((desktop.width-mainWindow.getWidth())/2,
						(desktop.height-mainWindow.getHeight())/2);				
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
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setJMenuBar(getJJMenuBar());
			jFrame.setSize(649, 402);
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
			jContentPane.setLayout(new BorderLayout());
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
			jJMenuBar.add(getEditMenu());
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
			fileMenu.add(getSaveMenuItem());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.add(getCutMenuItem());
			editMenu.add(getCopyMenuItem());
			editMenu.add(getPasteMenuItem());
			editMenu.add(getJMenuItemSettings());
		}
		return editMenu;
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
					System.exit(0);
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

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getCutMenuItem() {
		if (cutMenuItem == null) {
			cutMenuItem = new JMenuItem();
			cutMenuItem.setText("Cut");
			cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					Event.CTRL_MASK, true));
		}
		return cutMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getCopyMenuItem() {
		if (copyMenuItem == null) {
			copyMenuItem = new JMenuItem();
			copyMenuItem.setText("Copy");
			copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					Event.CTRL_MASK, true));
		}
		return copyMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getPasteMenuItem() {
		if (pasteMenuItem == null) {
			pasteMenuItem = new JMenuItem();
			pasteMenuItem.setText("Paste");
			pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
					Event.CTRL_MASK, true));
		}
		return pasteMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getSaveMenuItem() {
		if (saveMenuItem == null) {
			saveMenuItem = new JMenuItem();
			saveMenuItem.setText("Save");
			saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					Event.CTRL_MASK, true));
		}
		return saveMenuItem;
	}

	private JDialog getAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new AboutDialog(this.getJFrame());
			
		}
		return aboutDialog;
	}
	
	private void showNotImplemented(){
		JOptionPane.showMessageDialog(this.getJFrame(), "Not implemented yet.", "Warning", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void doSearch(){					
		
		try {
			
			//List<String> paths = browser.getDefaultCachePaths();
			ArrayList<IBrowser> browsers = new ArrayList<IBrowser>();
			browsers.add(new Firefox());
			browsers.add(new InternetExplorer());
			FileCollector fileCollector = new FileCollector(browsers);
			List<CachedFile> files = fileCollector.collect();
			getFilesListPanel().init(files);
			
			// http://www.medsea.eu/mime-util/detectors.html
			//MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
			//Collection mt = MimeUtil.getMimeTypes(files.get(0));
			
			String msg = String.format("Total files: %d", files.size());
			//msg = msg + "\n" + mt;
			
			JOptionPane.showMessageDialog(getJFrame(), 					 
					msg,
					"Message", 
					JOptionPane.INFORMATION_MESSAGE);
			
		} catch (Exception e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(getJFrame(), 
					//String.format("Failed to collect files: %1$s", e.getMessage()), 
					String.format("Failed to collect files: %s", e.getMessage()),
					"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
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
	
	private void editSettings(){
		SettingsDialog dlg = getSettingsDialog();
		try {
			dlg.setSettings(getSettings());
		} catch (IOException e) {

			JOptionPane.showMessageDialog(getJFrame(), 
					String.format("Failed to load settings from '%s':\n\n%s", 
							Settings.getSettingsFilePath(), e.getMessage()), 
					"", 
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		dlg.setVisible(true);
	}

	/**
	 * @return the settings
	 * @throws IOException 
	 */
	public Settings getSettings() throws IOException {
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
		// TODO Auto-generated method stub
		if(e.getActionCommand() == Settings.COMMAND_SAVE_SETTINGS){
			//JOptionPane.showMessageDialog(getJFrame(), "save settings");
			setSettings(getSettingsDialog().getSettings());
			try {
				getSettings().save();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				JOptionPane.showMessageDialog(getJFrame(), 
						String.format("Failed to save settings to '%s':\n\n%s", Settings.getSettingsFilePath(), e1.getMessage()), 
						"", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}	
}
