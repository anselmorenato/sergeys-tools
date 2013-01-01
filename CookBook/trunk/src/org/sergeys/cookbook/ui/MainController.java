package org.sergeys.cookbook.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.sergeys.cookbook.logic.Database;
import org.sergeys.cookbook.logic.HtmlImporter;
import org.sergeys.cookbook.logic.HtmlImporter.Status;
import org.sergeys.cookbook.logic.MassImportTask;
import org.sergeys.cookbook.logic.Recipe;
import org.sergeys.cookbook.logic.RecipeLibrary;
import org.sergeys.cookbook.logic.Settings;
import org.sergeys.cookbook.logic.Tag;
import org.sergeys.cookbook.ui.RecipeTreeValue.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @FXML private TreeView<RecipeTreeValue> tree;
    @FXML private WebView webview;
    @FXML private AnchorPane pane;
    @FXML private StackPane modalDimmer;
    @FXML private SplitPane splitter;
    @FXML private TextField title;
    @FXML private TextArea tags;
    @FXML private Button buttonSave;
    @FXML private Button buttonRevert;

    private Stage stage;
    private FileChooser fc;
    private Image appIcon;
    private Stage dialogStage;

    ChangeListener<TreeItem<RecipeTreeValue>> treeListener = new ChangeListener<TreeItem<RecipeTreeValue>>(){

        @Override
        public void changed(
                ObservableValue<? extends TreeItem<RecipeTreeValue>> observable,
                TreeItem<RecipeTreeValue> oldValue,
                TreeItem<RecipeTreeValue> newValue) {

            if(newValue != null){
                if(newValue.getValue().getType() == Type.Recipe){
                    setRecipe(newValue.getValue().getRecipe());
                }
            }
        }};

    public void initialize(){
        // called by convention
        // http://docs.oracle.com/javafx/2/api/javafx/fxml/doc-files/introduction_to_fxml.html
        log.info("init");

        // TODO call in background
        RecipeLibrary.getInstance().validate();

        buttonSave.setVisible(false);
        buttonRevert.setVisible(false);

        TreeItem<RecipeTreeValue> treeRoot = new TreeItem<RecipeTreeValue>();
        tree.setShowRoot(false);
        tree.setRoot(treeRoot);
        treeRoot.setExpanded(true);

        tree.getSelectionModel().selectedItemProperty().addListener(treeListener);

        buildTree();

        // http://docs.oracle.com/javafx/2/ui_controls/tree-view.htm#BABDEADA
//        tree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
//            @Override
//            public TreeCell<String> call(TreeView<String> arg0) {
//
//                return new TextFieldTreeCellImpl();
//            }
//        });

        double pos = Settings.getInstance().getWinDividerPosition();
//        System.out.println("set " + pos);

//        splitter.setDividerPosition(0, pos);
//        System.out.println("actual " + splitter.getDividerPositions()[0]);

//        splitter.layout();

        splitter.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {

//                System.out.println("changed to " + newValue);
            }});

        splitter.getDividers().get(0).positionProperty().addListener(new InvalidationListener(){

            @Override
            public void invalidated(Observable arg0) {

//                System.out.println("invalidated");
            }});


        pane.visibleProperty().addListener(new ChangeListener<Boolean>(){

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                    Boolean arg1, Boolean arg2) {

                if(arg2){
//                    System.out.println("visible");
                    double pos = Settings.getInstance().getWinDividerPosition();
//                    System.out.println("set " + pos);
//                    splitter.setDividerPosition(0, pos);
                }

            }});
    }

    public void myInit(Stage stage){
        this.stage = stage;
    }

    public void applySettings(){
//        double pos = Settings.getInstance().getWinDividerPosition();
//        splitter.setDividerPositions(pos, 1.0 - pos);
    }

    public void collectSettings(){
        double pos[] = splitter.getDividerPositions();
//        System.out.println("collect " + pos[0]);
        Settings.getInstance().setWinDividerPosition(pos[0]);
    }

    public Image getAppIcon(){
        if(appIcon == null){
            appIcon = new Image(getClass().getResourceAsStream("/images/amor.png"));
            //appIcon = new Image(getClass().getResourceAsStream("/images/BPFolderRecipesGreen.png"));
        }

        return appIcon;
    }

    public void onMenuCloseAction(ActionEvent e){
        Settings.getLogger().info("application exit");
        Platform.exit();
    }

    public void onMenuOpenAction(ActionEvent e){

        if(fc == null){
            fc = new FileChooser();
        }

        File prev = new File(Settings.getInstance().getLastFilechooserLocation());
        if(prev.exists()){
            fc.setInitialDirectory(prev);
        }

        // http://java-buddy.blogspot.com/2012/03/javafx-20-disable-ower-window-for.html

        File file = fc.showOpenDialog(stage);

        if (file != null) {
            Settings.getInstance().setLastFilechooserLocation(file.getParent());
            //String path = file.getAbsolutePath();

            try{
                //webview.getEngine().load("file:///" + path);
                webview.getEngine().load(file.toURI().toString());
            }
            catch(Exception ex){
                Settings.getLogger().error("", e);
            }
        }
    }

    public void onMenuImport(ActionEvent e){
        doImport();
//
//        double pos = Settings.getInstance().getWinDividerPosition();
//        System.out.println("set " + pos);
//        splitter.setDividerPosition(0, pos);
    }

    public void onMenuMassImport(ActionEvent e){
        doMassImport();
    }

    public void onMenuHelpAbout(ActionEvent e){
        // http://stackoverflow.com/questions/8309981/how-to-create-and-show-common-dialog-error-warning-confirmation-in-javafx-2
        // http://java-buddy.blogspot.com/2012/02/dialog-with-close-button.html
        if(dialogStage == null){
            try{

                URL location = getClass().getResource("About.fxml");
                FXMLLoader loader = new FXMLLoader(location);

                Pane root = (Pane)loader.load();

                dialogStage = new Stage();
                dialogStage.setTitle("CookBook");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.setScene(new Scene(root, 500, 300));
                dialogStage.initOwner(stage);
                dialogStage.getIcons().add(getAppIcon());

                // http://stackoverflow.com/questions/13246211/javafx-how-to-get-stage-from-controller-during-initialization
                DialogController controller = loader.getController();
                controller.setStage(dialogStage);

            }
            catch(Exception ex){
                Settings.getLogger().error("", ex);
            }
        }
        if(dialogStage != null){
            dialogStage.show();
            //showModalMessage(dialogStage.sceneProperty().get().getRoot());
        }
    }

    public void onTitleChanged(KeyEvent e){
        buttonSave.setVisible(true);
        buttonRevert.setVisible(true);
    }

    public void onTagsChanged(KeyEvent e){
        buttonSave.setVisible(true);
        buttonRevert.setVisible(true);
    }

    public void onButtonSave(ActionEvent e){
        try {
            Database.getInstance().updateRecipe(currentRecipe.getHash(), title.getText().trim());

            String tagarray[] = tags.getText().split("[,;]");
            ArrayList<String> taglist = new ArrayList<>();
            for(String t: tagarray){
                taglist.add(t.trim().toLowerCase());
            }

            Database.getInstance().updateRecipeTags(currentRecipe.getHash(), taglist);

            buttonSave.setVisible(false);
            buttonRevert.setVisible(false);

            buildTree();
        } catch (Exception e1) {
            Settings.getLogger().error("", e);
        }
    }

    public void onButtonRevert(ActionEvent e){
        if(currentRecipe != null){
            setTitle(currentRecipe);
        }

        buttonSave.setVisible(false);
        buttonRevert.setVisible(false);
    }

    /**
     * Show the given node as a floating dialog over the whole application, with
     * the rest of the application dimmed out and blocked from mouse events.
     *
     * @param message
     */
    public void showModalMessage(Node message) {
        modalDimmer.getChildren().add(message);
        modalDimmer.setOpacity(0);
        modalDimmer.setVisible(true);
        modalDimmer.setCache(true);
        TimelineBuilder.create().keyFrames(
            new KeyFrame(Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        modalDimmer.setCache(false);
                    }
                },
                new KeyValue(modalDimmer.opacityProperty(),1, Interpolator.EASE_BOTH)
        )).build().play();
    }

    /**
     * Hide any modal message that is shown
     */
    public void hideModalMessage() {
        modalDimmer.setCache(true);
        TimelineBuilder.create().keyFrames(
            new KeyFrame(Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        modalDimmer.setCache(false);
                        modalDimmer.setVisible(false);
                        modalDimmer.getChildren().clear();
                    }
                },
                new KeyValue(modalDimmer.opacityProperty(),0, Interpolator.EASE_BOTH)
        )).build().play();
    }



    /**
     *
     * @param item
     * @param tag
     * @return true if any children were added
     */
    private boolean buildSubtree(TreeItem<RecipeTreeValue> item, Tag tag){

        boolean hasChildren = false;

        try {

            List<Tag> tags = Database.getInstance().getChildrenTags(tag.getVal());
            for(Tag t: tags){
                TreeItem<RecipeTreeValue> titem = new TreeItem<RecipeTreeValue>();
                titem.setValue(new RecipeTreeValue(t));
                item.getChildren().add(titem);
                buildSubtree(titem, t);

                hasChildren = true;
            }

            List<Recipe> recipes = Database.getInstance().getRecipesByTag(tag.getVal());
            for(Recipe r: recipes){
                //TreeItem<RecipeTreeValue> ritem = new TreeItem<RecipeTreeValue>(new RecipeTreeValue(r), new ImageView(recipeIcon));
                TreeItem<RecipeTreeValue> ritem = new TreeItem<RecipeTreeValue>(new RecipeTreeValue(r));
                item.getChildren().add(ritem);

                hasChildren = true;
            }

        } catch (Exception e) {
            Settings.getLogger().error("", e);
        }

        return hasChildren;
    }

    private Image tagIcon;
//    private Image recipeIcon;
    private Image favIcon;

    private void buildTree(){

        tree.getRoot().getChildren().clear();

        if(tagIcon == null){
            try{
            tagIcon = new Image(getClass().getResourceAsStream("/images/folder_yellow.png"));
            //recipeIcon = new Image(getClass().getResourceAsStream("/images/free_icon.png"));
            favIcon = new Image(getClass().getResourceAsStream("/images/metacontact_online.png"));
            }
            catch(Exception ex){
                Settings.getLogger().error("", ex);
            }
        }

        try {
            ArrayList<Tag> tags = Database.getInstance().getRootTags();
            for(Tag t: tags){

                TreeItem<RecipeTreeValue> item;
                if(t.getVal().equals("favorites")){
                    item = new TreeItem<RecipeTreeValue>(new RecipeTreeValue(t), new ImageView(favIcon));
                }
                else{
                    item = new TreeItem<RecipeTreeValue>(new RecipeTreeValue(t), new ImageView(tagIcon));
                }

                if(t.getSpecialid() == Tag.SPECIAL_OTHER){
                    List<Recipe> recipes = Database.getInstance().getRecipesWithoutTags();
                    for(Recipe r: recipes){
                        //TreeItem<RecipeTreeValue> ritem = new TreeItem<RecipeTreeValue>(new RecipeTreeValue(r), new ImageView(recipeIcon));
                        TreeItem<RecipeTreeValue> ritem = new TreeItem<RecipeTreeValue>(new RecipeTreeValue(r));
                        item.getChildren().add(ritem);
                    }
                    tree.getRoot().getChildren().add(item);
                }
                else{
                    if(buildSubtree(item, t)){
                        tree.getRoot().getChildren().add(item);
                    }
                }
            }

        } catch (Exception e) {
            Settings.getLogger().error("", e);
        }
    }

    private HtmlImporter importer;

    private ChangeListener<HtmlImporter.Status> importListener = new ChangeListener<HtmlImporter.Status>() {

        @Override
        public void changed(
                ObservableValue<? extends Status> observable,
                Status oldValue, Status newValue) {

            if(newValue == Status.Complete){
//                System.out.println("completed import of " + importer.getHash());

                RecipeLibrary.getInstance().validate();

                buildTree();
            }
            else{
//                System.out.println("importer status " + newValue);
            }
        }
    };

    private void doImport(){
        if(fc == null){
            fc = new FileChooser();
        }

        File prev = new File(Settings.getInstance().getLastFilechooserLocation());
        if(prev.exists()){
            fc.setInitialDirectory(prev);
        }

        final File file = fc.showOpenDialog(stage);
        if(file != null){
            Settings.getInstance().setLastFilechooserLocation(file.getParent());

            if(importer == null){
                importer = new HtmlImporter(importListener);
            }

            importer.importFile(file);
        }
    }

    ExecutorService executor;

    ChangeListener<Number> taskListener = new ChangeListener<Number>() {

        @Override
        public void changed(ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) {

//            System.out.println("- progress " + newValue);
        }
    };

    EventHandler<WorkerStateEvent> taskHandler = new EventHandler<WorkerStateEvent>() {

        @Override
        public void handle(WorkerStateEvent event) {

            //System.out.println("- task done");

            RecipeLibrary.getInstance().validate();
            buildTree();
        }};

    HtmlImporter massImporter;

    private void doMassImport(){

        DirectoryChooser dc = new DirectoryChooser();
        final File dir = dc.showDialog(stage);
        if(dir == null){
            return;
        }

//        System.out.println("- create importer");
        massImporter = new HtmlImporter();

        Task<Void> task = new MassImportTask(dir, massImporter);

        task.progressProperty().addListener(taskListener);
        task.setOnSucceeded(taskHandler);

        if(executor == null){
            executor = Executors.newCachedThreadPool();
        }

//        System.out.println("- submit task");
        executor.execute(task);
    }

    private Recipe currentRecipe;

    private void setRecipe(Recipe recipe){
        currentRecipe = recipe;

        String filename = Settings.getSettingsDirPath() + File.separator + Settings.RECIPES_SUBDIR +
                File.separator + recipe.getHash() + ".html";
        webview.getEngine().load(new File(filename).toURI().toString());

        setTitle(recipe);
    }

    private void setTitle(Recipe recipe){
        title.setText(recipe.getTitle());

        try {
            List<String> t = Database.getInstance().getRecipeTags(recipe.getHash());
            StringBuilder sb = new StringBuilder();
            for(String s: t){
                if(sb.length() > 0){
                    sb.append(", ");
                }
                sb.append(s);
            }
            tags.setText(sb.toString());
        } catch (Exception e) {
            Settings.getLogger().error("", e);
        }

    }

}
