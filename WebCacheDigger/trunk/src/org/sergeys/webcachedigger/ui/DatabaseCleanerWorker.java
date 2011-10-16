package org.sergeys.webcachedigger.ui;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.sergeys.webcachedigger.logic.Database;

public class DatabaseCleanerWorker
extends SwingWorker<List<File>, Integer>
{

	@Override
	protected void done() {
		// TODO Auto-generated method stub
		super.done();
		
		try {
			Database.getInstance().removeByName(get());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected List<File> doInBackground() throws Exception {
		ArrayList<File> nonexistent = new ArrayList<File>();
		
		List<File> files = Database.getInstance().getNotSavedFiles();
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
