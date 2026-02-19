package com.log.ui;

import com.log.core.AppState;
import com.log.model.InputRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoriesPageController {

    @FXML
    private Button editButton;

    private ContextMenu editMenu;

    AppState instance = AppState.getInstance();

    @FXML
    private ListView<String> categoriesListView;

    @FXML
    private ListView<String> propertiesListView;

    @FXML
    private Label propertiesLabel;

    private HashMap<String, ObservableList<String>> categoriesMap = instance.getCategoriesMap();
    private ObservableList<String> categories = instance.getCategories();

    @FXML
    private GridPane entriesGrid;
    @FXML
    private InfoBarController infoBarController;

    private List<InputRow> inputRows = new ArrayList<>();

    private HashMap<String, Integer> defaultRowsMap = instance.getDefaultRowsMap();


    // ======================= END OF VARIABLES DECLARATION ==============================

    @FXML
    public void initialize() throws IOException {
        if (categoriesMap.isEmpty()) {
            loadTempData();
        }

        if(defaultRowsMap.isEmpty()) {
            loadDefaultRowsTempData();
        }

        categoriesListView.setItems(categories);
        loadProperties();


        // EDIT MENU SETUP
        MenuItem addItem = new MenuItem("Add Category");
        MenuItem deleteItem = new MenuItem("Delete Selected Category");

        addItem.setOnAction(e -> openAddCategoryPopup());
        deleteItem.setOnAction(e -> handleDeleteCategory());

        editMenu = new ContextMenu(addItem, deleteItem);
    }

    @FXML
    private void handleEditClick() {
        editMenu.show(editButton, Side.BOTTOM, 0, 0);
    }

    private void openAddCategoryPopup() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/AddCategoriesPopup.fxml")
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Category");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            AddCategoriesPopupController controller = loader.getController();
            String newCategory = controller.getEnteredCategory();
            ObservableList<String> newAttributes = controller.getAttributesList();
            if (newCategory != null && !newCategory.isBlank()) {

                if (!categories.contains(newCategory)) {
                    categories.add(newCategory);
                    categoriesMap.put(newCategory, newAttributes);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteCategory() {

        String selectedCategory =
                categoriesListView.getSelectionModel().getSelectedItem();

        if (selectedCategory == null) return;

        categories.remove(selectedCategory);
        categoriesMap.remove(selectedCategory);
    }

    //temporary data
    private void loadTempData() {
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

    private void loadDefaultRowsTempData() {

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

        instance.setDefaultRowsMap(defaultRowsMap);
    }


    private void loadProperties() {
        categoriesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldCategory, newCategory) -> {
                    if(newCategory != null)
                    {
                        propertiesListView.setItems(categoriesMap.get(newCategory));
                        propertiesLabel.setText(newCategory);
                        instance.setSelectedCategory(newCategory);

                        updateInfoBar();

                    }
                });
        propertiesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldProperty, newProperty) -> {

                    if (newProperty != null) {
                        instance.setSelectedProperty(newProperty);
                        int defaultRows = instance.getDefaultRowsMap().get(newProperty);
                        for (int i = 0; i < defaultRows; i++) {
                            try {
                                addInputRows(i);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        updateInfoBar();
                    }
                });
    }

    private void updateInfoBar() {
        if (infoBarController != null) {
            infoBarController.refresh();
        }
    }




    //TODO: check if editing the values of previous fields update the inputRows
    private void addInputRows(int rowCount) throws IOException {

        TextField field = new TextField();
        field.getStyleClass().add("input-field");

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/log/ui/components/unitsDropdown.fxml")
        );

        Parent units = loader.load();
        UnitsDropdownController controller = loader.getController();

        entriesGrid.add(field, 0, rowCount);
        if(rowCount < 1)
        {
            entriesGrid.add(units, 1, rowCount);
        }

        inputRows.add(new InputRow(field, controller));

        // ENTER adds new row dynamically
        field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER
                    && !field.getText().isBlank()
                    && inputRows.get(inputRows.size() - 1).getField() == field) {

                try {
                    addInputRows(rowCount+1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


}
