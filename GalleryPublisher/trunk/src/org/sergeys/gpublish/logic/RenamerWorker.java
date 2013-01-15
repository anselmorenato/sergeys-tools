package org.sergeys.gpublish.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
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
    //private HashMap<String, ArrayList<String>> wallpaperMap = new HashMap<String, ArrayList<String>>();
    private HashMap<String, TreeMap<Integer, String>> wallpaperMap = new HashMap<String, TreeMap<Integer,String>>();

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

        // sort by resolutions, actually no need to sort now
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

        	Settings.getLogger().info("Processing " + dir.getName());
        	
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
                
                try{
	                if(Settings.getInstance().isDeleteRawWallpapers()){
	                	// move wallpaper
	                	if(targetfile.exists()){
	                		if(!targetfile.delete()){
	                			Settings.getLogger().error("Failed to delete existing " + targetfile);
		                		warningCount++;
	                		}
	                	}
	                	if(file.renameTo(targetfile)){
	                		Settings.getLogger().info("Moved to " + targetfile.getName());
	                	}
	                	else{
	                		Settings.getLogger().error("Failed to rename " + file + " to " + targetfile);
	                		warningCount++;
	                	}
	                }
	                else{
	                	// copy wallpaper
	                	copyFile(file, targetfile);
	                	Settings.getLogger().info("Copied to " + targetfile.getName());
	                }
	                
	                //putWallpaper(file.getName(), resolution);
                }
                catch(Exception ex){
                	Settings.getLogger().error("Failed to copy/move wallpaper " + file.getAbsolutePath(), ex);
                	warningCount++;
                }
            }

//			try{
//				Thread.sleep(1000);
//			}
//			catch(InterruptedException ex){
//				return ExitCode.Cancelled;
//			}
        }

        // collect all wallpaper names in wp-ready
        findAllWallpapers();
        
        // post image files and html
        Settings.getLogger().info("Search and generate html for post images in " + postImagesDir);
        File[] files = postImagesDir.listFiles(FileFilters.OnlyFiles);

        // sort files
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

            Settings.getLogger().info("Found post image " + file.getName());

            String template;
            String text;

            // first goes photo itself or photo+panorama
            
            String panoramaName = filenametokens[0] + "-b." + filenametokens[1];
            if(new File(file.getParent() + File.separator + panoramaName).exists()){
                // panorama
                if(count == 0){
                    // 1st photo in the post without number
                    //template = "\n<a href=\"%2$s\"><img src=\"%3$s\" border=\"0\"></a>\n<b>.::кликабельно::.</b>\n\n";
                    template = Settings.getInstance().getHtmlTemplate("firstphoto.panorama");
                }
                else{
                    //template = "%1$s. \n<a href=\"%2$s\"><img src=\"%3$s\" border=\"0\"></a>\n<b>.::кликабельно::.</b>\n\n";
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

            // then go wallpapers if present
            if(wallpaperMap.containsKey(filename)){
                StringBuilder sbWp = new StringBuilder();
                //String templateWp = "<a href=\"%1$s\">%2$s</a>";
                String templateWp = Settings.getInstance().getHtmlTemplate("wallpaper");
                //for(String resolution: wallpaperMap.get(filename)){
                for(String resolution: wallpaperMap.get(filename).values()){

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

                //sbHtml.append("<b>Обои:</b> " + sbWp + "\n\n");
                template = Settings.getInstance().getHtmlTemplate("wallpaper.wrapper");
                sbHtml.append(String.format(template, sbWp));
            }
            
            // and separator between photos
            template = Settings.getInstance().getHtmlTemplate("photo.delimiter");
            sbHtml.append(template);

            if(files.length > 1 && count == 0){
                String strTotal = Integer.toString(sortedFiles.size() - 1);
                //sbHtml.append(String.format("<lj-cut text=\"Смотреть %s фотографий\">\n\n", strTotal));
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

    private void findAllWallpapers(){
    	//wallpaperMap = new HashMap<String, ArrayList<String>>();
    	wallpaperMap = new HashMap<String, TreeMap<Integer,String>>();
    	
    	File[] wallpapers = new File(Settings.getInstance().getDstWallpapersDir()).listFiles(FileFilters.OnlyFiles);
    	    	
    	for(File wallpaper: wallpapers){
    		String[] tokens = wallpaper.getName().split("[\\.-]");
    		// restore original file name
    		StringBuilder sb = new StringBuilder();
    		for(int i = 0; i < tokens.length - 2; i++){
    			if(i != 0){
    				sb.append("-");
    			}
    			sb.append(tokens[i]);
    		}
    		
    		sb.append(".").append(tokens[tokens.length - 1]);
    		
    		putWallpaper(sb.toString(), tokens[tokens.length - 2]);
    	}    	    	 
    }
    
    /**
     * 
     * @param filename like "sokol-05.jpg"
     * @param resolution like "1024x768"
     */
    private void putWallpaper(String filename, String resolution){
        //ArrayList<String> resolutions = wallpaperMap.get(filename);
    	TreeMap<Integer, String> resolutions = wallpaperMap.get(filename);
        if(resolutions == null){
            //resolutions = new ArrayList<String>();
        	resolutions = new TreeMap<Integer, String>();
            wallpaperMap.put(filename, resolutions);
        }

//        if(!resolutions.contains(resolution)){
//            resolutions.add(resolution);
//        }
        
        // have resolutions sorted ascending
        if(!resolutions.containsValue(resolution)){
        	String[] tokens = resolution.split("[xX]");
        	resolutions.put(Integer.valueOf(tokens[0]), resolution);
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
