package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;

public class ProgressDialog 
extends JDialog 
{

	public enum WorkType { CollectFiles, CopyFiles };
	
	// property names 
	public static final String SEARCH_COMPLETE = "SEARCH_COMPLETE"; //$NON-NLS-1$
	public static final String COPY_COMPLETE = "COPY_COMPLETE"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private WorkType workType;
	
	//FileCollectorWorker worker;
	@SuppressWarnings("rawtypes")
	SwingWorker worker;
	//Settings settings;
	HashSet<IBrowser> existingBrowsers;
	
	JLabel lblCount;
	JLabel lblMessage;
	
	// collector
	public static final int STAGE_COLLECT = 0;
	public static final int STAGE_FILTER_TYPE = 1;
	public static final int STAGE_FILTER_HASH = 2;
	// copier
	public static final int STAGE_COPY = 3;
	
	private String[] stageLabel = {
			Messages.getString("FileSearchProgressDialog.filesFound"), //$NON-NLS-1$
			Messages.getString("ProgressDialog.AnalyzingTypes"), //$NON-NLS-1$
			Messages.getString("ProgressDialog.CheckIfSaved"), //$NON-NLS-1$
			// http://www.grammarist.com/spelling/analyse-analyze/
			// Analyse is the preferred spelling in British and Australian English, 
			// while analyze is preferred in American and Canadian English
			Messages.getString("ProgressDialog.Copied") //$NON-NLS-1$
	};
	private int stage;
	
	// vars for file copying
	private List<CachedFile> filesToCopy;
	private String targetDir;
	private List<CachedFile> succesfullyCopied;
	
	/**
	 * Create the dialog.
	 * @param existingBrowsers 
	 */
	public ProgressDialog(HashSet<IBrowser> existingBrowsers) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ProgressDialog.class.getResource("/images/icon.png"))); //$NON-NLS-1$

		//this.settings = settings;
		this.existingBrowsers = existingBrowsers;
		
		setTitle(Messages.getString("FileSearchProgressDialog.searchFiles")); //$NON-NLS-1$
		setModal(true);
		setBounds(100, 100, 350, 129);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 50, 0, 0));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		lblMessage = new JLabel("Files found:"); //$NON-NLS-1$
		panel.add(lblMessage);
		
		JLabel lblSpace = new JLabel(" "); //$NON-NLS-1$
		panel.add(lblSpace);
		
		lblCount = new JLabel("0"); //$NON-NLS-1$
		lblMessage.setLabelFor(lblCount);
		lblCount.setHorizontalAlignment(SwingConstants.LEFT);
		lblCount.setPreferredSize(new Dimension(100, 20));
		panel.add(lblCount);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton(Messages.getString("FileSearchProgressDialog.cancel")); //$NON-NLS-1$
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
		panel_1.add(btnCancel);
		
		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.WEST);
		
		JLabel lblProgressGif = new JLabel(""); //$NON-NLS-1$
		lblProgressGif.setIcon(new ImageIcon(ProgressDialog.class.getResource("/images/progress.gif"))); //$NON-NLS-1$

		panel_2.add(lblProgressGif);
						
	}

	protected void doCancel() {
		if(worker != null){
			worker.cancel(true);
			worker = null;
		}				
		firePropertyChange(ProgressDialog.SEARCH_COMPLETE, null, null);
		this.setVisible(false);
	}

	
	@Override
	public void setVisible(boolean visible) {
		if(visible){
			startWork();
		}
		
		super.setVisible(visible);
	}

	private void startWork() {
		
		stage = -1;
		
		updateProgress(0, 0);

		switch(this.workType){
		case CollectFiles:
			ArrayList<IBrowser> browsers = new ArrayList<IBrowser>();
			for(IBrowser b: existingBrowsers){
				if(Settings.getInstance().getActiveBrowsers().contains(b.getName())){
					browsers.add(b);
				}
			}
			this.worker = new FileCollectorWorker(browsers, this);
			this.worker.execute();
			break;
			
		case CopyFiles:
			this.worker = new FileCopyWorker(getFilesToCopy(), getTargetDir(), this);
			this.worker.execute();
			break;
		}
						
	}
		
	
	public void updateProgress(long count, int stage){
		lblCount.setText(String.valueOf(count));
		
		if(stage != this.stage){
			this.stage = stage;
			
			lblMessage.setText(stageLabel[this.stage]);
		}
	}
	
	public void searchComplete(ArrayList<CachedFile> files){
		this.worker = null;
		lblMessage.setText(""); //$NON-NLS-1$
		firePropertyChange(ProgressDialog.SEARCH_COMPLETE, null, files);		
	}
	
	public void copyingComplete(List<CachedFile> list){
		this.worker = null;
		lblMessage.setText(""); //$NON-NLS-1$
		this.succesfullyCopied = list;
		firePropertyChange(ProgressDialog.COPY_COMPLETE, null, list);				
	}

	public WorkType getWorkType() {
		return workType;
	}

	public void setWorkType(WorkType workType) {
		this.workType = workType;
		
		switch(workType){
			case CollectFiles:
				this.setTitle(Messages.getString("ProgressDialog.SearchFiles")); //$NON-NLS-1$
				break;
			case CopyFiles:
				this.setTitle(Messages.getString("ProgressDialog.CopyFiles")); //$NON-NLS-1$
				break;
		}
	}

	public List<CachedFile> getFilesToCopy() {
		return filesToCopy;
	}

	public void setFilesToCopy(List<CachedFile> filesToCopy) {
		this.filesToCopy = filesToCopy;
	}

	public List<CachedFile> getSuccesfullyCopied() {
		return succesfullyCopied;
	}
	
	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}
}
