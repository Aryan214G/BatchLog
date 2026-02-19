package com.log.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPropertiesPopupController {

    @FXML
    private TextField attributeNameField;

    private String propertyName;

    public String getPropertyName() {
        return propertyName;
    }

    @FXML
    private void handleSaveProperty() {

        String input = attributeNameField.getText();

        if (input == null || input.trim().isEmpty()) {
            attributeNameField.setStyle("-fx-border-color: red;");
            return;
        }

        propertyName = input.trim();

        Stage stage = (Stage) attributeNameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        propertyName = null;
        Stage stage = (Stage) attributeNameField.getScene().getWindow();
        stage.close();
    }
}
