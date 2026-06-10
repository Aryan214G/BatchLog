package com.log.ui.settings;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class SettingsPageController {
    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {

        // Load default page when settings opens
        showDefaultsPage();
    }

    private void loadPage(String fxmlFile) {

        try {

            Parent page = FXMLLoader.load(
                    getClass().getResource("/com/log/ui/views/" + fxmlFile)
            );

            contentArea.getChildren().clear();
            contentArea.getChildren().add(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showDefaultsPage() {
        loadPage("DefaultSettings.fxml");
    }

    @FXML
    private void showUnitsPage() {
        loadPage("UnitSettings.fxml");
    }

    @FXML
    private void showDirectionsPage() {
        loadPage("DirectionSettings.fxml");
    }

    @FXML
    private void showCategoriesPage() { loadPage("AddCategorySettings.fxml"); }

    @FXML
    private void showBackupPage(){ loadPage("BackupAndRestorePage.fxml");}
}
