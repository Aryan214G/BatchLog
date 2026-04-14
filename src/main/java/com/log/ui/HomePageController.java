package com.log.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    @FXML private GridPane projectsGrid;

    @FXML private Button newBatchEntryBtn;
    @FXML private Button retrievalPageBtn;
    @FXML private Button settingsPageBtn;
    @FXML private Button helpPageBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadRecentProjects();

        newBatchEntryBtn.setOnAction(e -> navigateTo("/com/log/ui/views/BaseProperties.fxml"));
        retrievalPageBtn.setOnAction(e -> navigateTo("/com/log/ui/views/RetrievalPage.fxml"));
        settingsPageBtn.setOnAction(e -> openSettingsPopup());
        helpPageBtn.setOnAction(e -> handleHelp());
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    private void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) projectsGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSettingsPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/settingsPage.fxml")
            );
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.setTitle("Settings");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.initOwner(projectsGrid.getScene().getWindow());
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleHelp() {
        // TODO: open help dialog or navigate to help page
        System.out.println("Help clicked");
    }

    // ── Projects Grid ─────────────────────────────────────────────────────────

    private void loadRecentProjects() {
        // TODO: Replace with real project data from your data layer
        String[] sampleProjects = {"Project Alpha", "Project Beta", "Project Gamma"};

        int col = 0, row = 0;
        final int maxCols = 3;

        for (String projectName : sampleProjects) {
            projectsGrid.add(createProjectCard(projectName), col, row);
            if (++col >= maxCols) { col = 0; row++; }
        }
    }

    private VBox createProjectCard(String projectName) {
        Label nameLabel = new Label(projectName);
        nameLabel.getStyleClass().add("card-title");

        Label dateLabel = new Label("Last modified: N/A");
        dateLabel.getStyleClass().add("card-subtitle");

        Button openButton = new Button("Open");
        openButton.getStyleClass().add("card-button");
        openButton.setOnAction(e -> System.out.println("Opening: " + projectName));

        VBox card = new VBox(8, nameLabel, dateLabel, openButton);
        card.getStyleClass().add("project-card");
        return card;
    }
}