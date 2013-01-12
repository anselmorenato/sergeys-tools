package org.sergeys.privoxytool.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController {

	private Stage stage;
	
	@FXML private BorderPane pane;
	
	@FXML private Button btnLogAnalyzer;
	@FXML private Button btnUserActions;
	@FXML private Button btnConfiguration;
	
	public void initialize(){
        // called by convention
	}
	
	public void setStage(Stage stage){
		this.stage = stage;
	}
	
	public Image getAppIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	public void applySettings() {
		// TODO Auto-generated method stub
		
	}

    public void onMenuExit(ActionEvent e){
    	//doExit();
    	//pane.setVisible(false);
    	stage.hide();
    }
    
//    private void doExit(){
//    	Settings.getLogger().info("application exit");        
//        Platform.exit();
//    }

}
