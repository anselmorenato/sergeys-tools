package org.sergeys.privoxytool.ui;

import java.net.URL;

import org.sergeys.privoxytool.logic.Settings;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class PrivoxyTool extends Application {

    @Override
    public void start(Stage primaryStage) {
    	
    	Settings.getLogger().info("application start");
    	
    	try{
    		URL location = getClass().getResource("Main.fxml");
            FXMLLoader loader = new FXMLLoader(location);

            Pane root = (Pane)loader.load();
            MainController controller = (MainController)loader.getController();
            
            primaryStage.setTitle("PrivoxyTool");
            //primaryStage.setScene(new Scene(root, 800, 500));
            primaryStage.setScene(new Scene(root));
            //primaryStage.getIcons().add(controller.getAppIcon());

            controller.setStage(primaryStage);
            controller.applySettings();

//            primaryStage.setX(Settings.getInstance().getWinPosition().getWidth());
//            primaryStage.setY(Settings.getInstance().getWinPosition().getHeight());
//            primaryStage.setWidth(Settings.getInstance().getWinSize().getWidth());
//            primaryStage.setHeight(Settings.getInstance().getWinSize().getHeight());

            primaryStage.setOnHiding(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent event) {
                    //saveWinPosition();
                	
                	Settings.getLogger().info("application exit");
                }});

            primaryStage.show();

    	}
    	catch(Exception ex){
    		Settings.getLogger().error("Failed to start application, exit", ex);
    		Platform.exit();
    	}
    }

    public static void main(String[] args) {
        launch(args);
    }
}
