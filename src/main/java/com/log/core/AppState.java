package com.log.core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.time.LocalDate;

//singleton class
//TODO: plan to use synchronized object creation
public class AppState {

    private static final AppState instance = new AppState();

    AppState(){};

    private HashMap<String, ObservableList<String>> categoriesMap = new HashMap<>();

    //TODO: change the list to not be hardcoded
    //Todo:put this list in controller and read from that
    private ObservableList<String> categories = FXCollections.observableArrayList(
            "Physical", "Mechanical", "Thermal", "Tribological", "Microstructure");


    public static AppState getInstance() {
        return instance;
    }

    public HashMap<String, ObservableList<String>> getCategoriesMap() {
        return categoriesMap;
    }

    public void setCategoriesMap(HashMap<String, ObservableList<String>> categoriesMap) {
        this.categoriesMap = categoriesMap;
    }

    public ObservableList<String> getCategories() {
        return categories;
    }

    public void setCategories(ObservableList<String> categories) {
        this.categories = categories;
    }


    private String selectedCategory;
    private String selectedProperty;

    public String getSelectedCategory() { return selectedCategory; }
    public void setSelectedCategory(String selectedCategory) { this.selectedCategory = selectedCategory; }

    public String getSelectedProperty() { return selectedProperty; }
    public void setSelectedProperty(String selectedProperty) { this.selectedProperty = selectedProperty; }



    //============ FLAGS =================

    private boolean projectCreated = false;

    public boolean isProjectCreated() {
        return projectCreated;
    }

    public void setProjectCreated(boolean projectCreated) {
        this.projectCreated = projectCreated;
    }


}

