package com.log.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RetrievalPageController {

    @FXML
    private TextField projectField;

    @FXML
    private TextField productField;

    @FXML
    private TextField BatchIDfield;

    @FXML
    private TextField Dateoftestingfield;

    @FXML
    private TextField Placeoftestingfield;

    @FXML
    private VBox sidebar;

    @FXML
    private Button toggleBtn;

    private boolean isOpen = true;

    // ✅ FIXED SIDEBAR TOGGLE (layout-aware)
    @FXML
    private void toggleSidebar() {

        if (isOpen) {
            sidebar.setVisible(false);
            sidebar.setManaged(false);
        } else {
            sidebar.setVisible(true);
            sidebar.setManaged(true);
        }

        isOpen = !isOpen;
    }

    // ✅ Updated search handler
    @FXML
    private void handleSearch() {

        String project = projectField.getText();
        String product = productField.getText();
        String batchId = BatchIDfield.getText();
        String testDate = Dateoftestingfield.getText();
        String testSite = Placeoftestingfield.getText();

        System.out.println("Project: " + project);
        System.out.println("Product: " + product);
        System.out.println("Batch ID: " + batchId);
        System.out.println("Test Date: " + testDate);
        System.out.println("Test Site: " + testSite);
    }
}