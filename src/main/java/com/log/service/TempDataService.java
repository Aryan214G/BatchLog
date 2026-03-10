package com.log.service;

import com.log.core.AppState;
import com.log.core.DefaultMapState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TempDataService {

    private DefaultMapState Dmap = DefaultMapState.getInstance();
    private AppState instance = AppState.getInstance();

    public void loadCategoriesMap() {
        HashMap<String, ObservableList<String>> categoriesMap = instance.getCategoriesMap();

        categoriesMap.put("Physical",
                FXCollections.observableArrayList(
                        "Density",
                        "Open porosity"
                ));

        categoriesMap.put("Mechanical",
                FXCollections.observableArrayList(
                        "Tensile Strength",
                        "Tensile Modulus",
                        "Compressive Strength",
                        "Compressive Modulus",
                        "Flexural Strength",
                        "Flexural Modulus"
                ));

        categoriesMap.put("Thermal",
                FXCollections.observableArrayList(
                        "Specific Heat",
                        "Thermal Diffusivity",
                        "Thermal conductivity",
                        "Mass Loss(%)",
                        "Coefficient of thermal expansion"
                ));

        categoriesMap.put("Tribological",
                FXCollections.observableArrayList(
                        "Coefficient of friction",
                        "Wear Rate"
                ));

        categoriesMap.put("Microstructure",
                FXCollections.observableArrayList(
                        "ASTM grain size no.",
                        "Grain size"
                ));

        instance.setCategoriesMap(categoriesMap);
    }

    public ObservableList<String> loadCategoriesList(){
        ObservableList<String> categoriesList = FXCollections.observableArrayList();
        categoriesList.add("Physical");
        categoriesList.add("Mechanical");
        categoriesList.add("Thermal");
        categoriesList.add("Tribological");
        categoriesList.add("Micro Structure");
        return categoriesList;
    }

    public void loadDefaultRowsTempData() {

        HashMap<String, Integer> defaultRowsMap = Dmap.getDefaultRowsMap();

        // Physical
        defaultRowsMap.put("Density", 6);
        defaultRowsMap.put("Open porosity", 3);

        // Mechanical
        defaultRowsMap.put("Tensile Strength", 6);
        defaultRowsMap.put("Tensile Modulus", 6);
        defaultRowsMap.put("Compressive Strength", 6);
        defaultRowsMap.put("Compressive Modulus", 6);
        defaultRowsMap.put("Flexural Strength", 6);
        defaultRowsMap.put("Flexural Modulus", 6);

        // Thermal
        defaultRowsMap.put("Specific Heat", 3);
        defaultRowsMap.put("Thermal Diffusivity", 3);
        defaultRowsMap.put("Thermal conductivity", 3);
        defaultRowsMap.put("Mass Loss(%)", 5);
        defaultRowsMap.put("Coefficient of thermal expansion", 3);

        // Tribological
        defaultRowsMap.put("Coefficient of friction", 5);
        defaultRowsMap.put("Wear Rate", 5);

        // Microstructure
        defaultRowsMap.put("ASTM grain size no.", 3);
        defaultRowsMap.put("Grain size", 3);

        Dmap.setDefaultRowsMap(defaultRowsMap);
    }
}
