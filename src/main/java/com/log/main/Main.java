package com.log.main;

import com.log.main.UserDAO;
import javafx.application.Application;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        UserDAO.createTable();
        stage.setTitle("SQLite practice");
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
