package com.log.ui;

import com.log.core.AppState;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class AddPropertiesPopupController {

    @FXML private TextField propertyNameField;
    @FXML private VBox attributesContainer;

    private final AppState appState = AppState.getInstance();

    // ===== INITIALIZATION =====
    @FXML
    public void initialize() {

        // Example preloaded attributes (remove later)
        addAttributeCard("Density");
        addAttributeCard("Open porosity");
    }

    // ===== NEW ATTRIBUTE BUTTON =====
    @FXML
    private void handleNewAttribute() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Attribute");
        dialog.setHeaderText("Enter Attribute Name");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                addAttributeCard(name.trim());
            }
        });
    }

    // ===== SAVE PROPERTY =====
    @FXML
    private void handleSaveProperty() {

        String propertyName = propertyNameField.getText();

        if (propertyName == null || propertyName.isBlank()) {
            showAlert("Validation", "Property name cannot be empty.");
            return;
        }

        System.out.println("Saving Property: " + propertyName);

        // Example: Save to AppState (adapt to your model later)
        appState.getCategories().add(propertyName);

        showAlert("Success", "Property saved successfully!");
    }

    // ===== ATTRIBUTE CARD CREATION =====
    private void addAttributeCard(String attributeName) {

        HBox card = new HBox();
        card.setAlignment(Pos.CENTER_LEFT);
        card.setSpacing(10);
        card.getStyleClass().add("attributes-card");

        Label label = new Label(attributeName);
        HBox.setHgrow(label, javafx.scene.layout.Priority.ALWAYS);

        Button editBtn = new Button("✎");
        editBtn.getStyleClass().add("icon-btn");

        Button deleteBtn = new Button("⋯");
        deleteBtn.getStyleClass().add("icon-btn");

        // ===== EDIT ACTION =====
        editBtn.setOnAction(e -> {

            TextInputDialog dialog = new TextInputDialog(label.getText());
            dialog.setTitle("Edit Attribute");
            dialog.setHeaderText("Edit Attribute Name");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(newName -> {
                if (!newName.trim().isEmpty()) {
                    label.setText(newName.trim());
                }
            });
        });

        // ===== DELETE ACTION =====
        deleteBtn.setOnAction(e -> attributesContainer.getChildren().remove(card));

        card.getChildren().addAll(label, editBtn, deleteBtn);

        attributesContainer.getChildren().add(
                attributesContainer.getChildren().size() - 1,
                card
        );
    }

    // ===== ALERT UTILITY =====
    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
