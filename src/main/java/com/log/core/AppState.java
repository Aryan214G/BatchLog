package com.log.core;

import com.log.model.DefaultProperty;
import com.log.model.Property;
import com.log.model.PropertyView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.time.LocalDate;
import java.util.Map;

//singleton class
//TODO: plan to use synchronized object creation
public class AppState {

    private static final AppState instance = new AppState();

    AppState() {
    }

    ;

    private HashMap<String, ObservableList<DefaultProperty>> categoriesMap = new HashMap<>();

    private ObservableList<String> categories = FXCollections.observableArrayList();

    public static AppState getInstance() {
        return instance;
    }

    public HashMap<String, ObservableList<DefaultProperty>> getCategoriesMap() {
        return categoriesMap;
    }

    public void setCategoriesMap(HashMap<String, ObservableList<DefaultProperty>> categoriesMap) {
        this.categoriesMap = categoriesMap;
    }

    public ObservableList<String> getCategories() {
        return categories;
    }

    public void setCategories(ObservableList<String> categories) {
        this.categories = categories;
    }

    private Map<Integer, Map<String, DefaultProperty>> defaultPropertiesMap
            = new HashMap<>();

    public Map<Integer, Map<String, DefaultProperty>> getDefaultPropertiesMap() {
        return defaultPropertiesMap;
    }

    public void setDefaultPropertiesMap(
            Map<Integer, Map<String, DefaultProperty>> defaultPropertiesMap
    ) {
        this.defaultPropertiesMap = defaultPropertiesMap;
    }


    //============ FLAGS =================
    private boolean projectCreated = false;

    public boolean isProjectCreated() {
        return projectCreated;
    }

    public void setProjectCreated(boolean projectCreated) {
        this.projectCreated = projectCreated;
    }

    public void clear() {

        categoriesMap.clear();

        categories.clear();

        defaultPropertiesMap.clear();

        projectCreated = false;
    }

}

