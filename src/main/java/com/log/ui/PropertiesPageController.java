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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PropertiesPageController {

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

    private HashMap<String, ObservableList<String>> propertiesMap = instance.getPropertiesMap();
    private ObservableList<String> categories = instance.getCategories();

    @FXML
    private GridPane entriesGrid;
    @FXML
    private InfoBarController infoBarController;

    private List<InputRow> inputRows = new ArrayList<>();


    @FXML
    public void initialize() throws IOException {
        if (propertiesMap.isEmpty()) {
            loadTempData();
        }

        categoriesListView.setItems(categories);
        loadProperties();
        populateEntriesGrid();


        categoriesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldCategory, newCategory) -> {
                    if (newCategory != null) {
                        propertiesListView.setItems(propertiesMap.get(newCategory));
                        propertiesLabel.setText(newCategory);
                    }
                });

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
                    getClass().getResource("/com/log/ui/views/AddPropertiesPopup.fxml")
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Category");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            AddPropertiesPopupController controller = loader.getController();
            String newCategory = controller.getEnteredCategory();
            ObservableList<String> newAttributes = controller.getAttributesList();
            if (newCategory != null && !newCategory.isBlank()) {

                if (!categories.contains(newCategory)) {
                    categories.add(newCategory);
                    propertiesMap.put(newCategory, newAttributes);
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
        propertiesMap.remove(selectedCategory);
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
