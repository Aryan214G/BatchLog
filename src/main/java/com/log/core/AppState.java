package com.log.core;

import com.log.model.PropertyView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.time.LocalDate;

//singleton class
//TODO: plan to use synchronized object creation
public class AppState {

    private static final AppState instance = new AppState();

    AppState(){};

    private HashMap<String, ObservableList<PropertyView>> categoriesMap = new HashMap<>();


    //TODO: change the list to not be hardcoded
    //Todo:put this list in controller and read from that
    private ObservableList<String> categories = FXCollections.observableArrayList();

    public static AppState getInstance() {
        return instance;
    }

    public HashMap<String, ObservableList<PropertyView>> getCategoriesMap() {
        return categoriesMap;
    }

    public void setCategoriesMap(HashMap<String, ObservableList<PropertyView>> categoriesMap) {
        this.categoriesMap = categoriesMap;
    }

    public ObservableList<String> getCategories() {
        return categories;
    }

    public void setCategories(ObservableList<String> categories) {
        this.categories = categories;
    }


    //============ FLAGS =================
    private boolean projectCreated = false;

    public boolean isProjectCreated() {
        return projectCreated;
    }

    public void setProjectCreated(boolean projectCreated) {
        this.projectCreated = projectCreated;
    }


}

