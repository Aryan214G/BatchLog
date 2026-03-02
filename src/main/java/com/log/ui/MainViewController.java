package com.log.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

    public class MainViewController {

        public void handleNewEntry(ActionEvent event) throws IOException {

            // Load BaseProperties.fxml
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/log/ui/views/BaseProperties.fxml")
            );

            Parent root = loader.load();

            // Get current stage
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            // Set new scene
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

