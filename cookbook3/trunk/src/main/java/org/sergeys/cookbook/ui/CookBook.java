package org.sergeys.cookbook.ui;

import java.net.URL;

import org.sergeys.cookbook.logic.Settings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CookBook extends Application
{
	private Stage primaryStage;
	
    public static void main(String[] args) {
    	Settings.getLogger().debug("main");
    	launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("start");

        this.primaryStage = stage;

        URL location = getClass().getResource("/fxml/MainScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);

        Pane root = (Pane)fxmlLoader.load();
        primaryStage.setScene(new Scene(root));

        primaryStage.show();
    }

}
