package org.sergeys.dupfinder.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.BorderLayout;

public class DuplicateFinder {

	private JFrame frmDuplicateFileFinder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					DuplicateFinder window = new DuplicateFinder();
					window.frmDuplicateFileFinder.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DuplicateFinder() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDuplicateFileFinder = new JFrame();
		frmDuplicateFileFinder.setTitle("Duplicate File Finder");
		frmDuplicateFileFinder.setBounds(100, 100, 450, 300);
		frmDuplicateFileFinder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmDuplicateFileFinder.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JMenu mnLanguage = new JMenu("Language");
		mnSettings.add(mnLanguage);
		
		JMenuItem mntmSettings = new JMenuItem("Settings ...");
		mnSettings.add(mntmSettings);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About ...");
		mnHelp.add(mntmAbout);
		
		MainPanel mainPanel = new MainPanel();
		frmDuplicateFileFinder.getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

}
