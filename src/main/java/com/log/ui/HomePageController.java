package com.log.ui;

import com.log.core.BasePropertiesState;
import com.log.database.DBUtil;
import com.log.model.Project;
import com.log.service.ProjectService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.log.core.StateManager;

public class HomePageController implements Initializable {

    @FXML private VBox projectsList;  // change from GridPane to VBox in FXML

    @FXML private Button retrievalPageBtn;
    @FXML private Button settingsPageBtn;
    @FXML private Button helpPageBtn;
    @FXML private Button newProjectBtn;

    private final BasePropertiesState bpropState = BasePropertiesState.getInstance();
    private final ProjectService projectService = new ProjectService();
    @FXML
    private TextField projectSearchField;

    private List<Project> allProjects = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StateManager.clearAll();
        loadRecentProjects();

        retrievalPageBtn.setOnAction(e -> navigateTo("/com/log/ui/views/RetrievalPage.fxml"));
        settingsPageBtn.setOnAction(e -> openSettingsPopup());
        helpPageBtn.setOnAction(e -> handleHelp());
        newProjectBtn.setOnAction(e -> handleNewProject());
        projectSearchField.textProperty().addListener(
                (obs, oldVal, newVal) ->
                        filterProjects(newVal)
        );
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    private void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) projectsList.getScene().getWindow();
            stage.getScene().setRoot(root);
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
            popupStage.initOwner(projectsList.getScene().getWindow());
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNewProject() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/BaseProperties.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) projectsList.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleHelp() {
        navigateTo("/com/log/ui/views/Help.fxml");
    }

    // ── Projects List ─────────────────────────────────────────────────────────

    private void loadRecentProjects() {

        projectsList.getChildren().clear();

        allProjects = projectService.getAllProjects();

        if (allProjects.isEmpty()) {

            Label empty =
                    new Label(
                            "No projects yet. Create one to get started."
                    );

            empty.getStyleClass().add("empty-label");

            projectsList.getChildren().add(empty);

            return;
        }

        for (Project project : allProjects) {

            projectsList.getChildren().add(
                    createProjectRow(
                            project.getProjectName()
                    )
            );
        }
    }

    private void refreshProjects() {
        loadRecentProjects();
    }

    private HBox createProjectRow(String projectName) {
        // Icon
        Label icon = new Label("📁");
        icon.getStyleClass().add("card-icon");

        // Name
        Label nameLabel = new Label(projectName);
        nameLabel.getStyleClass().add("card-name");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        // Row
        HBox row = new HBox(12, icon, nameLabel);
        row.getStyleClass().add("project-card");
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Left click — open project
        row.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                handleProjectCardOpen(projectName);
            }
        });

        // Right click — context menu
        ContextMenu contextMenu = buildContextMenu(projectName);
        row.setOnContextMenuRequested(e ->
                contextMenu.show(row, e.getScreenX(), e.getScreenY())
        );

        return row;
    }

    private ContextMenu buildContextMenu(String projectName) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem openItem   = new MenuItem("Open");
        MenuItem renameItem = new MenuItem("Rename");
//        MenuItem deleteItem = new MenuItem("Delete");

        openItem.setOnAction(e -> handleProjectCardOpen(projectName));
        renameItem.setOnAction(e -> handleProjectCardEdit(projectName));
//        deleteItem.setOnAction(e -> handleProjectCardDelete(projectName));

        contextMenu.getItems().addAll(openItem, renameItem);
        return contextMenu;
    }

    // ── Card actions ──────────────────────────────────────────────────────────

    private void handleProjectCardDelete(String projectName) {
        try (Connection connection = DBUtil.getConnection()) {
            projectService.deleteProject(projectService.getProjectId(connection, projectName));
            refreshProjects();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleProjectCardEdit(String projectName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/EditProjectPopup.fxml")
            );
            Parent root = loader.load();

            EditProjectPopupController controller = loader.getController();
            controller.setOriginalProjectName(projectName);
            controller.setOnProjectUpdate(this::refreshProjects);

            Stage popupStage = new Stage();
            popupStage.setTitle("Edit Project");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            popupStage.initOwner(projectsList.getScene().getWindow());
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleProjectCardOpen(String projectName) {
        try (Connection conn = DBUtil.getConnection()) {
            int projectId = projectService.getProjectId(conn, projectName);
            bpropState.setProjectId(projectId);
            bpropState.setProjectName(projectName);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/ProjectPage.fxml")
            );
            Parent root = loader.load();

            ProjectPageController controller = loader.getController();
            controller.loadProject(projectName);

            Stage stage = (Stage) projectsList.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterProjects(String searchText) {

        projectsList.getChildren().clear();

        for (Project project : allProjects) {

            if (searchText == null
                    || searchText.isBlank()
                    || project.getProjectName()
                    .toLowerCase()
                    .contains(searchText.toLowerCase())) {

                projectsList.getChildren().add(
                        createProjectRow(
                                project.getProjectName()
                        )
                );
            }
        }
    }
}
