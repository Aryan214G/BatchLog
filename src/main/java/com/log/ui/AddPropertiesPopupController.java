package com.log.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPropertiesPopupController {

    @FXML
    private TextField propertyNameField;

    @FXML
    private TextField propertyEntriesField;

    private String propertyName;
    private Double propertyEntries;   // numeric value

    public String getPropertyName() {
        return propertyName;
    }

    public Double getPropertyEntries() {
        return propertyEntries;
    }

    @FXML
    private void handleSaveProperty() {

        boolean valid = true;

        String nameInput = propertyNameField.getText();
        String entriesInput = propertyEntriesField.getText();

        // ===== Validate name =====
        if (nameInput == null || nameInput.trim().isEmpty()) {
            propertyNameField.setStyle("-fx-border-color: red;");
            valid = false;
        } else {
            propertyNameField.setStyle(null);
        }

        // ===== Validate numeric input =====
        if (entriesInput == null || entriesInput.trim().isEmpty()) {
            propertyEntriesField.setStyle("-fx-border-color: red;");
            valid = false;
        } else {
            try {
                propertyEntries = Double.parseDouble(entriesInput.trim());
                propertyEntriesField.setStyle(null);
            } catch (NumberFormatException e) {
                propertyEntriesField.setStyle("-fx-border-color: red;");
                valid = false;
            }
        }

        if (!valid) return;

        propertyName = nameInput.trim();
        System.out.println(propertyEntries);
        Stage stage = (Stage) propertyNameField.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void handleCancel() {
        propertyName = null;
        propertyEntries = null;

        Stage stage = (Stage) propertyNameField.getScene().getWindow();
        stage.close();
    }
}