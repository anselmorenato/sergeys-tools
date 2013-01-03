package org.sergeys.gpublish.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.sergeys.gpublish.logic.Settings;

public class MainWindow {

    private JFrame frame;
    private JTextField textField;
    private JTextField textField_1;

    /**
     * Create the application.
     */
    public MainWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        Settings.getLogger().debug("main window init");

        frame = new JFrame();
        frame.setBounds(
        		Settings.getInstance().getWinPosition().width,
        		Settings.getInstance().getWinPosition().height,
        		Settings.getInstance().getWinSize().width,
        		Settings.getInstance().getWinSize().height);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	// http://stackoverflow.com/questions/258099/how-to-close-a-java-swing-application-from-the-code        
        
             
        frame.addWindowListener(new WindowAdapter() {
        	@Override
            public void windowClosing(WindowEvent e) {             
                doExit();
            }
		});
        
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doExit();
            }
        });
        mnFile.add(mntmExit);

        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        JMenuItem mntmAbout = new JMenuItem("About ...");
        mntmAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doAbout();
            }
        });
        mnHelp.add(mntmAbout);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panelTop = new JPanel();
        frame.getContentPane().add(panelTop, BorderLayout.NORTH);
        GridBagLayout gbl_panelTop = new GridBagLayout();
        gbl_panelTop.columnWidths = new int[] {0, 0, 0};
        //gbl_panelTop.rowHeights = new int[] {0, 0, 0};
        gbl_panelTop.columnWeights = new double[]{1.0, 2.0, 1.0};
        gbl_panelTop.rowWeights = new double[]{1.0, 1.0, 1.0};
        panelTop.setLayout(gbl_panelTop);

        JLabel lblNewLabel = new JLabel("Images folder:");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        panelTop.add(lblNewLabel, gbc_lblNewLabel);

        textField = new JTextField();
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.fill = GridBagConstraints.BOTH;
        gbc_textField.insets = new Insets(0, 0, 5, 5);
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 0;
        panelTop.add(textField, gbc_textField);
        textField.setColumns(10);

        JButton button = new JButton("...");
        GridBagConstraints gbc_button = new GridBagConstraints();
        gbc_button.anchor = GridBagConstraints.WEST;
        gbc_button.insets = new Insets(0, 0, 5, 0);
        gbc_button.gridx = 2;
        gbc_button.gridy = 0;
        panelTop.add(button, gbc_button);

        JLabel lblWebFolder = new JLabel("Web folder:");
        GridBagConstraints gbc_lblWebFolder = new GridBagConstraints();
        gbc_lblWebFolder.anchor = GridBagConstraints.EAST;
        gbc_lblWebFolder.insets = new Insets(0, 0, 0, 5);
        gbc_lblWebFolder.fill = GridBagConstraints.VERTICAL;
        gbc_lblWebFolder.gridx = 0;
        gbc_lblWebFolder.gridy = 2;
        panelTop.add(lblWebFolder, gbc_lblWebFolder);

        textField_1 = new JTextField();
        GridBagConstraints gbc_textField_1 = new GridBagConstraints();
        gbc_textField_1.insets = new Insets(0, 0, 0, 5);
        gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_1.gridx = 1;
        gbc_textField_1.gridy = 2;
        panelTop.add(textField_1, gbc_textField_1);
        textField_1.setColumns(10);

        JPanel panelCenter = new JPanel();
        frame.getContentPane().add(panelCenter, BorderLayout.CENTER);
        panelCenter.setLayout(new BorderLayout(0, 0));

        JTextPane textPane = new JTextPane();
        panelCenter.add(textPane);

        JPanel panelBottom = new JPanel();
        frame.getContentPane().add(panelBottom, BorderLayout.SOUTH);

        JButton btnClipboard = new JButton("Copy to clipboard");
        panelBottom.add(btnClipboard);
    }

    protected void doAbout() {
        // TODO Auto-generated method stub

    }

    protected void doExit() {
        Settings.getLogger().debug("application exit");
        
        Settings.getInstance().setWinPosition(new Dimension(frame.getX(), frame.getY()));
        Settings.getInstance().setWinSize(new Dimension(frame.getWidth(), frame.getHeight()));
        
        try {
			Settings.save();
		} catch (FileNotFoundException e) {
			Settings.getLogger().error("failed to save settings", e);
		}
        
        frame.setVisible(false);
        frame.dispose();
        System.exit(0);
    }

    public JFrame getFrame(){
        return frame;
    }

}
