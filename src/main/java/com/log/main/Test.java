package com.log.main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Test extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        {
            FXMLLoader loader = new FXMLLoader(Test.class.getResource("/com/log/ui/views/BaseProperties.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);

            scene.getStylesheets().add(
                    Test.class.getResource("/com/log/ui/styles/propertiesPage.css").toExternalForm()
            );

            stage.setTitle("BatchLog");
            stage.setScene(scene);
            stage.show();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}

