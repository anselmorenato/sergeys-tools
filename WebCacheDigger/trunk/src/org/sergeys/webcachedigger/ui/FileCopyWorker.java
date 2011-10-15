package org.sergeys.webcachedigger.ui;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.sergeys.library.FileUtils;
import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.Database;
import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;
import org.sergeys.webcachedigger.logic.SimpleLogger;

public class FileCopyWorker
extends SwingWorker<Integer, Integer>
{

	private List<CachedFile> files;
	String targetDir;
	ProgressDialog pd;
	Settings settings;
	
	public FileCopyWorker(List<CachedFile> files, String targetDir, ProgressDialog pd, Settings settings){
		this.files = files;
		this.targetDir = targetDir;
		this.pd = pd;
		this.settings = settings;
	}
	
	@Override
	protected void process(List<Integer> chunks) {		
		super.process(chunks);
		
		pd.updateProgress(chunks.isEmpty() ? 0 : chunks.get(chunks.size() - 1), ProgressDialog.STAGE_COPY);
	}

	@Override
	protected void done() {
		super.done();
		
		try {
			pd.copyingComplete(get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (CancellationException e) {
			SimpleLogger.logMessage("FileCopyWorker cancelled"); //$NON-NLS-1$
		}
	}

	@Override
	protected Integer doInBackground() throws Exception {
		
		int copied = 0;
		
		publish(copied);
		
		ArrayList<CachedFile> markAsSaved = new ArrayList<CachedFile>();
		
		for(final CachedFile file: files){
			
			if(isCancelled()){
				return null;
			}
			
			if(file.isSelectedToCopy()){
				
				String targetFile = targetDir + File.separator + file.getProposedName();
				if(file.guessExtension() != null){
					targetFile = targetFile + "." + file.guessExtension();  //$NON-NLS-1$
				}
				final String targetFile1 = targetFile; 
				try {
					
					FileUtils.copyFile(file.getAbsolutePath(), targetFile);
		
					if(settings.isExcludeAlreadySaved()){
						try {
							file.getHash();	// calculate hash here to indicate smooth progress
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						markAsSaved.add(file);
					}
					
					// TODO: java 7
					//Files.copy(file.getAbsolutePath(), targetFile, );
					
					copied++;
					
					publish(copied);
					
				} catch (final IOException e) {
					//throw e;
					SimpleLogger.logMessage(String.format("failed to copy file: %s", e.getMessage())); //$NON-NLS-1$
					SwingUtilities.invokeLater(new Runnable(){

						@Override
						public void run() {
							String msg = String.format(Messages.getString("FileCopyWorker.FailedToCopyFromTo"), //$NON-NLS-1$
									file.getAbsolutePath(), targetFile1,
									e.getMessage());
							JOptionPane.showMessageDialog(null, msg);
							
						}});					
				}
			}
		}
		
		if(settings.isExcludeAlreadySaved()){
			try {
				Database.getInstance().setSaved(markAsSaved);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return copied;						
	}

}
