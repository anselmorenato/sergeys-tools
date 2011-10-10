package org.sergeys.webcachedigger.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.Settings;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class FileSearchProgressDialog 
extends JDialog 
{

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
			"Identified:"
	};
	private int stage;
	
	/**
	 * Create the dialog.
	 * @param existingBrowsers 
	 */
	public FileSearchProgressDialog(Settings settings, HashSet<IBrowser> existingBrowsers) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(FileSearchProgressDialog.class.getResource("/images/icon.png")));
		//setIconImage(Toolkit.getDefaultToolkit().getImage(FileSearchProgressDialog.class.getResource("/images/progress.gif")));
		this.settings = settings;
		this.existingBrowsers = existingBrowsers;
		
		setTitle("Search files");
		setModal(true);
		setBounds(100, 100, 350, 129);

		//this.worker = worker;
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 50, 0, 0));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		lblFilesFound = new JLabel("Files found:");
		panel.add(lblFilesFound);
		
		lblCount = new JLabel("0");
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
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(FileSearchProgressDialog.class.getResource("/images/progress.gif")));
		//lblNewLabel.setIcon(new ImageIcon(FileSearchProgressDialog.class.getResource("/images/search.png")));
		panel_2.add(lblNewLabel);
				
		
	}

	protected void doCancel() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		ArrayList<IBrowser> browsers = new ArrayList<IBrowser>();
		for(IBrowser b: existingBrowsers){
			if(settings.getActiveBrowsers().contains(b.getName())){
				browsers.add(b);
			}
		}
		this.worker = new FileCollectorWorker(browsers, this);
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
		//setVisible(false);
		firePropertyChange("searchcompleted", null, files);		
	}
}
