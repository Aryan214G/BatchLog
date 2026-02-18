package com.log.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddAttributesPopupController {

    @FXML
    private TextField attributeNameField;

    private String attributeName;

    public String getAttributeName() {
        return attributeName;
    }

    @FXML
    private void handleSaveAttribute() {

        attributeName = attributeNameField.getText();

        Stage stage = (Stage) attributeNameField.getScene().getWindow();
        stage.close();
    }
}
