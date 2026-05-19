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

    @FXML
    private SearchBarController searchBarController;

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
    private TemperatureUnitService temperatureUnitService = new TemperatureUnitService();
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
            loadPropertyDataFromDB();
            setupSearchBar();
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
    private void setupSearchBar() {

        TextField searchField =
                searchBarController.getSearchField();

        ContextMenu suggestionsPopup =
                new ContextMenu();

        // ================= AUTOCOMPLETE =================
        searchField.textProperty().addListener(
                (obs, oldVal, newVal) -> {

                    if (newVal == null || newVal.isBlank()) {

                        suggestionsPopup.hide();
                        return;
                    }

                    String searchText =
                            newVal.trim().toLowerCase();

                    List<MenuItem> suggestions =
                            new ArrayList<>();

                    // ===== CATEGORY SUGGESTIONS =====
                    for (String category : categories) {

                        if (category.toLowerCase()
                                .contains(searchText)) {

                            MenuItem item =
                                    new MenuItem(category);

                            item.setOnAction(e -> {

                                searchField.setText(category);

                                suggestionsPopup.hide();

                                performSearch(category);
                            });

                            suggestions.add(item);
                        }
                    }

                    // ===== PROPERTY SUGGESTIONS =====
                    for (String category : categoriesMap.keySet()) {

                        ObservableList<PropertyView> properties =
                                categoriesMap.get(category);

                        for (PropertyView property : properties) {

                            String propertyName =
                                    property.getPropertyName();

                            if (propertyName.toLowerCase()
                                    .contains(searchText)) {

                                MenuItem item =
                                        new MenuItem(propertyName);

                                item.setOnAction(e -> {

                                    searchField.setText(propertyName);

                                    suggestionsPopup.hide();

                                    performSearch(propertyName);
                                });

                                suggestions.add(item);
                            }
                        }
                    }

                    suggestionsPopup.getItems().clear();
                    suggestionsPopup.getItems().addAll(suggestions);

                    if (!suggestions.isEmpty()) {

                        if (!suggestionsPopup.isShowing()) {

                            suggestionsPopup.show(
                                    searchField,
                                    Side.BOTTOM,
                                    0,
                                    0
                            );
                        }

                    } else {

                        suggestionsPopup.hide();
                    }
                });

        // ================= ENTER SEARCH =================
        searchField.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {

                performSearch(searchField.getText());

                suggestionsPopup.hide();
            }
        });
    }

    /* Method used to restore values from the exisiting data (if any) of currently selected batch/record,
     and load them into the ui.
     */
    private void loadPropertyDataFromDB() throws SQLException {
        int testId = basePropertiesState.getTestId();
        propertiesList = propertyService.getPropertiesByTest(testId);

        //=========== iterate through properties and store in statemanager ================
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
                    property.getUnit()
            );
        }
    }

    private void performSearch(String searchText) {

        searchText = searchText.trim().toLowerCase();

        boolean found = false;

        // ===== SEARCH CATEGORY =====
        for (String category : categories) {

            if (category.toLowerCase().equals(searchText)) {

                categoriesListView.getSelectionModel()
                        .select(category);

                categoriesListView.scrollTo(category);

                found = true;
                break;
            }
        }

        // ===== SEARCH PROPERTY =====
        if (!found) {

            for (String category : categoriesMap.keySet()) {

                ObservableList<PropertyView> properties =
                        categoriesMap.get(category);

                for (PropertyView property : properties) {

                    if (property.getPropertyName()
                            .toLowerCase()
                            .equals(searchText)) {

                        categoriesListView
                                .getSelectionModel()
                                .select(category);

                        HandleCategoryChange(category);

                        propertiesListView
                                .getSelectionModel()
                                .select(property);

                        propertiesListView
                                .scrollTo(property);

                        found = true;
                        break;
                    }
                }

                if (found) break;
            }
        }

        if (!found) {

            AlertUtil.showError(
                    "No category or property found for: "
                            + searchText
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
        if (selectedState.getSelectedProperty() != null)
        {
            saveCurrentPropertyValues(selectedState.getSelectedProperty());
        }

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
            //TODO: replace DB call. Use data available in Property object instead.
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
        tempUnitController.setTemperatureUnits();

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

    // ================= SAVE PROPERTY VALUES =================
    // Read values from all dynamic input rows and store them
    // inside their corresponding PropertyValue objects.
    for (InputRow row : inputRows) {

        String valueText = row.getField().getText();

        // Ignore empty rows while navigating between properties/categories.
        if (valueText != null && !valueText.isBlank()) {

            try {

                double propertyVal = Double.parseDouble(valueText.trim());

                // Update the in-memory PropertyValue object.
                row.getPropertyValue().setPropertyVAL(propertyVal);

            } catch (NumberFormatException e) {

                AlertUtil.showError("Invalid property value: " + valueText);
            }
        }
    }

    // ================= SAVE TEMPERATURE =================
    double tempVal = 0;

    String text = temperatureField.getText();

    // Temperature validation
    if (text != null && !text.isBlank()) {

        try {

            tempVal = Double.parseDouble(text.trim());

        } catch (NumberFormatException e) {

            AlertUtil.showError("Please enter a valid temperature.");
        }
    }

    int tempUnitID;
    String tempUnitVal;

    try (Connection conn = DBUtil.getConnection()) {

        tempUnitVal = tempUnitController.getComboBox().getValue();

        // During navigation, unit selection may still be incomplete.
        // Avoid crashing state saving in that case.
        if (tempUnitVal == null || tempUnitVal.isBlank()) {

            tempUnitID = -1;
            tempUnitVal = "";

        } else {

            // Fetch corresponding Unit object from DB.
            Unit unit = temperatureUnitService.getTemperatureUnit(conn, tempUnitVal);

            if (unit != null) {

                tempUnitID = unit.getUnitId();

            } else {

                // Unit name not found in DB.
                tempUnitID = -1;
            }
        }

    } catch (SQLException e) {

        throw new RuntimeException(e);
    }

    // ================= SAVE PROPERTY UNIT =================
    // Only the first row contains the unit dropdown because
    // all rows of a property share the same unit.
    String unitValue = "";

    if (!inputRows.isEmpty()
            && inputRows.get(0).getUnitController() != null
            && inputRows.get(0).getUnitController().getComboBox() != null) {

        unitValue = inputRows.get(0)
                .getUnitController()
                .getComboBox()
                .getValue();
    }

    Unit unit = new Unit();
    unit.setUnit(unitValue);

    // ================= SAVE PROPERTY STATE =================

    stateManager.saveState(
            property.getPropertyName(),

            new ArrayList<>(inputRows),

            //TODO: store temp id
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