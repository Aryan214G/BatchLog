package com.log.ui;

import com.log.database.DBUtil;
import com.log.service.ProjectService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.sqlite.core.DB;

import java.sql.Connection;
import java.sql.SQLException;

public class NewProjectPopupController {

    @FXML private TextField projectNameField;


    private Runnable onProjectSaved;

    public void setOnProjectSaved(Runnable callback) {
        this.onProjectSaved = callback;
    }


    @FXML
    private void handleSaveProject() {
        String projectName = projectNameField.getText().trim();

        if (projectName.isEmpty()) {
            showValidationError("Project name cannot be empty.");
            return;
        }
        Connection connection;
        try {
            connection = DBUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ProjectService projectService = new ProjectService();

        try {
            projectService.createProject(connection, projectName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        // TODO: Persist the project to your data layer here
        System.out.println("Saving project: " + projectName);

        if (onProjectSaved != null) {
            onProjectSaved.run();  // notify HomePageController
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