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

    private String projectName;
    private String batchNo;
    private String productName;
    private String componentID;
    private LocalDate testDate;
    private String placeOfTesting;
    private String fileName;


    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getComponentID() { return componentID; }
    public void setComponentID(String componentID) { this.componentID = componentID; }

    public LocalDate getTestDate() { return testDate; }
    public void setTestDate(LocalDate testDate) { this.testDate = testDate; }

    public String getPlaceOfTesting() { return placeOfTesting; }
    public void setPlaceOfTesting(String placeOfTesting) { this.placeOfTesting = placeOfTesting; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

}
