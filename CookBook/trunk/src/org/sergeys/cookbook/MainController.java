package org.sergeys.cookbook;

import java.io.File;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {

    @FXML private TreeView<String> tree;
    @FXML private WebView webview;
    @FXML private AnchorPane pane;

    private Stage stage;
    private FileChooser fc;

    public void initialize(){
        System.out.println("init");
    }

    public void onMenuCloseAction(ActionEvent e){
        System.out.println("exit");
        Platform.exit();
    }

    public void onMenuOpenAction(ActionEvent e){

        if(fc == null){
            fc = new FileChooser();
        }

        // http://java-buddy.blogspot.com/2012/03/javafx-20-disable-ower-window-for.html

        File file = fc.showOpenDialog(stage);

        if (file != null) {
            String path = file.getAbsolutePath();
            System.out.println(path);
            try{
                webview.getEngine().load("file:///" + path);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }


    public void createSampleData(Stage stage){

        this.stage = stage;

        TreeItem<String> treeRoot = new TreeItem<String>("Root node");

        treeRoot.getChildren().addAll(Arrays.asList(
                new TreeItem<String>("Child Node 1"),
                new TreeItem<String>("Child Node 2"),
                new TreeItem<String>("Child Node 3")));


        treeRoot.getChildren().get(2).getChildren().addAll(Arrays.asList(
                new TreeItem<String>("Child Node 4"),
                new TreeItem<String>("Child Node 5"),
                new TreeItem<String>("Child Node 6"),
                new TreeItem<String>("Child Node 7"),
                new TreeItem<String>("Child Node 8"),
                new TreeItem<String>("Child Node 9"),
                new TreeItem<String>("Child Node 10"),
                new TreeItem<String>("Child Node 11"),
                new TreeItem<String>("Child Node 12")));


        tree.setShowRoot(true);
        tree.setRoot(treeRoot);
        treeRoot.setExpanded(true);

        WebEngine webEngine = webview.getEngine();
        try{
            webEngine.load("file:///D:/workspace/CookBook/samplefiles/2.html");
            //webEngine.load("http://java.oracle.com");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
