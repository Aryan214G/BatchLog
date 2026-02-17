package com.log.ui;

import com.log.core.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class BasePropertiesController {

    // ===== FXML FIELDS =====
    @FXML private TextField projectName;
    @FXML private TextField batchNo;
    @FXML private TextField productName;
    @FXML private TextField componentID;
    @FXML private DatePicker testDate;
    @FXML private TextField placeOfTesting;
    @FXML private TextField fileName;

    private final AppState appState = AppState.getInstance();

    // ===== INITIALIZATION =====
    @FXML
    public void initialize() {
        // You can preload data here if needed
        System.out.println("Base Properties Loaded");
    }

    // ===== BUTTON ACTIONS =====

    @FXML
    private void handleNext() {

        if (!validateInputs()) {
            System.out.println("Validation failed");
            return;
        }

        String project = projectName.getText();
        String batch = batchNo.getText();
        String product = productName.getText();
        String component = componentID.getText();
        LocalDate date = testDate.getValue();
        String place = placeOfTesting.getText();
        String file = fileName.getText();

        // Example: store something in AppState if needed
        System.out.println("Project: " + project);
        System.out.println("Batch: " + batch);

        // TODO: Save to AppState / Database / Model
        appState.setProjectName(projectName.getText());
        appState.setBatchNo(batchNo.getText());
        appState.setProductName(productName.getText());
        appState.setComponentID(componentID.getText());
        appState.setTestDate(testDate.getValue());
        appState.setPlaceOfTesting(placeOfTesting.getText());
        appState.setFileName(fileName.getText());
        // TODO: Navigate to next screen
    }

    @FXML
    private void handleCancel() {
        clearFields();
    }

    // ===== HELPERS =====

    private boolean validateInputs() {

        if (projectName.getText().isEmpty()) return false;
        if (batchNo.getText().isEmpty()) return false;
        if (productName.getText().isEmpty()) return false;

        return true;
    }

    private void clearFields() {
        projectName.clear();
        batchNo.clear();
        productName.clear();
        componentID.clear();
        placeOfTesting.clear();
        fileName.clear();
        testDate.setValue(null);
    }
}
