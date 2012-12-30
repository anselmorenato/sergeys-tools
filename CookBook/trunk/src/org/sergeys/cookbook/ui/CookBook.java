package org.sergeys.cookbook.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.sergeys.cookbook.logic.Settings;

public class CookBook extends Application {

    private Stage primaryStage;
    private MainController controller;

    @Override
    public void start(final Stage stage) {

        this.primaryStage = stage;

        try {

            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

            // http://docs.oracle.com/javafx/2/api/javafx/fxml/doc-files/introduction_to_fxml.html

            URL location = getClass().getResource("MainScene.fxml");
            //URL location = getClass().getResource("Main2.fxml");
            //ResourceBundle resources = ResourceBundle.getBundle("com.foo.example");
            FXMLLoader fxmlLoader = new FXMLLoader(location);

            Pane root = (Pane)fxmlLoader.load();
            controller = (MainController)fxmlLoader.getController();

            primaryStage.setTitle("CookBook");
            //primaryStage.setScene(new Scene(root, 800, 500));
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(controller.getAppIcon());

            controller.applySettings();

            primaryStage.setX(Settings.getInstance().getWinPosition().getWidth());
            primaryStage.setY(Settings.getInstance().getWinPosition().getHeight());
            primaryStage.setWidth(Settings.getInstance().getWinSize().getWidth());
            primaryStage.setHeight(Settings.getInstance().getWinSize().getHeight());

            primaryStage.setOnHiding(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent event) {
                    saveWinPosition();
                }});

            primaryStage.show();

            controller.myInit(primaryStage);

            //controller.createSampleData();
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        // called before main stage closing events
        super.stop();

        Settings.save();
    }

    private void saveWinPosition(){
        // save window location, stage should be still shown
        Settings.getInstance().getWinPosition().setSize(primaryStage.getX(), primaryStage.getY());
        Settings.getInstance().getWinSize().setSize(primaryStage.getWidth(), primaryStage.getHeight());
        controller.collectSettings();

        try {
            Settings.save();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
