package com.log.ui;

import com.log.core.AppState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.HashMap;

public class PropertiesPageController {

    AppState instance = AppState.getInstance();
    @FXML
    private ListView<String> categoriesListView;

    @FXML
    private ListView<String> propertiesListView;

    @FXML
    private Label propertiesLabel;

    private HashMap<String, ObservableList<String>> propertiesMap = instance.getPropertiesMap();

    private ObservableList<String> categories = instance.getCategories();



    @FXML
    private InfoBarController infoBarController;   // controller





    @FXML
    public void initialize(){
        loadTempData();
        categoriesListView.setItems(categories);

        categoriesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldCategory, newCategory) -> {
                    if(newCategory != null)
                    {
                        propertiesListView.setItems(propertiesMap.get(newCategory));
                        propertiesLabel.setText(newCategory);
                        instance.setSelectedCategory(newCategory);

                        updateInfoBar();

                    }
                });
        propertiesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {

                    if (newVal != null) {
                        instance.setSelectedProperty(newVal);
                        updateInfoBar();
                    }
                });

    }

    private void updateInfoBar() {
        if (infoBarController != null) {
            infoBarController.refresh();
        }
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

}
