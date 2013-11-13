package org.sergeys.cookbook.ui;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.sergeys.cookbook.logic.Settings;

public class AboutController extends DialogController
{
    @FXML private Label lblProduct;
    @FXML private Label lblVersion;
    @FXML private Label lblAuthor;
    @FXML private Label lblJava;

    @FXML private TableView<SystemProperty> tableProps;

    @FXML private WebView webLibs;

    public void initialize(){
        super.initialize();

        lblProduct.setText("CookBook - the Recipe Book");
        String version = DateFormat.getDateInstance().format(Settings.getInstance().getCurrentVersion()) +
                String.format(" (rev. %s)", Settings.getInstance().getResources().getProperty("svn.revision"));
        lblVersion.setText(version);
        lblAuthor.setText("Sergey Selivanov");
        lblJava.setText(System.getProperties().getProperty("java.runtime.name") + " " +
                System.getProperties().getProperty("java.runtime.version"));

        Properties props = System.getProperties();
        final ArrayList<Entry<Object, Object>> p = new ArrayList<Entry<Object, Object>>(
                props.entrySet());
        Collections.sort(p, new Comparator<Entry<Object, Object>>() {
            @Override
            public int compare(Entry<Object, Object> o1,
                    Entry<Object, Object> o2) {
                return o1.getKey().toString().compareTo(o2.getKey().toString());
            }
        });

        //ObservableList<Entry<Object, Object>> ol = FXCollections.observableArrayList(p);
        ArrayList<SystemProperty> al = new ArrayList<>();
        for(Entry<Object, Object> e: p){
            al.add(new SystemProperty(e.getKey().toString(), e.getValue().toString()));
        }

        ObservableList<SystemProperty> ol = FXCollections.observableArrayList(al);

        tableProps.setItems(ol);

        tableProps.getColumns().clear();

        //tableProps.getColumns().get(0).setCellValueFactory();
        TableColumn<SystemProperty, String> col = new TableColumn<>();
        col.setCellValueFactory(new PropertyValueFactory<SystemProperty, String>("name"));
        col.setText("Property");

        tableProps.getColumns().add(col);

        col = new TableColumn<>();
        col.setCellValueFactory(new PropertyValueFactory<SystemProperty, String>("value"));
        col.setText("Value");

        tableProps.getColumns().add(col);

        String url = getClass().getResource("/resources/libraries.html").toString();
        WebEngine engine = webLibs.getEngine();
        engine.load(url);
    }

    public class SystemProperty{
        private String name;
        private String value;

        SystemProperty(String name, String value){
            this.setName(name);
            this.setValue(value);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
