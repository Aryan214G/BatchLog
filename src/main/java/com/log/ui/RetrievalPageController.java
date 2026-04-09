package com.log.ui;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class RetrievalPageController {

    @FXML
    private TextField projectField;

    @FXML
    private TextField productField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField statusField;

    @FXML
    private VBox sidebar;

    @FXML
    private Button toggleBtn;

    private boolean isOpen = true;

    @FXML
    private void toggleSidebar() {

        double targetX = isOpen ? -250 : 0;

        TranslateTransition sidebarAnim =
                new TranslateTransition(Duration.millis(250), sidebar);
        sidebarAnim.setToX(targetX);

        TranslateTransition buttonAnim =
                new TranslateTransition(Duration.millis(250), toggleBtn);
        buttonAnim.setToX(targetX);

        sidebarAnim.play();
        buttonAnim.play();

        isOpen = !isOpen;
    }

    @FXML
    private void handleSearch() {
        System.out.println("Project: " + projectField.getText());
        System.out.println("Product: " + productField.getText());
        System.out.println("Category: " + categoryField.getText());
        System.out.println("Status: " + statusField.getText());
    }
}