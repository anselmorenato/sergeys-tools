package org.sergeys.gpublish.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.sergeys.gpublish.ui.MainWindow;

public class RenamerWorker extends SwingWorker<RenamerWorker.ExitCode, Integer> {

    public enum ExitCode { SuccessClean, SuccessWarnings, Error, Cancelled };

    private MainWindow mainWindow;
    private StringBuilder sbHtml = new StringBuilder();

    // filename without ext -> list of resolutions; "sokol-05" -> ["1024x768", "1280x1024"]
    private HashMap<String, ArrayList<String>> wallpaperMap = new HashMap<String, ArrayList<String>>();

    private int warningCount = 0;
    
    public RenamerWorker(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    @Override
    protected ExitCode doInBackground() throws Exception {
        // this method runs in background thread, cannot update ui controls here

        // copy wallpapers

        // check that folders actually exist
        // TODO rename folders to dirs
        File wpDstFolder = new File(Settings.getInstance().getDstWallpapersFolder());
        if(!wpDstFolder.exists() || !wpDstFolder.isDirectory()){
            Settings.getLogger().error("Dir does not exist or file is not a dir: " + wpDstFolder);
            return ExitCode.Error;
        }

        File wpSrcFolder = new File(Settings.getInstance().getSrcWallpapersFolder());
        if(!wpSrcFolder.exists() || !wpSrcFolder.isDirectory()){
            Settings.getLogger().error("Dir does not exist or file is not a dir: " + wpSrcFolder);
            return ExitCode.Error;
        }

        File postImagesDir = new File(Settings.getInstance().getSrcPostImagesFolder());
        if(!postImagesDir.exists() || !postImagesDir.isDirectory()){
            Settings.getLogger().error("Dir does not exist or file is not a dir: " + wpSrcFolder);
            return ExitCode.Error;
        }

        String dstFolderName = wpDstFolder.getName().toLowerCase();

        // find and process all wallpaper subdirs

        File[] subdirs = wpSrcFolder.listFiles(FileFilters.OnlyDirs);

        for(File dir: subdirs){

            // need to check this periodically and terminate when requested
            if(isCancelled()){
                Settings.getLogger().debug("I am cancelled");
                return ExitCode.Cancelled;
            }

            String dirname = dir.getName().toLowerCase();

            if(dirname.equals(dstFolderName)){
                Settings.getLogger().info("Target dir found as subdir, skipped: " + dir);
                continue;
            }

            if(!dirname.matches("^wp-\\d+[xX]\\d+$")){
                Settings.getLogger().warn("Dir name does not match pattern wp-0123x456, skipped: " + dir);
                warningCount++;
                continue;
            }

            String[] dirnametokens = dirname.split("[-xX]");	// regexp: any of these chars is a separator
//			if(dirnametokens.length != 3
//					|| !dirnametokens[0].equals("wp")
//					|| !dirnametokens[1].matches("^\\d+$")	// regexp: one or more digits
//					|| !dirnametokens[2].matches("^\\d+$")){
//				Settings.getLogger().info("Dir does not match pattern wp-0123x456, skipped: " + dir.getName());
//				continue;
//			}

            String resolution = dirnametokens[1] + "x" + dirnametokens[2];

            Settings.getLogger().info("Found " + dir);
//sbHtml.append("found dir " + dir + "\n");

            // find and copy all wallpaper files

            File[] files = dir.listFiles(FileFilters.OnlyFiles);

            for(File file: files){

                // need to check this periodically and terminate when requested
                if(isCancelled()){
                    Settings.getLogger().debug("I am cancelled");
                    return ExitCode.Cancelled;
                }

                if(!file.getName().matches("^\\w+-\\d+\\.\\w+$")){
                    Settings.getLogger().warn("File name does not match pattern name-01.ext, skipped: " + file.getAbsolutePath());
                    warningCount++;
                    continue;
                }

                String[] filenametokens = file.getName().split("\\.");	// name.ext
//				if(filenametokens.length != 2){ // assume exactly one dot allowed in the name!
//					Settings.getLogger().info("File must have only one dot in the name, skipped: " + file.getAbsolutePath());
//					continue;
//				}
                String targetname = filenametokens[0] + "-" + resolution + "." + filenametokens[1];
                File targetfile = new File(wpDstFolder.getAbsolutePath() + File.separator +  targetname);
                copyFile(file, targetfile);

                putWallpaper(filenametokens[0], resolution);

                Settings.getLogger().info("Copied " + targetfile.getAbsolutePath());
//sbHtml.append("copied " + targetfile + "\n");
            }

//			try{
//				Thread.sleep(1000);
//			}
//			catch(InterruptedException ex){
//				return ExitCode.Cancelled;
//			}
        }

        // post image files and html
        File[] files = postImagesDir.listFiles(FileFilters.OnlyFiles);

        // order files
        TreeMap<String, File> sortedFiles = new TreeMap<String, File>();
        for(File file: files){
            if(!file.getName().matches("^\\w+-\\d+\\.\\w+$")){
                Settings.getLogger().warn("File name does not match pattern name-01.ext, skipped: " + file.getAbsolutePath());
                warningCount++;
                continue;
            }
            String[] filenametokens = file.getName().split("[\\.-]");

            String refinedNumber = Integer.valueOf(filenametokens[1]).toString();

            if(sortedFiles.containsKey(refinedNumber)){
                Settings.getLogger().warn("Duplicated file number " + refinedNumber + " for " + sortedFiles.get(refinedNumber) + " and " +
                        file + ", second file skipped");
                warningCount++;
            }
            else{
                sortedFiles.put(refinedNumber, file);
            }
        }

        int count = 0;
        for(File file: sortedFiles.values()){
            String filename = file.getName();
            String[] filenametokens = filename.split("\\.");
//			if(filenametokens.length != 2){
//				Settings.getLogger().info("File must have only one dot in the name, skipped: " + file.getAbsolutePath());
//				continue;
//			}

            Settings.getLogger().info("Found " + file.getAbsolutePath());
//sbHtml.append("found " + file + "\n");


            String template;

            // TODO panoramas
            if(count == 0){
                // 1st foto in the post without number
                template = "\n\n<img src=\"%2$s\" border=\"0\">\n\n";
            }
            else{
                template = "%1$s. \n\n<img src=\"%2$s\" border=\"0\">\n\n";
            }

            String text = String.format(template, count, Settings.getInstance().getWebPrefixPostImages() + "/" + filename);

            sbHtml.append(text);

            // wallpapers
            if(wallpaperMap.containsKey(filenametokens[0])){
                for(String resolution: wallpaperMap.get(filenametokens[0])){
                    sbHtml.append(resolution + "\n");
                }
            }


            if(files.length > 1 && count == 0){
                String strTotal = Integer.toString(sortedFiles.size() - 1);
                sbHtml.append(String.format("<lj-cut text=\"Ñלמענועü %s פמעמדנאפטי\">\n\n", strTotal));
            }

            count++;
        }

        if(files.length > 1){
            sbHtml.append("</lj-cut>");
        }

        return (warningCount == 0) ? ExitCode.SuccessClean : ExitCode.SuccessWarnings;
    }

    @Override
    protected void done() {
        // this method runs in GUI thread, can update ui controls
        super.done();

        try {
            switch(this.get()){
                case SuccessClean:
                    mainWindow.getTextPaneHtml().setText(sbHtml.toString());
                    break;
                case SuccessWarnings:
                    mainWindow.getTextPaneHtml().setText(sbHtml.toString());
                    JOptionPane.showMessageDialog(mainWindow.getFrame(), "Got some warnings, please check log file.");
                    break;
                case Cancelled:
                    JOptionPane.showMessageDialog(mainWindow.getFrame(), "worker cancelled");
                    break;
                case Error:
                    JOptionPane.showMessageDialog(mainWindow.getFrame(), "Processing failed, please see log file.");
                    break;
                default:
                    break;
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (CancellationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private void putWallpaper(String filename, String resolution){
        ArrayList<String> resolutions = wallpaperMap.get(filename);
        if(resolutions == null){
            resolutions = new ArrayList<String>();
            wallpaperMap.put(filename, resolutions);
        }

        if(!resolutions.contains(resolution)){
            resolutions.add(resolution);
        }
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
