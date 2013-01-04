package org.sergeys.gpublish.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import org.sergeys.gpublish.logic.Settings;

public class MainWindow implements ClipboardOwner {

    private JFrame frame;
    private JTextField textFieldImagesFolder;
    private JTextField textFieldWebFolder;
    private JTextPane textPaneHtml;

    /**
     * Create the application.
     */
    public MainWindow() {
    	try{
    		initialize();
    	}
    	catch(Exception ex){
    		Settings.getLogger().error("failed to init main window", ex);
    	}
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        Settings.getLogger().debug("main window init");

        // looks like old name is used in 1.6 on macosx and this not works
        //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    	
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception ex) {
			Settings.getLogger().error("failed to set lookandfeel", ex);
		}		        
        
        frame = new JFrame();
        frame.setTitle("Gallery Publisher");

        // see main()
//        frame.setBounds(
//        		Settings.getInstance().getWinPosition().width,
//        		Settings.getInstance().getWinPosition().height,
//        		Settings.getInstance().getWinSize().width,
//        		Settings.getInstance().getWinSize().height);
        
        frame.setBounds(0, 0, 500, 300);	// for eclipse windowbuilder editor        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	// http://stackoverflow.com/questions/258099/how-to-close-a-java-swing-application-from-the-code        
        
             
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/BPCameraCherry.png")));
        
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
        gbl_panelTop.rowWeights = new double[]{1.0, 1.0, 1.0, 0.0};
        panelTop.setLayout(gbl_panelTop);

        JLabel lblNewLabel = new JLabel("Images folder:");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        panelTop.add(lblNewLabel, gbc_lblNewLabel);

        textFieldImagesFolder = new JTextField();
        GridBagConstraints gbc_textFieldImagesFolder = new GridBagConstraints();
        gbc_textFieldImagesFolder.fill = GridBagConstraints.BOTH;
        gbc_textFieldImagesFolder.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldImagesFolder.gridx = 1;
        gbc_textFieldImagesFolder.gridy = 0;
        panelTop.add(textFieldImagesFolder, gbc_textFieldImagesFolder);
        textFieldImagesFolder.setColumns(10);

        JButton btnSelectImageFolder = new JButton("...");
        btnSelectImageFolder.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		doSelectImageFolder();
        	}
        });
        GridBagConstraints gbc_btnSelectImageFolder = new GridBagConstraints();
        gbc_btnSelectImageFolder.anchor = GridBagConstraints.WEST;
        gbc_btnSelectImageFolder.insets = new Insets(0, 0, 5, 0);
        gbc_btnSelectImageFolder.gridx = 2;
        gbc_btnSelectImageFolder.gridy = 0;
        panelTop.add(btnSelectImageFolder, gbc_btnSelectImageFolder);

        JLabel lblWebFolder = new JLabel("Web folder:");
        GridBagConstraints gbc_lblWebFolder = new GridBagConstraints();
        gbc_lblWebFolder.anchor = GridBagConstraints.EAST;
        gbc_lblWebFolder.insets = new Insets(0, 0, 5, 5);
        gbc_lblWebFolder.fill = GridBagConstraints.VERTICAL;
        gbc_lblWebFolder.gridx = 0;
        gbc_lblWebFolder.gridy = 2;
        panelTop.add(lblWebFolder, gbc_lblWebFolder);

        textFieldWebFolder = new JTextField();
        GridBagConstraints gbc_textFieldWebFolder = new GridBagConstraints();
        gbc_textFieldWebFolder.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldWebFolder.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldWebFolder.gridx = 1;
        gbc_textFieldWebFolder.gridy = 2;
        panelTop.add(textFieldWebFolder, gbc_textFieldWebFolder);
        textFieldWebFolder.setColumns(10);
        
        JButton btnGenerateHtml = new JButton("Generate HTML");
        btnGenerateHtml.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		doGenerateHtml();
        	}
        });
        GridBagConstraints gbc_btnGenerateHtml = new GridBagConstraints();
        gbc_btnGenerateHtml.insets = new Insets(0, 0, 0, 5);
        gbc_btnGenerateHtml.gridx = 1;
        gbc_btnGenerateHtml.gridy = 3;
        panelTop.add(btnGenerateHtml, gbc_btnGenerateHtml);

        JPanel panelCenter = new JPanel();
        frame.getContentPane().add(panelCenter, BorderLayout.CENTER);
        panelCenter.setLayout(new BorderLayout(0, 0));

        textPaneHtml = new JTextPane();
        panelCenter.add(textPaneHtml);

        JPanel panelBottom = new JPanel();
        frame.getContentPane().add(panelBottom, BorderLayout.SOUTH);

        JButton btnClipboard = new JButton("Copy to clipboard");
        btnClipboard.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		doCopyToClipboard();
        	}
        });
        
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		doClear();
        	}
        });
        panelBottom.add(btnClear);
        panelBottom.add(btnClipboard);
        
        textFieldImagesFolder.setText(Settings.getInstance().getLastImagesFolder());
        textFieldWebFolder.setText(Settings.getInstance().getLastWebFolder());
    }

    protected void doClear() {
    	textPaneHtml.setText("");		
	}

	private DirSelectorDialog dirSelector;	
    
    protected void doSelectImageFolder() {
        if(dirSelector == null){
        	dirSelector = new DirSelectorDialog(frame);
        	dirSelector.addPropertyChangeListener(DirSelectorDialog.DIRECTORY_SELECTED, new PropertyChangeListener(){

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if(evt.getPropertyName().equals(DirSelectorDialog.DIRECTORY_SELECTED)){
                    	textFieldImagesFolder.setText(evt.getNewValue().toString());
                    }
                }});

        	dirSelector.setLocationRelativeTo(frame);
        }
        
        dirSelector.setVisible(true);		    			
	}

	protected void doCopyToClipboard() {
		StringSelection stringSelection = new StringSelection(textPaneHtml.getText());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(stringSelection, this);		
	}

	protected void doGenerateHtml() {
		textPaneHtml.setText("here goes generated html");		
	}

	protected void doAbout() {
        JOptionPane.showMessageDialog(frame, "TODO: about");
    }

    protected void doExit() {
        Settings.getLogger().debug("application exit");
        
        Settings.getInstance().setWinPosition(new Dimension(frame.getX(), frame.getY()));
        Settings.getInstance().setWinSize(new Dimension(frame.getWidth(), frame.getHeight()));
        
        Settings.getInstance().setLastImagesFolder(textFieldImagesFolder.getText());
        Settings.getInstance().setLastWebFolder(textFieldWebFolder.getText());
        
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

	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		// do nothing		
	}

}
