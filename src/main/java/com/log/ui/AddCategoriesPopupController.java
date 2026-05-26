package com.log.ui;

import com.log.core.AppState;
import com.log.model.PropertyView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Optional;

public class AddCategoriesPopupController {

    @FXML
    private TextField propertyNameField;
    @FXML
    private VBox attributesContainer;

    private final AppState appState = AppState.getInstance();


    // 🔥 ADDED: store entered category
    private String enteredCategory;

    // 🔥 ADDED: getter for parent controller
    public String getEnteredCategory() {
        return enteredCategory;
    }

    private ObservableList<PropertyView> propertiesList = FXCollections.observableArrayList();

    public ObservableList<PropertyView> getPropertiesList() {
        return propertiesList;
    }

    private HashMap<String,Integer> entriesMap = new HashMap<>();

    public HashMap<String,Integer> getEntriesMap(){return entriesMap;}

    // ===== NEW PROPERTY BUTTON =====
    @FXML
    private void handleNewProperty() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/PropertiesPopup.fxml")
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Property");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            AddPropertiesPopupController controller = loader.getController();
            String propertyName = controller.getPropertyName();
            int entries = (controller.getPropertyEntries()).intValue();
            entriesMap.put(propertyName,entries);
            if (propertyName != null && !propertyName.isBlank()) {
                //TODO: un-comment later
//                addAttributeCard(propertyName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        // 🔥 STORE VALUE FOR PARENT
        enteredCategory = propertyName;


        showAlert("Success", "Property saved successfully!");

        // 🔥 CLOSE POPUP
        Stage stage = (Stage) propertyNameField.getScene().getWindow();
        stage.close();
    }

    // ===== ATTRIBUTE CARD CREATION =====
    //TODO: un-comment later
//    private void addAttributeCard(String attributeName) {
//
//        HBox card = new HBox();
//        card.setAlignment(Pos.CENTER_LEFT);
//        card.setSpacing(10);
//        card.getStyleClass().add("attributes-card");
//
//        Label label = new Label(attributeName);
//        HBox.setHgrow(label, javafx.scene.layout.Priority.ALWAYS);
//
//        Button editBtn = new Button("✎");
//        editBtn.getStyleClass().add("icon-btn");
//
//        Button deleteBtn = new Button("⌫");
//        deleteBtn.getStyleClass().add("icon-btn");
//
//        // ===== EDIT ACTION =====
//        editBtn.setOnAction(e -> {
//
//            TextInputDialog dialog = new TextInputDialog(label.getText());
//            dialog.setTitle("Edit Attribute");
//            dialog.setHeaderText("Edit Attribute Name");
//
//            Optional<String> result = dialog.showAndWait();
//
//            result.ifPresent(newName -> {
//                if (!newName.trim().isEmpty()) {
//                    label.setText(newName.trim());
//                }
//            });
//        });
//
//        // ===== DELETE ACTION =====
//        deleteBtn.setOnAction(e -> {
//            attributesContainer.getChildren().remove(card);
//            propertiesList.remove(label.getText());
//        });
//
//
//        card.getChildren().addAll(label, editBtn, deleteBtn);
//
//        propertiesList.add(attributeName);
//
//        attributesContainer.getChildren().add(
//                attributesContainer.getChildren().size() - 1,
//                card
//        );
//    }

    // ===== ALERT UTILITY =====
    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
