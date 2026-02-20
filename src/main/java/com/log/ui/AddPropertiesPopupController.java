package com.log.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPropertiesPopupController {

    @FXML
    private TextField attributeNameField;

    @FXML
    private TextField attributeEntriesField;

    private String propertyName;
    private Double attributeEntries;   // numeric value

    public String getPropertyName() {
        return propertyName;
    }

    public Double getAttributeEntries() {
        return attributeEntries;
    }

    @FXML
    private void handleSaveProperty() {

        boolean valid = true;

        String nameInput = attributeNameField.getText();
        String entriesInput = attributeEntriesField.getText();

        // ===== Validate name =====
        if (nameInput == null || nameInput.trim().isEmpty()) {
            attributeNameField.setStyle("-fx-border-color: red;");
            valid = false;
        } else {
            attributeNameField.setStyle(null);
        }

        // ===== Validate numeric input =====
        if (entriesInput == null || entriesInput.trim().isEmpty()) {
            attributeEntriesField.setStyle("-fx-border-color: red;");
            valid = false;
        } else {
            try {
                attributeEntries = Double.parseDouble(entriesInput.trim());
                attributeEntriesField.setStyle(null);
            } catch (NumberFormatException e) {
                attributeEntriesField.setStyle("-fx-border-color: red;");
                valid = false;
            }
        }

        if (!valid) return;

        propertyName = nameInput.trim();
        System.out.println(attributeEntries);
        Stage stage = (Stage) attributeNameField.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void handleCancel() {
        propertyName = null;
        attributeEntries = null;

        Stage stage = (Stage) attributeNameField.getScene().getWindow();
        stage.close();
    }
}