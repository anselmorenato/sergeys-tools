package org.sergeys.cookbook.ui;


import java.io.FileNotFoundException;

import org.sergeys.cookbook.logic.Settings;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;

public class MainController {
	@FXML private SplitPane splitPane;
	
    public void initialize(){
        System.out.println("init");
        
        double pos = Settings.getInstance().getWinDividerPosition();
        System.out.println(">" + pos);
        splitPane.setDividerPositions(pos);
    }
    
    public void onMenuItemClose(ActionEvent e){
        Settings.getLogger().info("application exit");
        
        try {

            double pos = splitPane.getDividerPositions()[0];
            System.out.println("<" + pos);

            Settings.getInstance().setWinDividerPosition(pos);

            System.out.println(Settings.getInstance().getWinDividerPosition());

            Settings.save();
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Platform.exit();
    }

}
