package com.log.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

//singleton class
//TODO: plan to use synchronized object creation
public class AppState {

    private static final AppState instance = new AppState();

    AppState() {
    }

    private HashMap<String, ObservableList<String>> propertiesMap = new HashMap<>();

    //TODO: change the list to not be hardcoded
    private ObservableList<String> categories = FXCollections.observableArrayList(
            "Physical", "Mechanical", "Thermal", "Tribological", "Microstructure");


    public static AppState getInstance() {
        return instance;
    }

    public HashMap<String, ObservableList<String>> getPropertiesMap() {
        return propertiesMap;
    }

    public void setPropertiesMap(HashMap<String, ObservableList<String>> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public ObservableList<String> getCategories() {
        return categories;
    }

    public void setCategories(ObservableList<String> categories) {
        this.categories = categories;
    }
}

