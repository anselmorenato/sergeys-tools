package org.sergeys.cookbook.logic;

import java.io.File;
import java.io.FilenameFilter;

import org.sergeys.cookbook.logic.HtmlImporter.Status;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;

public class MassImportTask extends Task<Void> implements ChangeListener<HtmlImporter.Status>
{
	private File directory;
	private HtmlImporter importer;
	private boolean canContinue;
	
	private Object sync = new Object();
	
	public MassImportTask(File directory, HtmlImporter importer){
		this.directory = directory;
		this.importer = importer;
	}
	
	@Override
	protected Void call() throws Exception {
				
		importer.statusProperty().addListener(this);
		
		String[] files = directory.list(new FilenameFilter() {					
			@Override
			public boolean accept(File dir, String filename) {
				File f = new File(dir.getAbsolutePath() + File.separator + filename);
				return f.isFile() && (filename.toLowerCase().endsWith(".html") || filename.toLowerCase().endsWith(".htm"));
			}
		});
						
		System.out.println("> found to import " + files.length);
		
		int count = 0;
		for(final String file: files){
			updateProgress(count++, files.length);
						
			synchronized (sync) {
            	canContinue = false;;
            }
			System.out.println("> import " + file);
			
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {

					importer.Import(new File(directory.getAbsolutePath() + File.separator + file));
				}
			});
            //importer.Import(new File(directory.getAbsolutePath() + File.separator + file));
        
            boolean cont = false;
            int waitcount = 0;
            while(!cont){
	            synchronized (sync) {
	            	cont = canContinue;
	            }
	            
	            if(!cont){
	            	System.out.println("> waiting " + waitcount);
	            	Thread.sleep(500);
	            }
	            
	            waitcount++;
	            
	            if(waitcount > 15){
	            	break;
	            }
            }
		}
				
		return null;
	}

	@Override
	public void changed(ObservableValue<? extends Status> observable,
			Status oldValue, Status newValue) {
	
		//System.out.println("> status in task " + newValue);
		synchronized (sync) {
			if(newValue == Status.Complete || 
			   newValue == Status.AlreadyExist || 
			   newValue == Status.Failed){
				
				canContinue = true;
				//System.out.println("> can continue");
			}
			else{
				canContinue = false;
			}
		}
		
	}

}
