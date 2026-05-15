package com.log.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class FilterPopupController implements Initializable {

    @FXML private VBox columnCheckboxContainer;
    @FXML private VBox propertyCheckboxContainer;

    // Current states passed in from RetrievalResultsController
    private Map<String, Boolean> columnStates  = new LinkedHashMap<>();
    private Map<String, Boolean> propertyStates = new LinkedHashMap<>();

    // Callback fired when user clicks Apply
    private Consumer<FilterResult> onApply;

    public static class FilterResult {
        public final Map<String, Boolean> columnStates;
        public final Map<String, Boolean> propertyStates;

        public FilterResult(Map<String, Boolean> columnStates,
                            Map<String, Boolean> propertyStates) {
            this.columnStates   = columnStates;
            this.propertyStates = propertyStates;
        }
    }

    public void setOnApply(Consumer<FilterResult> callback) {
        this.onApply = callback;
    }

    public void setColumnStates(Map<String, Boolean> states) {
        this.columnStates = new LinkedHashMap<>(states);
    }

    public void setPropertyStates(Map<String, Boolean> states) {
        this.propertyStates = new LinkedHashMap<>(states);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Checkboxes are populated after setColumnStates / setPropertyStates
        // are called — see loadCheckboxes()
    }

    // Called by RetrievalResultsController after passing in states
    public void loadCheckboxes() {
        columnCheckboxContainer.getChildren().clear();
        propertyCheckboxContainer.getChildren().clear();

        for (Map.Entry<String, Boolean> entry : columnStates.entrySet()) {
            CheckBox cb = new CheckBox(entry.getKey());
            cb.setSelected(entry.getValue());
            columnCheckboxContainer.getChildren().add(cb);
        }

        for (Map.Entry<String, Boolean> entry : propertyStates.entrySet()) {
            CheckBox cb = new CheckBox(entry.getKey());
            cb.setSelected(entry.getValue());
            propertyCheckboxContainer.getChildren().add(cb);
        }
    }

    @FXML
    private void handleApply() {
        // Read column checkbox states
        Map<String, Boolean> newColumnStates = new LinkedHashMap<>();
        List<String> columnKeys = new ArrayList<>(columnStates.keySet());
        for (int i = 0; i < columnCheckboxContainer.getChildren().size(); i++) {
            CheckBox cb = (CheckBox) columnCheckboxContainer.getChildren().get(i);
            newColumnStates.put(columnKeys.get(i), cb.isSelected());
        }

        // Read property checkbox states
        Map<String, Boolean> newPropertyStates = new LinkedHashMap<>();
        List<String> propertyKeys = new ArrayList<>(propertyStates.keySet());
        for (int i = 0; i < propertyCheckboxContainer.getChildren().size(); i++) {
            CheckBox cb = (CheckBox) propertyCheckboxContainer.getChildren().get(i);
            newPropertyStates.put(propertyKeys.get(i), cb.isSelected());
        }

        if (onApply != null) {
            onApply.accept(new FilterResult(newColumnStates, newPropertyStates));
        }

        closePopup();
    }

    @FXML
    private void handleCancel() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) columnCheckboxContainer.getScene().getWindow();
        stage.close();
    }
}