package com.log.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//singleton class
//TODO: plan to use synchronized object creation
public class AppState {

    private static final AppState instance = new AppState();

    AppState(){};

    //TODO: change the list to not be hardcoded
    private ObservableList<String> properties = FXCollections.observableArrayList(
            "Physical", "Mechanical", "Thermal", "Tribological", "Microstructure");

    public static AppState getInstance(){
        return instance;
    }

    public ObservableList<String> getProperties() {
        return properties;
    }

    public void setProperties(ObservableList<String> properties) {
        this.properties = properties;
    }
}
