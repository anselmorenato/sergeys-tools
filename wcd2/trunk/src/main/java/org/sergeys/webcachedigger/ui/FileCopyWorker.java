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


public class FileCopyWorker
extends SwingWorker<List<CachedFile>, Integer>
{

    private List<CachedFile> files;
    String targetDir;
    ProgressDialog pd;


    public FileCopyWorker(List<CachedFile> files, String targetDir, ProgressDialog pd){
        this.files = files;
        this.targetDir = targetDir;
        this.pd = pd;

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
            Settings.getLogger().error("", e); //$NON-NLS-1$
        } catch (ExecutionException e) {
            Settings.getLogger().error("", e); //$NON-NLS-1$
        }
        catch (CancellationException e) {
            Settings.getLogger().debug("worker cancelled", e); //$NON-NLS-1$
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

                if(Settings.getInstance().isExcludeSavedAndIgnored()){
                    // check for duplicates
                    if(markAsSaved.containsKey(file.getHash())){
                        Settings.getLogger().info(String.format("Duplicate file skipped: %s", file.getName())); //$NON-NLS-1$
                        copied.add(file);	// to remove from visible list
                        continue;
                    }
                }

                if(file.getMimeType().equals("audio/mpeg") && Settings.getInstance().isRenameMp3byTags()){ //$NON-NLS-1$
                    String proposed = Mp3Utils.getInstance().proposeName(file);
                    if(!proposed.contains("?")){ //$NON-NLS-1$
                        file.setProposedName(proposed);
                    }
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

                    if(Settings.getInstance().isExcludeSavedAndIgnored()){
                        markAsSaved.put(file.getHash(), file);
                    }

                    copied.add(file);
                    copiedCount++;

                    publish(copiedCount);

                } catch (final IOException e) {
                    Settings.getLogger().error("failed to copy " + file.getAbsolutePath() + " to " + targetFile1, e); //$NON-NLS-1$ //$NON-NLS-2$
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

        if(Settings.getInstance().isExcludeSavedAndIgnored()){
            try {
                Database.getInstance().updateSaved(markAsSaved.values());
            } catch (NoSuchAlgorithmException e) {
                Settings.getLogger().error("", e); //$NON-NLS-1$
            } catch (SQLException e) {
                Settings.getLogger().error("", e); //$NON-NLS-1$
            } catch (IOException e) {
                Settings.getLogger().error("", e); //$NON-NLS-1$
            }
        }

        return copied;
    }

}
