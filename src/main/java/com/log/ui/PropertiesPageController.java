package com.log.ui;

import com.log.core.AppState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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


        //lambda function
//        propertiesListView.setCellFactory(listView-> new ListCell<>(){
//            private final Button button = new Button();
//
//            //initializer block in place of default constructor
//            {
//                button.setMaxWidth(Double.MAX_VALUE);
//            }
//
//            @Override
//            protected void updateItem(String item, boolean empty)
//            {
//                super.updateItem(item, empty);
//
//                if(empty || item == null)
//                {
//                    setGraphic(null);
//                }
//                else{
//                    button.setText(item);
//                    setGraphic(button);
//                }
//            }
//        });
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

}
