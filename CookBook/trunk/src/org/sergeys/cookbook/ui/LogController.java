package org.sergeys.cookbook.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.Future;

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
    private Future<?> future;
    private Runnable watcher = new Runnable() {

    	public boolean canContinue = true; // for fu=indbugs, no one can set this 
    	
        @Override
        public void run() {

            // http://www.ericbruno.com/ericbruno/Programming_with_Reason/Entries/2010/4/30_Java_File_IO_Comparison.html
            // http://skillshared.blogspot.com/2012/11/how-to-read-dynamically-growing-file.html

            File logfile = new File(Settings.getSettingsDirPath()
                    + File.separator + "log.txt");
            BufferedReader br = null;
            
            try {                
                
              //br = new BufferedReader(new FileReader(logfile));
              br = new BufferedReader(new InputStreamReader(new FileInputStream(logfile), Charset.defaultCharset()));
              //boolean canContinue = true;
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
              
              Settings.getLogger().debug("log watch cycle ended normally");
                
            }
            catch(Exception ex){
                try {
                	if(br != null){
                		br.close();
                	}
                    
                    Settings.getLogger().debug("reader closed");
                } catch (IOException e) {
                    Settings.getLogger().error("", e);
                }
                
                if(ex instanceof InterruptedException){
                    Settings.getLogger().debug("log watch cycle: thread interrupted");
                }
                else{
                    Settings.getLogger().error("", ex);
                }
                Thread.currentThread().interrupt();
            }

            Settings.getLogger().debug("watcher run complete");
        }
    };

//    class Watcher extends Thread{
//
//        @Override
//        public void run() {
//            File logfile = new File(Settings.getSettingsDirPath()
//                    + File.separator + "log0.txt");
//            BufferedReader br = null;
//            
//            try {
//                
//                br = new BufferedReader(new FileReader(logfile));
//                boolean canContinue = true;
//                StringBuilder sb = new StringBuilder();
//                while (canContinue) {
//                    String str = br.readLine();
//                    if (str != null) {
//                    	sb.append(str);
//                    	sb.append("\n");
//                    }
//                    else{
//                    	final String append = sb.toString();
//                    	
//                    	Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                logTextarea.appendText(append);                                
//                            }
//                        });
//                    	
//                    	Thread.sleep(500);
//                    	sb = new StringBuilder();
//                    }                    
//                }
//            }
//            catch(Exception ex){
//                try {
//                    br.close();                    
//                    Settings.getLogger().debug("reader closed");
//                } catch (IOException e) {
//                    Settings.getLogger().error("", e);
//                }
//                if(ex instanceof InterruptedException){
//                    Settings.getLogger().debug("log watch thread interrupted");
//                }
//                else{
//                    Settings.getLogger().error("", ex);
//                    Thread.currentThread().interrupt();
//                }                
//            }
//
//            Settings.getLogger().debug("watcher run complete");
//        }
//    }

    public void initialize() {
         super.initialize();
    }

//    Watcher w;

    @Override
    public void handle(WindowEvent evt) {
        if (evt.getEventType() == WindowEvent.WINDOW_SHOWN) {

        	logTextarea.clear();
        	
            future = Settings.getExecutor().submit(watcher);
            //Settings.getExecutor().execute(watcher);
        	        	
//            if(w == null){
//                w = new Watcher();
//                w.start();
//            }

        } else if (evt.getEventType() == WindowEvent.WINDOW_HIDING ||
                evt.getEventType() == WindowEvent.WINDOW_HIDDEN ||
                evt.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {

            try {
                future.cancel(true);
            	
//            	if(w != null){
//            		w.interrupt();
//            		Settings.getLogger().debug("interrupted");
//            		w.join();
//            		Settings.getLogger().debug("joined");
//            		w = null;
//            	}

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
