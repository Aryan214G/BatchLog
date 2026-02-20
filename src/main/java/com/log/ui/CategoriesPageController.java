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
import javafx.scene.layout.HBox;
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

    private final AppState instance = AppState.getInstance();

    @FXML
    private ListView<String> categoriesListView;

    @FXML
    private ListView<String> propertiesListView;

    @FXML
    private Label propertiesLabel;

    @FXML
    private GridPane entriesGrid;

    @FXML
    private HBox headerBox;

    @FXML
    private InfoBarController infoBarController;

    private final HashMap<String, ObservableList<String>> categoriesMap = instance.getCategoriesMap();
    private final ObservableList<String> categories = instance.getCategories();
    private final HashMap<String, Integer> defaultRowsMap = instance.getDefaultRowsMap();

    private final List<InputRow> inputRows = new ArrayList<>();

    // ======================= INITIALIZE ==============================

    @FXML
    public void initialize() {

        if (categoriesMap.isEmpty()) {
            loadTempData();
        }

        if (defaultRowsMap.isEmpty()) {
            loadDefaultRowsTempData();
        }

        categoriesListView.setItems(categories);
        loadProperties();

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

    // ======================= CATEGORY POPUP ==============================

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

    // ======================= PROPERTY SELECTION ==============================

    private void loadProperties() {

        categoriesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldCategory, newCategory) -> {

                    if (newCategory != null) {
                        entriesGrid.getChildren().clear();
                        headerBox.getChildren().clear();
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

                        entriesGrid.getChildren().clear();
                        headerBox.getChildren().clear();
                        inputRows.clear();

                        instance.setSelectedProperty(newProperty);

                        try {
                            addHeaderControls();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        int defaultRows =
                                instance.getDefaultRowsMap().get(newProperty);

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

    // ======================= HEADER CONTROLS ==============================

    private void addHeaderControls() throws IOException {

        headerBox.getChildren().clear();

        // Temperature Field
        TextField temperatureField = new TextField();
        temperatureField.setPromptText("Temperature");
        temperatureField.getStyleClass().add("input-field");

        // Temperature Unit Dropdown
        FXMLLoader unitLoader = new FXMLLoader(
                getClass().getResource("/com/log/ui/components/unitsDropdown.fxml")
        );
        Parent tempUnitNode = unitLoader.load();

        // Direction Dropdown
        FXMLLoader directionLoader = new FXMLLoader(
                getClass().getResource("/com/log/ui/components/directionDropdown.fxml")
        );
        Parent directionNode = directionLoader.load();

        headerBox.getChildren().addAll(
                temperatureField,
                tempUnitNode,
                directionNode
        );
    }

    // ======================= VALUE ROWS ==============================

    private void addInputRows(int rowIndex) throws IOException {

        TextField field = new TextField();
        field.getStyleClass().add("input-field");

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/log/ui/components/unitsDropdown.fxml")
        );

        Parent units = loader.load();
        UnitsDropdownController controller = loader.getController();

        entriesGrid.add(field, 0, rowIndex);

        // First value row gets units
        if (rowIndex == 0) {
            entriesGrid.add(units, 1, rowIndex);
        }

        inputRows.add(new InputRow(field, controller));

        field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER
                    && !field.getText().isBlank()
                    && inputRows.get(inputRows.size() - 1).getField() == field) {

                try {
                    addInputRows(entriesGrid.getRowCount());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // ======================= TEMP DATA ==============================

    private void loadTempData() {

        categoriesMap.put("Physical",
                FXCollections.observableArrayList(
                        "Density",
                        "Open porosity"
                ));

        categoriesMap.put("Thermal",
                FXCollections.observableArrayList(
                        "Specific Heat",
                        "Thermal conductivity"
                ));

        instance.setCategoriesMap(categoriesMap);
    }

    private void loadDefaultRowsTempData() {

        defaultRowsMap.put("Density", 6);
        defaultRowsMap.put("Open porosity", 3);
        defaultRowsMap.put("Specific Heat", 3);
        defaultRowsMap.put("Thermal conductivity", 3);

        instance.setDefaultRowsMap(defaultRowsMap);
    }
}