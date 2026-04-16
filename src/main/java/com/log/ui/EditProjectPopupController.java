package com.log.ui;

import com.log.database.DBUtil;
import com.log.service.ProjectService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class EditProjectPopupController {

    @FXML private TextField projectNameField;

    private Runnable onProjectUpdate;
    private String originalProjectName;  // set by HomePageController before popup opens

    public void setOnProjectUpdate(Runnable callback) {
        this.onProjectUpdate = callback;
    }

    public void setOriginalProjectName(String name) {
        this.originalProjectName = name;
        projectNameField.setText(name);  // pre-fill the field with the current name
    }

    @FXML
    private void handleUpdateProject() {
        String newName = projectNameField.getText().trim();

        if (newName.isEmpty()) {
            showValidationError("Project name cannot be empty.");
            return;
        }

        try (Connection connection = DBUtil.getConnection()) {
            ProjectService projectService = new ProjectService();
            int projectId = projectService.getProjectId(connection, originalProjectName);

            if (projectId == -1) {
                showValidationError("Project not found.");
                return;
            }

            projectService.editProject(projectId, newName);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (onProjectUpdate != null) {
            onProjectUpdate.run();
        }

        closePopup();
    }

    private void showValidationError(String message) {
        projectNameField.setPromptText(message);
        projectNameField.getStyleClass().add("input-error");
        projectNameField.clear();
    }

    private void closePopup() {
        Stage stage = (Stage) projectNameField.getScene().getWindow();
        stage.close();
    }
}