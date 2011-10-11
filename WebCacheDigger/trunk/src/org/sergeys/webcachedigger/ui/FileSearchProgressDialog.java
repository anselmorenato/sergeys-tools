package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.Settings;

public class FileSearchProgressDialog 
extends JDialog 
{

	// property names 
	public static final String SEARCH_COMPLETE = "SEARCH_COMPLETE";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FileCollectorWorker worker;
	Settings settings;
	HashSet<IBrowser> existingBrowsers;
	
	JLabel lblCount;
	JLabel lblFilesFound;
	
	private String[] stageLabel = {
			"Files found:",
			"Analyzed:"
			// http://www.grammarist.com/spelling/analyse-analyze/
			// Analyse is the preferred spelling in British and Australian English, 
			// while analyze is preferred in American and Canadian English
			
	};
	private int stage;
	
	/**
	 * Create the dialog.
	 * @param existingBrowsers 
	 */
	public FileSearchProgressDialog(Settings settings, HashSet<IBrowser> existingBrowsers) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(FileSearchProgressDialog.class.getResource("/images/icon.png")));

		this.settings = settings;
		this.existingBrowsers = existingBrowsers;
		
		setTitle("Search files");
		setModal(true);
		setBounds(100, 100, 350, 129);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 50, 0, 0));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		lblFilesFound = new JLabel("Files found:");
		panel.add(lblFilesFound);
		
		JLabel lblSpace = new JLabel(" ");
		panel.add(lblSpace);
		
		lblCount = new JLabel("0");
		lblFilesFound.setLabelFor(lblCount);
		lblCount.setHorizontalAlignment(SwingConstants.LEFT);
		lblCount.setPreferredSize(new Dimension(100, 20));
		panel.add(lblCount);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
		panel_1.add(btnCancel);
		
		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.WEST);
		
		JLabel lblProgressGif = new JLabel("");
		lblProgressGif.setIcon(new ImageIcon(FileSearchProgressDialog.class.getResource("/images/progress.gif")));

		panel_2.add(lblProgressGif);
						
	}

	protected void doCancel() {
		this.setVisible(false);
	}

	@Override	
	public void setVisible(boolean visible) {
		if(visible){
			stage = -1;
			updateProgress(0, 0);
			startWork();
		}
		
		super.setVisible(visible);
	}

	private void startWork() {
		
		ArrayList<IBrowser> browsers = new ArrayList<IBrowser>();
		for(IBrowser b: existingBrowsers){
			if(settings.getActiveBrowsers().contains(b.getName())){
				browsers.add(b);
			}
		}
		this.worker = new FileCollectorWorker(browsers, this, settings);
		this.worker.execute();
		
	}
		
	public void updateProgress(long count, int stage){
		lblCount.setText(String.valueOf(count));
		if(stage != this.stage){
			this.stage = stage;
			lblFilesFound.setText(stageLabel[this.stage]);
		}
	}
	
	public void searchComplete(ArrayList<CachedFile> files){
		this.worker = null;

		firePropertyChange(FileSearchProgressDialog.SEARCH_COMPLETE, null, files);		
	}
}
