package org.sergeys.desktopreminder;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DeskReminder {

	private JFrame frmReminder;

	private AboutDialog aboutDialog;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeskReminder window = new DeskReminder();
					window.frmReminder.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DeskReminder() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmReminder = new JFrame();
		frmReminder.setTitle("Reminder");
		frmReminder.setBounds(100, 100, 450, 300);
		frmReminder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmReminder.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doShowAbout();
			}
		});
		mnHelp.add(mntmAbout);
	}

//	public void setAboutDialog(AboutDialog aboutDialog) {
//		this.aboutDialog = aboutDialog;
//	}

	public AboutDialog getAboutDialog() {
		if(aboutDialog == null){
			aboutDialog = new AboutDialog(this.frmReminder);
		}
		return aboutDialog;
	}

	private void doShowAbout(){
		Point loc = this.frmReminder.getLocation();
		loc.translate((this.frmReminder.getWidth() - getAboutDialog().getWidth())/2,
				(this.frmReminder.getHeight() - getAboutDialog().getHeight())/2);
		getAboutDialog().setLocation(loc);
		getAboutDialog().setVisible(true);
	}
}
