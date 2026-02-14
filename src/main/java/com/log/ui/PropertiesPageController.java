package com.log.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class PropertiesPageController {

    @FXML
    private ListView<String> propertiesListView;

    private ObservableList<String> properties = FXCollections.observableArrayList(
            "property 1", "property 2", "property 3");

    @FXML
    public void initialize(){
        propertiesListView.setItems(properties);
    }
}
