package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
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
					int count = WebCacheDigger.this.copyFiles();
					String msg = String.format("Copied %d file(s).", count);
					
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
			
			//getFilesListPanel().addPropertyChangeListener("selectedfile", getJPanelFileDetails());
			getFilesListPanel().addPropertyChangeListener("selectedfile", (PropertyChangeListener)getJPanelFileDetails());
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

	private JDialog getAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new AboutDialog(this.getJFrame());
			
		}
		return aboutDialog;
	}
	
	private void searchCachedFiles(){					
		
		try {
			ArrayList<IBrowser> browsers = new ArrayList<IBrowser>();
			browsers.add(new Firefox());
			browsers.add(new InternetExplorer());
			FileCollector fileCollector = new FileCollector(browsers);
			List<CachedFile> files = fileCollector.collect(getSettings());
			getFilesListPanel().init(files);
			
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
		if(e.getActionCommand() == Settings.COMMAND_SAVE_SETTINGS){
			setSettings(getSettingsDialog().getSettings());
			try {
				getSettings().save();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(getJFrame(), 
						String.format("Failed to save settings to '%s':\n\n%s", Settings.getSettingsFilePath(), e1.getMessage()), 
						"", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}	
	
	private int copyFiles(){
		int copied = 0;
		String targetDir = settings.getProperty(Settings.SAVE_TO_PATH);
		for(CachedFile file: getFilesListPanel().getCachedFiles()){
			if(file.isSelectedToCopy()){
				String targetFile = targetDir + File.separator + file.getName();
				if(file.guessExtension() != null){
					targetFile = targetFile + "." + file.guessExtension(); 
				}
				try {
					CachedFile.copyFile(file.getAbsolutePath(), targetFile);
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

}
