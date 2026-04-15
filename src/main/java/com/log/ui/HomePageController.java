package com.log.ui;

import com.log.model.Project;
import com.log.service.ProjectService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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
        ProjectService ps = new ProjectService();

        List<Project> projects= ps.getAllProjects();

        int col = 0, row = 0;
        final int maxCols = 2;

        for (Project project : projects) {
            projectsGrid.add(createProjectCard(project.getProjectName()), col, row);
            if (++col >= maxCols) { col = 0; row++; }
        }

        // Make both columns share width equally
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        projectsGrid.getColumnConstraints().addAll(cc, cc);
    }

    private HBox createProjectCard(String projectName) {
        // Left: circle icon
        Label icon = new Label();
        icon.getStyleClass().add("card-icon");

        // Center: project name (grows to fill space)
        Label nameLabel = new Label(projectName);
        nameLabel.getStyleClass().add("card-name");
        HBox.setHgrow(nameLabel, javafx.scene.layout.Priority.ALWAYS);

        // Right: shortcut label
        Label shortcut = new Label("⌘C");
        shortcut.getStyleClass().add("card-shortcut");

        // Right: three-dot menu button
        Button menuBtn = new Button("⋮");
        menuBtn.getStyleClass().add("card-menu-btn");
        menuBtn.setOnAction(e -> handleCardMenu(projectName, menuBtn));

        HBox card = new HBox(10, icon, nameLabel, shortcut, menuBtn);
        card.getStyleClass().add("project-card");
        card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        return card;
    }

    private void handleCardMenu(String projectName, Button anchor) {
        javafx.scene.control.ContextMenu contextMenu = new javafx.scene.control.ContextMenu();

        javafx.scene.control.MenuItem openItem = new javafx.scene.control.MenuItem("Open");
        javafx.scene.control.MenuItem renameItem = new javafx.scene.control.MenuItem("Rename");
        javafx.scene.control.MenuItem deleteItem = new javafx.scene.control.MenuItem("Delete");

        openItem.setOnAction(e -> System.out.println("Open: " + projectName));
        renameItem.setOnAction(e -> System.out.println("Rename: " + projectName));
        deleteItem.setOnAction(e -> System.out.println("Delete: " + projectName));

        contextMenu.getItems().addAll(openItem, renameItem, deleteItem);
        contextMenu.show(anchor, javafx.geometry.Side.BOTTOM, 0, 0);
    }
}