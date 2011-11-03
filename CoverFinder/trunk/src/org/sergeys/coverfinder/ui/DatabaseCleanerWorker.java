package org.sergeys.coverfinder.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.sergeys.coverfinder.logic.Database;


public class DatabaseCleanerWorker 
extends SwingWorker<List<File>, Integer>
{

	@Override
	protected void done() {
		// TODO Auto-generated method stub
		super.done();
		
		try {
			Database.getInstance().deleteByAbsolutePath(get());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	@Override
	protected List<File> doInBackground() throws Exception {
		ArrayList<File> nonexistent = new ArrayList<File>();
		
		List<File> files = Database.getInstance().selectAllFiles();
		for(File file: files){
			
			if(isCancelled()){
				return null;
			}
			
			if(!file.exists()){
				nonexistent.add(file);
			}
		}
		
		return nonexistent;
	}

}