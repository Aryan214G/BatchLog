package com.log.ui;

import com.log.core.AppState;
import com.log.core.BasePropertiesState;
import com.log.core.SelectedState;
import com.log.database.DBUtil;
import com.log.model.*;
import com.log.service.*;
import com.log.ui.util.AlertUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Side;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesPageController {

    @FXML
    private Button editButton;

    private ContextMenu editMenu;

    AppState instance = AppState.getInstance();
    SelectedState selectedState = SelectedState.getInstance();
    BasePropertiesState basePropertiesState = BasePropertiesState.getInstance();
    private DefaultPropertyService defaultPropertyService = new DefaultPropertyService();
    private Map<Integer, Map<String, DefaultProperty>> defaultPropertiesMap;
    @FXML
    private ListView<String> categoriesListView;

    @FXML
    private ListView<PropertyView> propertiesListView;

    @FXML
    private Label propertiesLabel;

    private HashMap<String, ObservableList<PropertyView>> categoriesMap = instance.getCategoriesMap();
    private ObservableList<String> categories = instance.getCategories();



    @FXML
    private GridPane entriesGrid;

    @FXML
    private HBox headerBox;

    @FXML
    private Button submitButton;

    @FXML
    private InfoBarController infoBarController;

    private List<InputRow> inputRows = new ArrayList<>();
    List<Property> propertiesList;
    @FXML
    private Button printButton;

    private TextField temperatureField;
    private UnitsDropdownController tempUnitController;
    private DirectionDropdownController directionController;

    // === import services ===//
    private PropertyService propertyService = new PropertyService();
    private TemperatureService temperatureService = new TemperatureService();
    private DirectionService directionService = new DirectionService();
    private CategoryService categoryService = new CategoryService();
    private UnitsService unitsService = new UnitsService();
    private BatchService batchService = new BatchService();
    private PropertySubmissionService propertySubmissionService = new PropertySubmissionService(
            temperatureService,
            directionService,
            categoryService,
            unitsService,
            propertyService
            );
    // ======================= END OF VARIABLES DECLARATION ==============================

    @FXML
    public void initialize() throws IOException, SQLException {

        //disable UI components when project is not created
            isSubmitButtonVisible(false);
        if(!instance.isProjectCreated()) {
            categoriesListView.setDisable(true);
            propertiesListView.setDisable(true);
        }
        else {
            loadCategoriesFromDB();
            loadPropertiesFromDB();
        }



        CategorySelectionListener();
        PropertySelectionListener();
        defaultPropertiesMap = defaultPropertyService.getDefaultsGrouped();


        // EDIT MENU SETUP
        MenuItem addItem = new MenuItem("Add Category");
        MenuItem deleteItem = new MenuItem("Delete Selected Category");

        //TODO: un-comment later
//        addItem.setOnAction(e -> openAddCategoryPopup());
        deleteItem.setOnAction(e -> handleDeleteCategory());

        editMenu = new ContextMenu(addItem, deleteItem);
    }

    // Method used to restore values from the exisiting data (if any) of currently selected batch/record, and load them into the ui.
    private void loadPropertiesFromDB() throws SQLException {
        int testId = basePropertiesState.getTestId();
        propertiesList = propertyService.getPropertiesByTest(testId);

        //=========== iterate through propertiesViews and store in statemanager ================
        inputRows.clear();
        for(Property property : propertiesList){

            // ============== Populate input rows =================
            try (Connection conn = DBUtil.getConnection()) {
                populateInputRowsHelper(property, conn);
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }


            //================= save state ==================
            stateManager.saveState(
                    property.getPropertyName(),
                    new ArrayList<>(inputRows),
                    property.getTemperature(),
                    property.getDirection(),
                    unit
            );
        }
    }

    public void populateInputRowsHelper(Property property, Connection conn){

        int propertyID = property.getPropertyID();
        List<PropertyValue> propertyValues  = propertyService.getValuesByProperty(conn, propertyID);

        for(PropertyValue value : propertyValues){
            InputRow inputRow = new InputRow();
            inputRow.setPropertyValue(value);

            inputRows.add(inputRow);
        }
    }


    private void isSubmitButtonVisible(boolean value){
        submitButton.setVisible(value);
        submitButton.setManaged(value);
    }

    @FXML
    private void handleEditClick() {
        if(instance.isProjectCreated())
        {
        editMenu.show(editButton, Side.BOTTOM, 0, 0);
        }
        else if(!instance.isProjectCreated())
        {
            AlertUtil.showError("Please create a project before editing categories.");
        }

    }

    // ======================= CATEGORY POPUP ==============================

    //TODO: resolve error and un comment
//    private void openAddCategoryPopup() {
//
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getResource("/com/log/ui/views/AddCategoriesPopup.fxml")
//            );
//
//            Parent root = loader.load();
//
//            Stage stage = new Stage();
//            stage.setTitle("Add Category");
//            stage.setScene(new Scene(root));
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.showAndWait();
//
//            AddCategoriesPopupController controller = loader.getController();
//
//            String newCategory = controller.getEnteredCategory();
//            ObservableList<PropertyView> newProperties = controller.getPropertiesList();
//            HashMap<String,Integer> attrEntriesMap = controller.getEntriesMap();
//            if (newCategory != null && !newCategory.isBlank()) {
//
//                if (!categories.contains(newCategory)) {
//
//                    categoryService.createCategory(newCategory);   // INSERT INTO DB
//
//                    loadCategoriesFromDB(); // refresh state
//
//                    categoriesMap.put(newCategory, newProperties);
//                }
//                for (Map.Entry<String, Integer> entry : attrEntriesMap.entrySet()) {
//
//                    String key = entry.getKey();
//                    Integer value = entry.getValue();
//
//                    defaultRowsMap.put(key,value);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void handleDeleteCategory() {

        String selectedCategory =
                categoriesListView.getSelectionModel().getSelectedItem();

        if (selectedCategory == null) return;

        List<Category> dbCategories = categoryService.getAllCategories();

        for (Category c : dbCategories) {

            if (c.getCategoryName().equals(selectedCategory)) {

                categoryService.deleteCategory(c.getCategoryId());
                break;
            }
        }

        loadCategoriesFromDB();
    }

    private void CategorySelectionListener() {

        categoriesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldCategory, newCategory) -> {
                    if(newCategory != null)
                    {
                        HandleCategoryChange(newCategory);
                    }
                });
    }

    ObservableList<PropertyView> properties;
    private void HandleCategoryChange(String newCategory){
        saveCurrentPropertyValues(selectedState.getSelectedProperty());
        clearUIComponents();

        properties = instance.getCategoriesMap().get(newCategory);

        System.out.println("Properties loaded size: " + properties.size());
        System.out.println("Category: " + newCategory);
        System.out.println("Properties loaded: " + properties);

        propertiesListView.setItems(properties);
        propertiesLabel.setText(newCategory);
        selectedState.setSelectedCategory(newCategory);

        try {
            Category categoryObj = categoryService.getCategory(
                    DBUtil.getConnection(),
                    newCategory
            );

            if (categoryObj != null) {
                selectedState.setSelectedCategoryId(categoryObj.getCategoryId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        updateInfoBar();

    }
    private void PropertySelectionListener() {

        propertiesListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldProperty, newProperty) -> {

                    if (newProperty != null)
                    {
                        HandlePropertyChange(newProperty,oldProperty);
                    }
                });
    }


    private void HandlePropertyChange(PropertyView newProperty,PropertyView oldProperty){
        if(oldProperty != null) { saveCurrentPropertyValues(oldProperty); }

        clearUIComponents();
        inputRows.clear();
        isSubmitButtonVisible(true);

        selectedState.setSelectedProperty(newProperty);

        int categoryId = selectedState.getSelectedCategoryId();
        String propertyName = newProperty.getPropertyName();

        DefaultProperty dp = null;

        if (defaultPropertiesMap.containsKey(categoryId)) {
            dp = defaultPropertiesMap.get(categoryId).get(propertyName);
        }

        int defaultRows = (dp != null) ? dp.getRows() : 1;
        try {
            loadMetrics();
            addHeaderControls();
            loadPropertyFields(defaultRows, newProperty.getPropertyName(), dp);
            updateMetrics();   // force refresh after load
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        updateInfoBar();
    }

    private void updateInfoBar() {
        if (infoBarController != null) {
            infoBarController.refresh();
        }
    }



    private void addInputRow(int rowCount, String property, DefaultProperty dp, InputRow inputRow) throws IOException {

        TextField field = new TextField();

        field.getStyleClass().add("input-field");


        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/log/ui/components/unitsDropdown.fxml")
        );

        Parent units = loader.load();
        UnitsDropdownController controller = loader.getController();
        controller.setUnits(property);
        if (dp != null && rowCount == 0) {
            String unitName = unitsService.getUnitNameById(dp.getUnitId());
            controller.setSelectedUnit(unitName);
        }

        entriesGrid.add(field, 0, rowCount);
        if(rowCount < 1)
        {
            entriesGrid.add(units, 1, rowCount);
        }

        inputRow.setField(field);
        inputRow.setUnitController(controller);
        inputRows.add(inputRow);

        // ENTER adds new row dynamically
        field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER
                    && !field.getText().isBlank()
                    && inputRows.get(inputRows.size() - 1).getField() == field) {

                try {
                    addInputRow(rowCount+1, property, dp, new InputRow());
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

    @FXML
    private void PrintButtonHandler() {

            boolean hasText = inputRows.stream()
                    .anyMatch(row ->
                            row.getField().getText() != null &&
                                    !row.getField().getText().trim().isEmpty()
                    );

            if (!hasText) {
                AlertUtil.showWarning("Please enter at least one value before printing.");
                return;
            }

            System.out.println("Printing...");
        }


    private final PropertyStateManager stateManager = new PropertyStateManager();

    private void saveCurrentPropertyValues(PropertyView property) {

        if (property == null || temperatureField == null) {
            return;
        }

        for (InputRow row : inputRows) {
            double propertyVal = Double.parseDouble(row.getField().getText());

                row.getPropertyValue().setPropertyVAL(propertyVal);
        }
        int tempVal = 0;

        String text = temperatureField.getText();

        if (text != null && !text.isBlank()) {
            try {
                tempVal = Integer.parseInt(text.trim());
            } catch (NumberFormatException e) {
                AlertUtil.showError("Please enter a valid temperature.");
            }
        }

        int tempUnitID;
        String tempUnitVal;
        try (Connection conn = DBUtil.getConnection()) {

            tempUnitVal = tempUnitController.getComboBox().getValue();

            if (tempUnitVal == null) {
                throw new IllegalStateException("Temperature unit not selected");
            }

            Unit unit = unitsService.getUnit(conn, tempUnitVal);

            if (unit == null) {
                throw new IllegalStateException("Unit not found in DB: " + tempUnitVal);
            }

            tempUnitID = unit.getUnitId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Unit
        String unitValue = unitValue = inputRows.get(0)
                    .getUnitController()
                    .getComboBox()
                    .getValue();
        Unit unit = new Unit();
        unit.setUnit(unitValue);

        stateManager.saveState(
                property.getPropertyName(),
                inputRows,
                new Temperature(tempVal, tempUnitID, tempUnitVal),
                new Direction(directionController.getSelectedDirection()),
                unit
        );
    }



    private void loadPropertyFields(int defaultRows, String property, DefaultProperty dp) throws IOException {

        inputRows.clear();
        entriesGrid.getChildren().clear();

        PropertyState state = stateManager.getState(property);

        // ================= NO SAVED STATE =================
        if (state == null) {

            for (int i = 0; i < defaultRows; i++) {
                addInputRow(i, property, dp, new InputRow());
            }

            return;
        }

        // ================= RESTORE HEADER =================
        String temp = String.valueOf(state.getTemperature().getTempVal());

        temperatureField.setText(temp);
        tempUnitController.setSelectedUnit(state.getTemperature().getTempUnitVal());
        directionController.setSelectedDirection(state.getDirection().getDirVal());

        // ================= RESTORE ROWS =================
        for (int i = 0; i < defaultRows; i++) {

            InputRow inputRow;

            if (i < state.getInputRows().size()) {
                inputRow = state.getInputRows().get(i);
            } else {
                inputRow = new InputRow();
            }
            addInputRow(i, property, dp, inputRow);

            PropertyValue pv = inputRow.getPropertyValue();

            String inputValue = (pv != null)
                    ? String.valueOf(pv.getPropertyVAL())
                    : "";
            inputRow.getField().setText(inputValue);

            String unit = state.getUnit().getUnit();

            inputRow.getUnitController().setSelectedUnit(unit);
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
        isSubmitButtonVisible(false);

        if (metrics != null) {
            entriesPanel.getChildren().remove(metrics);
            metrics = null;
            metricsController = null;
        }
    }

    private void loadCategoriesFromDB() {
        categoryService.refreshCategoriesState();
        categoriesMap = instance.getCategoriesMap();
        categoriesListView.setItems(instance.getCategories());
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

    public void handleEntrySubmit(ActionEvent actionEvent) {
        PropertyView selectedProperty = selectedState.getSelectedProperty();
        saveCurrentPropertyValues(selectedProperty);

        PropertyState propertyState =
                stateManager.getState(selectedProperty.getPropertyName());

        propertySubmissionService.submit(
                propertyState,
                selectedProperty.getPropertyName(),
                selectedState.getSelectedCategory()
        );
    }

}