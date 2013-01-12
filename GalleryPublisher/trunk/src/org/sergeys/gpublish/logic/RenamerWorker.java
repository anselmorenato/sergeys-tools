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

	private final String FILE_REGEXP = "^[\\w-]+-\\d+\\.\\w+$"; 
	private final String FILE_PANORAMA_REGEXP = "^[\\w-]+-\\d+-b\\.\\w+$";
	
    public enum ExitCode { SuccessClean, SuccessWarnings, Error, Cancelled };

    private MainWindow mainWindow;
    private StringBuilder sbHtml = new StringBuilder();

    // filename -> list of resolutions; "sokol-05.jpg" -> ["1024x768", "1280x1024"]
    private HashMap<String, ArrayList<String>> wallpaperMap = new HashMap<String, ArrayList<String>>();

    private int warningCount = 0;

    public RenamerWorker(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    @Override
    protected ExitCode doInBackground() throws Exception {
        // this method runs in background thread, cannot update ui controls here

        Settings.getLogger().debug("start background task");

        // copy wallpapers

        // check that folders actually exist
        // TODO rename folders to dirs
        File wpDstDir = new File(Settings.getInstance().getDstWallpapersDir());
        if(!wpDstDir.exists() || !wpDstDir.isDirectory()){
            Settings.getLogger().error("Dir does not exist or file is not a dir: " + wpDstDir);
            return ExitCode.Error;
        }

        File wpSrcDir = new File(Settings.getInstance().getSrcWallpapersDir());
        if(!wpSrcDir.exists() || !wpSrcDir.isDirectory()){
            Settings.getLogger().error("Dir does not exist or file is not a dir: " + wpSrcDir);
            return ExitCode.Error;
        }

        File postImagesDir = new File(Settings.getInstance().getSrcPostImagesDir());
        if(!postImagesDir.exists() || !postImagesDir.isDirectory()){
            Settings.getLogger().error("Dir does not exist or file is not a dir: " + postImagesDir);
            return ExitCode.Error;
        }

        String dstDirName = wpDstDir.getName().toLowerCase();

        // find and process all wallpaper subdirs
        Settings.getLogger().info("Search for wallpaper subdirs in " + wpSrcDir);

        File[] subdirs = wpSrcDir.listFiles(FileFilters.OnlyDirs);

        // sort by resolutions
        TreeMap<Integer, File> sortedDirs = new TreeMap<Integer, File>();
        for(File dir: subdirs){
            String dirname = dir.getName().toLowerCase();

            if(dirname.equals(dstDirName)){
                Settings.getLogger().info("Target dir found as subdir, skipped: " + dir.getName());
                continue;
            }

            if(!dirname.matches("^wp-\\d+[xX]\\d+$")){
                Settings.getLogger().warn("Dir name does not match pattern wp-0123x456, skipped: " + dir.getName());
                warningCount++;
                continue;
            }

            String[] dirnametokens = dirname.split("[-xX]");	// regexp: any of these chars is a separator

            sortedDirs.put(Integer.valueOf(dirnametokens[1]), dir);

            Settings.getLogger().info("Found wallpaper dir " + dir.getName());
        }

        Settings.getLogger().info("Search, rename and copy wallpapers");
        for(File dir: sortedDirs.values()){

            String[] dirnametokens = dir.getName().split("[-xX]");	// regexp: any of these chars is a separator

            String resolution = dirnametokens[1] + "x" + dirnametokens[2];

            // find and copy all wallpaper files

            File[] files = dir.listFiles(FileFilters.OnlyFiles);

            for(File file: files){

                // need to check this periodically and terminate when requested
                if(isCancelled()){
                    Settings.getLogger().debug("I am cancelled");
                    return ExitCode.Cancelled;
                }

                if(!file.getName().matches(FILE_REGEXP)){
                    Settings.getLogger().warn("File name does not match regexp " + FILE_REGEXP + ", skipped: " + file.getAbsolutePath());
                    warningCount++;
                    continue;
                }

                String[] filenametokens = file.getName().split("\\.");	// name.ext

                String targetname = filenametokens[0] + "-" + resolution + "." + filenametokens[1];
                File targetfile = new File(wpDstDir.getAbsolutePath() + File.separator +  targetname);
                copyFile(file, targetfile);

                putWallpaper(file.getName(), resolution);

                Settings.getLogger().info("Copied " + targetfile.getName());
            }

//			try{
//				Thread.sleep(1000);
//			}
//			catch(InterruptedException ex){
//				return ExitCode.Cancelled;
//			}
        }

        // post image files and html
        Settings.getLogger().info("Search and generate html for post images in " + postImagesDir);
        File[] files = postImagesDir.listFiles(FileFilters.OnlyFiles);

        // sort files
        //TreeMap<String, File> sortedFiles = new TreeMap<String, File>();
        TreeMap<Integer, File> sortedFiles = new TreeMap<Integer, File>();
        for(File file: files){

            if(file.getName().matches(FILE_PANORAMA_REGEXP)){
                Settings.getLogger().debug("Panorama found, skipped: " + file.getName());
                continue;
            }

            if(!file.getName().matches(FILE_REGEXP)){
                Settings.getLogger().warn("File name does not match regexp " + FILE_REGEXP + ", skipped: " + file.getName());
                warningCount++;
                continue;
            }
            String[] filenametokens = file.getName().split("[\\.-]");

            //String refinedNumber = Integer.valueOf(filenametokens[1]).toString();
            // number is the token before dot 
            //String refinedNumber = Integer.valueOf(filenametokens[filenametokens.length - 1 - 1]).toString();
            int refinedNumber = Integer.valueOf(filenametokens[filenametokens.length - 1 - 1]);

            if(sortedFiles.containsKey(refinedNumber)){
                Settings.getLogger().warn("Duplicated file number " + refinedNumber + " for " +
                        sortedFiles.get(refinedNumber).getName() + " and " + file.getName() + ", second file skipped");
                warningCount++;
            }
            else{
                sortedFiles.put(refinedNumber, file);
            }
        }

        // generate html
        int count = 0;
        for(File file: sortedFiles.values()){
            String filename = file.getName();
            String[] filenametokens = filename.split("\\.");

            Settings.getLogger().info("Found " + file.getName());

            String template;
            String text;

            String panoramaName = filenametokens[0] + "-b." + filenametokens[1];
            if(new File(file.getParent() + File.separator + panoramaName).exists()){
                // panorama
                if(count == 0){
                    // 1st photo in the post without number
                    //template = "\n<a href=\"%2$s\"><img src=\"%3$s\" border=\"0\"></a>\n<b>.::�����������::.</b>\n\n";
                    template = Settings.getInstance().getHtmlTemplate("firstphoto.panorama");
                }
                else{
                    //template = "%1$s. \n<a href=\"%2$s\"><img src=\"%3$s\" border=\"0\"></a>\n<b>.::�����������::.</b>\n\n";
                    template = Settings.getInstance().getHtmlTemplate("nextphoto.panorama");
                }

                text = String.format(template, count,
                        Settings.getInstance().getWebPrefixPostImages() + "/" + panoramaName,
                        Settings.getInstance().getWebPrefixPostImages() + "/" + filename);

            }
            else{
                if(count == 0){
                    // 1st photo in the post without number
                    //template = "\n<img src=\"%2$s\" border=\"0\">\n\n";
                    template = Settings.getInstance().getHtmlTemplate("firstphoto");
                }
                else{
                    //template = "%1$s. \n<img src=\"%2$s\" border=\"0\">\n\n";
                    template = Settings.getInstance().getHtmlTemplate("nextphoto");
                }

                text = String.format(template, count, Settings.getInstance().getWebPrefixPostImages() + "/" + filename);
            }


            sbHtml.append(text);

            // wallpapers
            if(wallpaperMap.containsKey(filename)){
                StringBuilder sbWp = new StringBuilder();
                //String templateWp = "<a href=\"%1$s\">%2$s</a>";
                String templateWp = Settings.getInstance().getHtmlTemplate("wallpaper");
                for(String resolution: wallpaperMap.get(filename)){

                    // verify again that wallpaper really exist
                    String wpFilename = filenametokens[0] + "-" + resolution + "." + filenametokens[1];
                    String wpFilepath = Settings.getInstance().getDstWallpapersDir() + File.separator + wpFilename;
                    if(!new File(wpFilepath).exists()){
                        Settings.getLogger().error("Wallpaper file must exist but not found for some reason: " + wpFilepath);
                        return ExitCode.Error;
                    }

                    if(sbWp.length() > 0){
                        //sbWp.append("&nbsp;|&nbsp;");
                        //sbWp.append(" | ");
                        sbWp.append(Settings.getInstance().getHtmlTemplate("wallpaper.separator"));
                    }

                    String textWp = String.format(templateWp,
                            Settings.getInstance().getWebPrefixWallpapers() + "/" + wpFilename,
                            resolution);

                    sbWp.append(textWp);	// no newline here
                }

                //sbHtml.append("<b>����:</b> " + sbWp + "\n\n");
                template = Settings.getInstance().getHtmlTemplate("wallpaper.wrapper");
                sbHtml.append(String.format(template, sbWp));
            }


            if(files.length > 1 && count == 0){
                String strTotal = Integer.toString(sortedFiles.size() - 1);
                //sbHtml.append(String.format("<lj-cut text=\"�������� %s ����������\">\n\n", strTotal));
                sbHtml.append(String.format(Settings.getInstance().getHtmlTemplate("ljcut.start"), strTotal));
            }

            count++;
        }

        if(files.length > 1){
            //sbHtml.append("</lj-cut>");
            sbHtml.append(Settings.getInstance().getHtmlTemplate("ljcut.end"));
        }

        Settings.getLogger().debug("end background task");

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
                    //JOptionPane.showMessageDialog(mainWindow.getFrame(), "worker cancelled");
                    break;
                case Error:
                    JOptionPane.showMessageDialog(mainWindow.getFrame(), "Processing failed, please check log file.");
                    break;
                default:
                    break;
            }
        } catch (InterruptedException e) {
            Settings.getLogger().error("", e);
        } catch (ExecutionException e) {
            Settings.getLogger().error("", e);
        } catch (CancellationException e) {
            Settings.getLogger().error("", e);
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
