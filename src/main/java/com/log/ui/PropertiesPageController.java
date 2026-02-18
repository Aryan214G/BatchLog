package com.log.ui;

import com.log.core.AppState;
import com.log.model.InputRow;
import com.log.model.InputRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private List<InputRow> inputRows = new ArrayList<>();

    private HashMap<String, ObservableList<String>> propertiesMap = instance.getPropertiesMap();

    private ObservableList<String> categories = instance.getCategories();
    @FXML
    public void initialize() throws IOException {
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



    //TODO: check if editing the values of previous fields update the inputRows
    int rowCount = 0;
    private void populateEntriesGrid() throws IOException {
        int colCount = 0;
//        Label label = new Label("Value");
        TextField field = new TextField();

        //component loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/log/ui/components/unitsDropdown.fxml"));
        Parent units = loader.load();
        UnitsDropdownController controller = loader.getController();

        //styles
//        label.getStyleClass().add("body-text");
        field.getStyleClass().add("input-field");


        entriesGrid.add(field, 0, rowCount);
        entriesGrid.add(units, 1, rowCount);

        inputRows.add(new InputRow(field, controller));
        rowCount++;

        field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER
                    && !field.getText().isBlank()
                    && inputRows.get(inputRows.size() - 1).getField() == field) {
                try {
                    populateEntriesGrid();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

}
