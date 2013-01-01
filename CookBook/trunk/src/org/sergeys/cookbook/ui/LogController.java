package org.sergeys.cookbook.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.sergeys.cookbook.logic.Settings;

public class LogController extends DialogController implements
        EventHandler<WindowEvent> {
    @FXML
    private TextArea logTextarea;

    // for some reason runnable launched via executor here causes app to long wait on exit 
//    private Future<?> future;
//    private Runnable watcher = new Runnable() {
//
//        @Override
//        public void run() {
//
//            // http://www.ericbruno.com/ericbruno/Programming_with_Reason/Entries/2010/4/30_Java_File_IO_Comparison.html
//            // http://skillshared.blogspot.com/2012/11/how-to-read-dynamically-growing-file.html
//
//            File logfile = new File(Settings.getSettingsDirPath()
//                    + File.separator + "log0.txt");
//            BufferedReader br = null;
//            FileReader fr = null;
//            try {
//                fr = new FileReader(logfile);
//                br = new BufferedReader(fr);
//                boolean canContinue = true;
//                while (canContinue) {
//                    final String str = br.readLine();
//                    if (str != null) {
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                logTextarea.appendText(str + "\n");
//                            }
//                        });
//                    }
//
//                    Thread.sleep(500);
//                }
//
////			} catch (IOException e) {
////				Settings.getLogger().error("", e);
////			} catch (InterruptedException e) {
////				try {
////					br.close();
////					Settings.getLogger().debug("reader closed");
////					Thread.currentThread().interrupt();
////
////				} catch (IOException e1) {
////					Settings.getLogger().error("", e1);
////				}
////				Settings.getLogger().debug("log watch thread interrupted");
//            }
//            catch(Exception ex){
//                try {
//                    br.close();
//                    fr.close();
//                    Settings.getLogger().debug("reader closed");
//                } catch (IOException e) {
//                    Settings.getLogger().error("", e);
//                }
//                if(ex instanceof InterruptedException){
//                    Settings.getLogger().debug("log watch thread interrupted");
//                }
//                else{
//                    Settings.getLogger().error("", ex);
//                }
//                Thread.currentThread().interrupt();
//            }
//
//            Settings.getLogger().debug("watcher run complete");
//        }
//    };

    class Watcher extends Thread{

        @Override
        public void run() {
            File logfile = new File(Settings.getSettingsDirPath()
                    + File.separator + "log0.txt");
            BufferedReader br = null;
            
            try {
                
                br = new BufferedReader(new FileReader(logfile));
                boolean canContinue = true;
                StringBuilder sb = new StringBuilder();
                while (canContinue) {
                    String str = br.readLine();
                    if (str != null) {
                    	sb.append(str);
                    	sb.append("\n");
                    }
                    else{
                    	final String append = sb.toString();
                    	
                    	Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                logTextarea.appendText(append);                                
                            }
                        });
                    	
                    	Thread.sleep(500);
                    	sb = new StringBuilder();
                    }                    
                }
            }
            catch(Exception ex){
                try {
                    br.close();                    
                    Settings.getLogger().debug("reader closed");
                } catch (IOException e) {
                    Settings.getLogger().error("", e);
                }
                if(ex instanceof InterruptedException){
                    Settings.getLogger().debug("log watch thread interrupted");
                }
                else{
                    Settings.getLogger().error("", ex);
                    Thread.currentThread().interrupt();
                }                
            }

            Settings.getLogger().debug("watcher run complete");
        }
    }

    public void initialize() {
         super.initialize();
    }

    Watcher w;

    @Override
    public void handle(WindowEvent evt) {
        if (evt.getEventType() == WindowEvent.WINDOW_SHOWN) {

            //future = Settings.getExecutor().submit(watcher);
            //Settings.getExecutor().execute(watcher);

        	logTextarea.clear();
        	
            if(w == null){
                w = new Watcher();
                w.start();
            }

        } else if (evt.getEventType() == WindowEvent.WINDOW_HIDING ||
                evt.getEventType() == WindowEvent.WINDOW_HIDDEN ||
                evt.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {

            try {
                //future.cancel(true);
            	
            	if(w != null){
            		w.interrupt();
            		Settings.getLogger().debug("interrupted");
            		w.join();
            		Settings.getLogger().debug("joined");
            		w = null;
            	}

            } catch (Exception e) {
                Settings.getLogger().error("", e);
            }
//            future = null;
        }
    }

    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);

        //stage.setOnHiding(this);
        stage.setOnHidden(this);
        //stage.setOnCloseRequest(this);
        stage.setOnShown(this);
    }
}
