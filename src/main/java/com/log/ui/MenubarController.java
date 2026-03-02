package com.log.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.IOException;

public class MenubarController {

    @FXML
    private MenuBar menuBar;

    @FXML
    private void loadBasePropertiesView(ActionEvent event) throws IOException {
        String path = "/com/log/ui/views/BaseProperties.fxml";
        loadScene(event, path);
    }
    private void loadScene(ActionEvent event, String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        String css = getClass().getResource("/com/log/ui/styles/baseCategory.css").toExternalForm();
        scene.getStylesheets().add(css);

        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        }
    @FXML
    private void openSaveFilePopup(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/log/ui/views/saveFile.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root);

        Stage popupStage = new Stage();
        popupStage.setTitle("Save File");
        popupStage.setScene(scene);


        popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);


        Stage owner = (Stage) menuBar.getScene().getWindow();
        popupStage.initOwner(owner);

        popupStage.showAndWait();
    }

    @FXML
    private void openSettingsPagePopup(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/log/ui/views/settingsPage.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root);

        Stage popupStage = new Stage();
        popupStage.setTitle("Settings");
        popupStage.setScene(scene);


        popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);


        Stage owner = (Stage) menuBar.getScene().getWindow();
        popupStage.initOwner(owner);

        popupStage.showAndWait();
    }
}
