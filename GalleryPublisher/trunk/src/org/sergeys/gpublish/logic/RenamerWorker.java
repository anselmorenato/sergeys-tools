package org.sergeys.gpublish.logic;

import java.io.File;

import javax.swing.SwingWorker;

import org.sergeys.gpublish.ui.MainWindow;

public class RenamerWorker extends SwingWorker<String, Integer> {

	private MainWindow mainWindow;
	private StringBuilder sbHtml = new StringBuilder();
	
	public RenamerWorker(MainWindow mainWindow){
		this.mainWindow = mainWindow;
	}
	
	@Override
	protected String doInBackground() throws Exception {
				
		//File wpSrcFolder = new File(Settings.getInstance().getSrcWallpapersFolder());
		
		sbHtml.append("dfvs svdffv");
		
		return sbHtml.toString();
	}

	@Override
	protected void done() {
		super.done();
		
		mainWindow.getTextPaneHtml().setText(sbHtml.toString());
	}

}
