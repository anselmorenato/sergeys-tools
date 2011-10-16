package org.sergeys.webcachedigger.ui;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
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
import org.sergeys.webcachedigger.logic.Mp3Utils;
import org.sergeys.webcachedigger.logic.Settings;
import org.sergeys.webcachedigger.logic.SimpleLogger;

public class FileCopyWorker
extends SwingWorker<List<CachedFile>, Integer>
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
	protected List<CachedFile> doInBackground() throws Exception {
		
		ArrayList<CachedFile> copied = new ArrayList<CachedFile>(); 
		
		int copiedCount = 0;
		
		publish(copiedCount);
		
		Hashtable<String, CachedFile> markAsSaved = new Hashtable<String, CachedFile>();
		
		for(final CachedFile file: files){
			
			if(isCancelled()){
				return null;
			}
			
			if(file.isSelectedToCopy()){
				
				if(settings.isExcludeAlreadySaved()){
					// check for duplicates
					if(markAsSaved.containsKey(file.getHash())){						
						SimpleLogger.logMessage(String.format("Duplicate file skipped: %s", file.getName()));
						continue;											
					}
				}
				
				if(file.getMimeType().equals("audio/mpeg") && settings.isRenameMp3byTags()){					
					file.setProposedName(Mp3Utils.proposeName(file));					
				}
				
				// TODO: we may rename different files with same name but I see no sense in that.
				
				String targetFile = targetDir + File.separator + file.getProposedName();
				String possibleExtension = file.guessExtension(); 
				if(possibleExtension != null && !targetFile.toLowerCase().endsWith(possibleExtension.toLowerCase())){
					targetFile = targetFile + "." + possibleExtension;  //$NON-NLS-1$
				}
				final String targetFile1 = targetFile; 
				try {
					
					FileUtils.copyFile(file.getAbsolutePath(), targetFile);
		
					if(settings.isExcludeAlreadySaved()){
						markAsSaved.put(file.getHash(), file);
					}
					
					copied.add(file);										
					copiedCount++;
					
					publish(copiedCount);
					
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
				Database.getInstance().updateSaved(markAsSaved.values());
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
