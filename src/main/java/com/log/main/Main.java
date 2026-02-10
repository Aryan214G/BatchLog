package com.log.main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    @Override
    public void start(Stage stage)  throws Exception {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/com/log/ui/BaseProperties.fxml"));
        Scene scene = new Scene(loader.load(), 640, 400);

        stage.setTitle("Batchlog");
        stage.setScene(scene);
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}

