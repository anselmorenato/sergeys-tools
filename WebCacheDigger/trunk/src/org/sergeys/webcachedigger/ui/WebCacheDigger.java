package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
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
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.sergeys.webcachedigger.logic.FileCollector;
import org.sergeys.webcachedigger.logic.Firefox;
import org.sergeys.webcachedigger.logic.IBrowser;

public class WebCacheDigger {

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
			jPanelFoundFilesActions.setLayout(new GridBagLayout());
			jPanelFoundFilesActions.add(getJButtonCopySelectedFiles(), new GridBagConstraints());
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					//JOptionPane.showMessageDialog(getJFrame(), "not implemented", "warning", JOptionPane.INFORMATION_MESSAGE);
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
			jPanelTop.setLayout(new GridBagLayout());
			jPanelTop.add(getJButtonSearch(), new GridBagConstraints());
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
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					//showNotImplemented();
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
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				WebCacheDigger application = new WebCacheDigger();
				application.getJFrame().setVisible(true);
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
			jFrame.setTitle("Application");
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
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialog aboutDialog = getAboutDialog();
					aboutDialog.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					aboutDialog.setLocation(loc);
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
			IBrowser browser = new Firefox();
			//List<String> paths = browser.getDefaultCachePaths();
			ArrayList<IBrowser> browsers = new ArrayList<IBrowser>();
			browsers.add(new Firefox());
			FileCollector fileCollector = new FileCollector(browsers);
			List<File> files = fileCollector.collect();
			/*
			JOptionPane.showMessageDialog(getJFrame(), 
					//String.format("Failed to collect files: %1$s", e.getMessage()), 
					String.format("Paths:\n\n%s", msg),
					"Message", 
					JOptionPane.INFORMATION_MESSAGE);*/
			getFilesListPanel().init(files);
		} catch (Exception e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(getJFrame(), 
					//String.format("Failed to collect files: %1$s", e.getMessage()), 
					String.format("Failed to collect files: %s", e.getMessage()),
					"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
