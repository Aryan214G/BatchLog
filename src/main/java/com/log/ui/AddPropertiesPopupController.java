package com.log.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPropertiesPopupController {

    @FXML
    private TextField propertiesNameField;

    private String propertyName;

    public String getPropertyName() {
        return propertyName;
    }

    @FXML
    private void handleSaveProperty() {

        propertyName = propertiesNameField.getText();

        Stage stage = (Stage) propertiesNameField.getScene().getWindow();
        stage.close();
    }
}
