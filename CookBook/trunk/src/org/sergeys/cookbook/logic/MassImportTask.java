package org.sergeys.cookbook.logic;

import java.io.File;
import java.io.FilenameFilter;

import org.sergeys.cookbook.logic.HtmlImporter.Status;

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
						
		System.out.println("found to import " + files.length);
		
		int count = 0;
		for(String file: files){
			updateProgress(count++, files.length);
			System.out.println(file);
			canContinue = false;
			
			System.out.println("import...");
            importer.Import(new File(directory.getAbsolutePath() + File.separator + file));
        
            boolean cont = false;
            synchronized (sync) {
            	cont = canContinue;
            }
            
            if(!cont){
            	System.out.println("waiting...");
            	Thread.sleep(500);
            }
		}
				
		return null;
	}

	@Override
	public void changed(ObservableValue<? extends Status> observable,
			Status oldValue, Status newValue) {
		// TODO Auto-generated method stub
	
		System.out.println("status in task " + newValue);
		synchronized (sync) {
			canContinue = (newValue == Status.Complete);
		}
		
	}

}
