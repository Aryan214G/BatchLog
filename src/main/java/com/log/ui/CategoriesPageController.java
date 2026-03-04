package com.log.ui;

import com.log.core.AppState;
import com.log.core.DefaultMapState;
import com.log.core.SelectedState;
import com.log.model.PropertyState;
import com.log.model.Reading;
import com.log.service.PropertyStateManager;
import com.log.service.StatisticsService;
import com.log.service.TempDataService;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesPageController {

    @FXML
    private Button editButton;

    private ContextMenu editMenu;

    AppState instance = AppState.getInstance();
    DefaultMapState DMapInstance = DefaultMapState.getInstance();
    SelectedState selectedState = SelectedState.getInstance();

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
    private HBox headerBox;

    @FXML
    private InfoBarController infoBarController;

    private List<InputRow> inputRows = new ArrayList<>();

    private HashMap<String, Integer> defaultRowsMap = DMapInstance.getDefaultRowsMap();

    private HashMap<String, String> defaultUnits = DMapInstance.getDefaultUnitsMap();
    @FXML
    private Button printButton;

    private TextField temperatureField;
    private UnitsDropdownController tempUnitController;
    private DirectionDropdownController directionController;

    private TempDataService tempDataService = new TempDataService();

    // ======================= END OF VARIABLES DECLARATION ==============================

    @FXML
    public void initialize() throws IOException {

        if(!instance.isProjectCreated()) {
            categoriesListView.setDisable(true);
            propertiesListView.setDisable(true);
        }

        loadTempData();

        categoriesListView.setItems(categories);
        loadProperties();


        // EDIT MENU SETUP
        MenuItem addItem = new MenuItem("Add Category");
        MenuItem deleteItem = new MenuItem("Delete Selected Category");

        addItem.setOnAction(e -> openAddCategoryPopup());
        deleteItem.setOnAction(e -> handleDeleteCategory());

        editMenu = new ContextMenu(addItem, deleteItem);
    }

    private void loadTempData(){
        tempDataService.loadTempData();
        categoriesMap = instance.getCategoriesMap();

        tempDataService.loadDefaultRowsTempData();
        defaultRowsMap = DMapInstance.getDefaultRowsMap();
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
            HashMap<String,Integer> attrEntriesMap = controller.getEntriesMap();
            if (newCategory != null && !newCategory.isBlank()) {

                if (!categories.contains(newCategory)) {
                    categories.add(newCategory);
                    categoriesMap.put(newCategory, newAttributes);
                }
                for (Map.Entry<String, Integer> entry : attrEntriesMap.entrySet()) {

                    String key = entry.getKey();
                    Integer value = entry.getValue();

                    defaultRowsMap.put(key,value);
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



    //TODO: loadProperties is doing multiple tasks. Either change the name of the method or divide the responsibilities
    private void loadProperties() {
        categoriesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldCategory, newCategory) -> {
                    if(newCategory != null)
                    {
                        saveCurrentPropertyValues(selectedState.getSelectedProperty());

                        clearUIComponents();
                        propertiesListView.setItems(categoriesMap.get(newCategory));
                        propertiesLabel.setText(newCategory);
                        selectedState.setSelectedCategory(newCategory);

                        updateInfoBar();

                    }
                });
        propertiesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldProperty, newProperty) -> {

                    if (newProperty != null) {

                        saveCurrentPropertyValues(oldProperty);
                        clearUIComponents();
                        inputRows.clear();

                        selectedState.setSelectedProperty(newProperty);

                        int defaultRows = DMapInstance.getDefaultRowsMap().get(newProperty);

                        try {
                            loadMetrics();
                            addHeaderControls();
                            loadPropertyFields(defaultRows, newProperty);
                            updateMetrics();   // force refresh after load
                        } catch (IOException e) {
                            throw new RuntimeException(e);
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
    private void addInputRow(int rowCount, String property) throws IOException {

        TextField field = new TextField();
        field.getStyleClass().add("input-field");

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/log/ui/components/unitsDropdown.fxml")
        );

        Parent units = loader.load();
        UnitsDropdownController controller = loader.getController();
        controller.setUnits(property);

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
                    addInputRow(rowCount+1, property);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        field.textProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Text changed");
            System.out.println("metricsController = " + metricsController);

            if (metricsController != null) {
                updateMetrics();
            }
        });
    }


    // ======================= HEADER CONTROLS ==============================

    private void addHeaderControls() throws IOException {

        headerBox.getChildren().clear();

        temperatureField = new TextField();
        temperatureField.setPromptText("Temperature");
        temperatureField.getStyleClass().add("input-field");

        FXMLLoader unitLoader = new FXMLLoader(
                getClass().getResource("/com/log/ui/components/unitsDropdown.fxml")
        );
        Parent tempUnitNode = unitLoader.load();
        tempUnitController = unitLoader.getController();

        FXMLLoader directionLoader = new FXMLLoader(
                getClass().getResource("/com/log/ui/components/directionDropdown.fxml")
        );
        Parent directionNode = directionLoader.load();
        directionController = directionLoader.getController();

        headerBox.getChildren().addAll(
                temperatureField,
                tempUnitNode,
                directionNode
        );
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void PrintButtonHandler() {

            boolean hasText = inputRows.stream()
                    .anyMatch(row ->
                            row.getField().getText() != null &&
                                    !row.getField().getText().trim().isEmpty()
                    );

            if (!hasText) {
                showAlert("Please enter at least one value before printing.");
                return;
            }

            System.out.println("Printing...");
        }


    private final PropertyStateManager stateManager = new PropertyStateManager();

    private void saveCurrentPropertyValues(String property) {

        if (property == null || temperatureField == null) {
            return;
        }

        List<Reading> readings = new ArrayList<>();

        for (InputRow row : inputRows) {
            readings.add(new Reading(
                    row.getField().getText(),
                    row.getUnitController().getComboBox().getValue()
            ));
        }

        stateManager.saveState(
                property,
                readings,
                temperatureField.getText(),
                tempUnitController.getComboBox().getValue(),
                directionController.getSelectedDirection()
        );
    }

    private void loadPropertyFields(int defaultRows, String property) throws IOException {

        inputRows.clear();
        entriesGrid.getChildren().clear();

        PropertyState state = stateManager.getState(property);

        if (state == null) {
            for (int i = 0; i < defaultRows; i++) {
                addInputRow(i, property);
            }
            return;
        }

        temperatureField.setText(state.getTemperature());
        tempUnitController.setSelectedUnit(state.getTemperatureUnit());
        directionController.setSelectedDirection(state.getDirection());

        for (int i = 0; i < state.getReadings().size(); i++) {

            addInputRow(i, property);

            inputRows.get(i).getField()
                    .setText(state.getReadings().get(i).getValue());

            inputRows.get(i).getUnitController()
                    .setSelectedUnit(state.getReadings().get(i).getUnit());
        }
    }


    @FXML
    private VBox entriesPanel;

    private MetricsController metricsController;

    private Parent metrics;
    private void loadMetrics() throws IOException {

        if (metrics != null) return;
        int index = entriesPanel.getChildren().indexOf(headerBox);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/log/ui/components/metrics.fxml")
        );

        metrics = loader.load();
        metricsController = loader.getController();
        entriesPanel.getChildren().add(index, metrics);

    }
    private void updateMetrics() {

        if (metricsController == null) return;

        List<Double> values = getCurrentPropertyValues();

        if (values.isEmpty()) {
            metricsController.setMean(0);
            metricsController.setStandardDeviation(0);
            return;
        }

        double mean = StatisticsService.mean(values);
        double sd = StatisticsService.standardDeviation(values);

        metricsController.setMean(mean);
        metricsController.setStandardDeviation(sd);
    }

    private void clearUIComponents(){
        headerBox.getChildren().clear();
        entriesGrid.getChildren().clear();

        if (metrics != null) {
            entriesPanel.getChildren().remove(metrics);
            metrics = null;
            metricsController = null;
        }
    }

    private List<Double> getCurrentPropertyValues() {

        List<Double> values = new ArrayList<>();

        for (InputRow row : inputRows) {

            String text = row.getField().getText();

            if (text != null && !text.isBlank()) {
                try {
                    values.add(Double.parseDouble(text));
                } catch (NumberFormatException ignored) {
                    // ignore invalid numbers
                }
            }
        }

        return values;
    }
}