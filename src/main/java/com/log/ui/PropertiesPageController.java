package com.log.ui;

import com.log.core.AppState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

import java.util.HashMap;

public class PropertiesPageController {

    AppState instance = AppState.getInstance();
    @FXML
    private ListView<String> categoriesListView;

    @FXML
    private ListView<String> propertiesListView;

    @FXML
    private Label propertiesLabel;

    @FXML
    private GridPane entriesGrid;

    private HashMap<String, ObservableList<String>> propertiesMap = instance.getPropertiesMap();

    private ObservableList<String> categories = instance.getCategories();
    @FXML
    public void initialize(){
        loadTempData();
        categoriesListView.setItems(categories);
        loadProperties();
        populateEntriesGrid();
    }

    //temporary data
    private void loadTempData() {
        propertiesMap.put("Physical",
                FXCollections.observableArrayList(
                        "Density",
                        "Open porosity"
        ));

        propertiesMap.put("Mechanical",
                FXCollections.observableArrayList(
                        "Tensile Strength",
                        "Tensile Modulus",
                        "Compressive Strength",
                        "Compressive Modulus",
                        "Flexural Strength",
                        "Flexural Modulus"
                ));

        propertiesMap.put("Thermal",
                FXCollections.observableArrayList(
                        "Specific Heat",
                        "Thermal Diffusivity",
                        "Thermal conductivity",
                        "Mass Loss(%)",
                        "Coefficient of thermal expansion"
                ));

        propertiesMap.put("Tribological",
                FXCollections.observableArrayList(
                        "Coefficient of friction",
                        "Wear Rate"
                ));

        propertiesMap.put("Microstructure",
                FXCollections.observableArrayList(
                        "ASTM grain size no.",
                        "Grain size"
                ));


    }

    private void loadProperties() {
        categoriesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldCategory, newCategory) -> {
                    if(newCategory != null)
                    {
                        propertiesListView.setItems(propertiesMap.get(newCategory));
                        propertiesLabel.setText(newCategory);
                    }
                });
    }

    int rowCount = 0;
    private void populateEntriesGrid() {
        int colCount = 0;
        Label label = new Label("Value");
        TextField field = new TextField();
        ComboBox<String> combo = new ComboBox<>();

        //styles
        label.getStyleClass().add("body-text");
        field.getStyleClass().add("input-field");
        combo.getStyleClass().add("combo-box");

        entriesGrid.add(label, colCount, rowCount);
        entriesGrid.add(field, colCount, rowCount + 1);
        entriesGrid.add(combo, colCount + 1, rowCount + 1);

        rowCount++;

        field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();   // prevent normal tab behavior
                populateEntriesGrid();
            }
        });

    }
}
